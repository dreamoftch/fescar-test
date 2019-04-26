package com.tch.test.fescar1.controller

import io.seata.core.context.RootContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


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
        RootContext.bind(xid)
        println("get from context ${RootContext.getXID()}")
        val name = "updated-user-1"
        jdbcTemplate.update("update user1 set name = '$name' where id = 1")
        return "success"
    }

}