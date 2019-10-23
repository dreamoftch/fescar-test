package com.tch.fescartest

import com.tch.fescartest.tcc.service.TccTransactionService
import io.seata.spring.annotation.GlobalTransactionScanner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("classpath:spring/*.xml")
class SeataTccTest {

    @Bean
    fun getGlobalTransactionScanner(): GlobalTransactionScanner {
        val applicationId = "my_test_app"
        val transactionServiceGroup = "my_test_tx_group"
        return GlobalTransactionScanner(applicationId, transactionServiceGroup)
    }

}

fun main(args: Array<String>) {
    val applicationContext = SpringApplication.run(SeataTccTest::class.java, *args)
    val tccTransactionService = applicationContext.getBean(TccTransactionService::class.java)
    println("doTransactionCommit result: " + tccTransactionService.doTransactionCommit())
//    val map = mutableMapOf<Any, Any>()
//    println("doTransactionCommit result: " + tccTransactionService.doTransactionRollback(map) + " , map: $map")
}

