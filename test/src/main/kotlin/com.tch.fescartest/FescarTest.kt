package com.tch.fescartest

import com.github.kevinsawicki.http.HttpRequest
import io.seata.core.context.RootContext
import io.seata.rm.RMClient
import io.seata.tm.TMClient
import io.seata.tm.api.TransactionalExecutor
import io.seata.tm.api.TransactionalTemplate
import io.seata.tm.api.transaction.RollbackRule
import io.seata.tm.api.transaction.TransactionInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

private const val APPLICATION_ID = "my_test_app"
private const val TX_SERVICE_GROUP = "my_test_tx_group"

fun main(args: Array<String>) {
    TMClient.init(APPLICATION_ID, TX_SERVICE_GROUP)
    RMClient.init(APPLICATION_ID, TX_SERVICE_GROUP)
    testException()
//    val countDownLatch = CountDownLatch(2)
//    thread {
//        try {
//            testNormal()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        countDownLatch.countDown()
//    }
//    thread {
//        try {
//            TimeUnit.SECONDS.sleep(4)
//            anotherTestNormal()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        countDownLatch.countDown()
//    }
//    testTimeout()
//    testCrash()
//    testTCCrash()

//    testMultipleGlobalTransactions()

//    countDownLatch.await()
}



private fun testMultipleGlobalTransactions() {
    val pool = Executors.newFixedThreadPool(2)
    pool.execute {
        doInGlobalTransaction(30000) {
            val name = "firstTransaction-1"
            println("${getTime()} $name begin...")
            val xid = RootContext.getXID()
            val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
            println("${getTime()} $name firstResult: $firstResult")
            TimeUnit.SECONDS.sleep(25)
            println("${getTime()} $name finish...")
            "success"
        }
    }
    TimeUnit.SECONDS.sleep(10)
    pool.execute {
        doInGlobalTransaction(10000) {
            val name = "Transaction-2"
            println("${getTime()} $name begin...")
            val xid = RootContext.getXID()
            val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
            println("${getTime()} $name firstResult: $firstResult")
            println("${getTime()} $name finish...")
            "success"
        }
    }
    pool.shutdown()
}

private fun testTCCrash() {
    val name = "testTCCrash"
    doInGlobalTransaction(30000) {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name secondResult: $secondResult")
        TimeUnit.SECONDS.sleep(15)
        println("$name after sleep")
        "success"
    }
}

private fun testCrash() {
    val name = "testCrash"
    doInGlobalTransaction(10000) {
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
    doInGlobalTransaction(20000) {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        println("$name secondResult: $secondResult")
        "success"
    }
}

private fun anotherTestNormal() {
    val name = "anotherTestNormal"
    doInGlobalTransaction(10000) {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/otherTest?xid=$xid").body()
        println("$name firstResult: $firstResult")
        "success"
    }
}

private fun testException() {
    val name = "testException"
    doInGlobalTransaction(20000) {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("$name firstResult: $firstResult")
        println("$name secondResult: $secondResult")
        TimeUnit.SECONDS.sleep(10)
        throw RuntimeException("我要出错了。。。")
    }
}

private fun testTimeout() {
    val name = "testTimeout"
    println("${getTime()} testTimeout begin...")
    doInGlobalTransaction(5000) {
        val xid = RootContext.getXID()
        println("$name xid is $xid")
        val firstResult = HttpRequest.get("http://localhost:8081/?xid=$xid").body()
        val secondResult = HttpRequest.get("http://localhost:8082/?xid=$xid").body()
        println("${getTime()} $name firstResult: $firstResult")
        println("${getTime()} $name secondResult: $secondResult")
        TimeUnit.SECONDS.sleep(10)
        println("${getTime()} $name after sleep...")
        "success"
    }
    println("${getTime()} testTimeout finish...")
}

private val formatter = DateTimeFormatter.ISO_DATE_TIME
private fun getTime(): String {
    return LocalDateTime.now().format(formatter)
}

private fun doInGlobalTransaction(timeout: Int, block: () -> Any) {
    val transactionalTemplate = TransactionalTemplate()
    try {
        transactionalTemplate.execute(object: TransactionalExecutor {
            override fun getTransactionInfo(): TransactionInfo {
                return TransactionInfo().apply {
                    this.timeOut = timeout
                    this.name = "my_test_tx_group"
//                    this.rollbackRules = setOf(
//                            RollbackRule(Exception::class.java)
//                    )
                }
            }
            override fun execute(): Any {
                return block()
            }
        })
    } catch (e: TransactionalExecutor.ExecutionException) {
        println("${getTime()} doInGlobalTransaction exception occur...")
        e.printStackTrace()
    }
}