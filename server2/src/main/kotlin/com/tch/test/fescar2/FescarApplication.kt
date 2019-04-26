package com.tch.test.fescar2

import io.seata.rm.RMClient
import io.seata.tm.TMClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FescarApplication

fun main(args: Array<String>) {
	runApplication<FescarApplication>(*args)
	val applicationId = "my_test_app2"
	val transactionServiceGroup = "my_test_tx_group"
	TMClient.init(applicationId, transactionServiceGroup)
	RMClient.init(applicationId, transactionServiceGroup)

}

