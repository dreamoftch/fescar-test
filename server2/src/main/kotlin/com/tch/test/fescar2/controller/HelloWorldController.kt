package com.tch.test.fescar2.controller

import io.seata.core.context.RootContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*


/**
 * @description:
 * @author: tianch
 * @create: 2019-01-30
 **/
@RestController
class HelloWorldController {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @GetMapping
    fun test(@RequestParam(value = "xid") xid: String): String {
        setOf(1,2) == setOf(1,2)
        RootContext.bind(xid)
        println("get from context ${RootContext.getXID()}, ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}")
        val name = "updated-user-2"
        jdbcTemplate.update("update user2 set name = '$name' where id = 1")
        System.exit(1)
        return "success"
    }

}