package com.tch.test.fescar2.controller

import com.tch.test.api.getTime
import io.seata.core.context.RootContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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
        val name = "updated-user-2"
        println("begin test ${getTime()}")
        jdbcTemplate.update("update user2 set name = '$name' where id = 1")
//        TimeUnit.SECONDS.sleep(10)
        println("finish test ${getTime()}")
        return "success"
    }

}