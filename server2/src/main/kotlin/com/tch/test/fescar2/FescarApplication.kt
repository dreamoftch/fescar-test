package com.tch.test.fescar2

import com.alibaba.fescar.rm.RMClientAT
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FescarApplication

fun main(args: Array<String>) {
	runApplication<FescarApplication>(*args)
	val applicationId = "my_test_app2"
	val transactionServiceGroup = "my_test_tx_group2"
	RMClientAT.init(applicationId, transactionServiceGroup)

}

