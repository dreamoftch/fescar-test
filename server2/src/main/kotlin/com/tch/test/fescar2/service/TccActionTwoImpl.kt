package com.tch.test.fescar2.service

import com.tch.test.tcc.api.TccActionTwo
import io.seata.rm.tcc.api.BusinessActionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class TccActionTwoImpl : TccActionTwo {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    override fun prepare(actionContext: BusinessActionContext, b: String, list: List<*>): Boolean {
        val xid = actionContext.xid
        val name = "tcc-updated-user-2"
        jdbcTemplate.update("update user2 set temp_name = '$name' where id = 1")
        println("TccActionTwo prepare, xid:" + xid + ", b:" + b + ", c:" + list[1])
        return true
    }

    override fun commit(actionContext: BusinessActionContext): Boolean {
        val xid = actionContext.xid
        jdbcTemplate.update("update user2 set name = temp_name, temp_name = null where id = 1")
        println("TccActionTwo commit, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext.getActionContext("c"))
        return true
    }

    override fun rollback(actionContext: BusinessActionContext): Boolean {
        val xid = actionContext.xid
        jdbcTemplate.update("update user2 set temp_name = null where id = 1")
        println("TccActionTwo rollback, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext.getActionContext("c"))
        return true
    }

}