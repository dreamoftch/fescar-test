package com.tch.test.fescar1

import com.alibaba.fescar.rm.RMClientAT
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FescarApplication

fun main(args: Array<String>) {
	runApplication<FescarApplication>(*args)
	val applicationId = "my_test_app1"
	val transactionServiceGroup = "my_test_tx_group1"
	RMClientAT.init(applicationId, transactionServiceGroup)
}

