package com.tch.test.fescar1.controller

import com.tch.test.api.getTime
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
        val name = "updated-user-1"
        println("begin test ${getTime()}")
        jdbcTemplate.update("update user1 set name = '$name' where id = 1")
        println("finish test ${getTime()}")
        return "success"
    }

    @GetMapping(path = ["/otherTest"])
    fun otherTest(@RequestParam(value = "xid") xid: String): String {
        RootContext.bind(xid)
        val name = "another-updated-user-1"
        println("begin otherTest ${getTime()}")
        jdbcTemplate.update("update user1 set name = '$name' where id = 1")
        println("finish otherTest ${getTime()}")
        return "success"
    }

}