package com.tch.test.fescar1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("classpath:spring/*.xml")
class FescarApplication

fun main(args: Array<String>) {
	runApplication<FescarApplication>(*args)
}

