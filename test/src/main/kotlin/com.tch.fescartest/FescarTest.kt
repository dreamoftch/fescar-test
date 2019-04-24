package com.tch.fescartest

import com.alibaba.fescar.core.context.RootContext
import com.alibaba.fescar.tm.TMClient
import com.alibaba.fescar.tm.api.TransactionalExecutor
import com.alibaba.fescar.tm.api.TransactionalTemplate
import com.github.kevinsawicki.http.HttpRequest
import java.util.concurrent.TimeUnit

private const val APPLICATION_ID = "my_test_app"
private const val TX_SERVICE_GROUP = "my_test_tx_group"

fun main(args: Array<String>) {
    TMClient.init(APPLICATION_ID, TX_SERVICE_GROUP)
    testException()
    testNormal()
//    testTimeout()
//    testCrash()
}

private fun testCrash() {
    val name = "testCrash"
    doInGlobalTransaction(10000, "tx_$name") {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name secondResult: $secondResult")
        "success"
    }
}

private fun testNormal() {
    val name = "testNormal"
    doInGlobalTransaction(10000, "tx_$name") {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        println("$name secondResult: $secondResult")
        "success"
    }
}

private fun testException() {
    val name = "testException"
    doInGlobalTransaction(10000, "tx_$name") {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        println("$name secondResult: $secondResult")
        throw RuntimeException("我要出错了。。。")
    }
}

private fun testTimeout() {
    val name = "testTimeout"
    doInGlobalTransaction(2000, "tx_$name") {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        println("$name secondResult: $secondResult")
        TimeUnit.SECONDS.sleep(5)
        "success"
    }
}

private fun doInGlobalTransaction(timeout: Int, globalTransactionName: String, block: () -> Any) {
    val transactionalTemplate = TransactionalTemplate()
    try {
        transactionalTemplate.execute(object: TransactionalExecutor {
            override fun timeout(): Int {
                return timeout
            }
            override fun execute(): Any {
                return block()
            }
            override fun name(): String {
                return globalTransactionName
            }
        })
    } catch (e: TransactionalExecutor.ExecutionException) {
        e.printStackTrace()
    }
}