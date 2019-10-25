package com.tch.test.fescar1.service

import com.alibaba.fastjson.JSON
import com.tch.test.tcc.api.TccActionOne
import io.seata.rm.tcc.api.BusinessActionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class TccActionOneImpl : TccActionOne {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    override fun prepare(actionContext: BusinessActionContext, a: Int): Boolean {
        val xid = actionContext.xid
        println("TccActionOne prepare, ${JSON.toJSONString(actionContext)}")
        val name = "tcc-updated-user-1"
        jdbcTemplate.update("update user1 set temp_name = '$name' where id = 1")
        return true
    }

    override fun commit(actionContext: BusinessActionContext): Boolean {
        val xid = actionContext.xid
        jdbcTemplate.update("update user1 set name = temp_name, temp_name = null where id = 1")
        println("TccActionOne commit, xid:" + xid + ", a:" + actionContext.getActionContext("a"))
        return true
    }

    override fun rollback(actionContext: BusinessActionContext): Boolean {
        val xid = actionContext.xid
        jdbcTemplate.update("update user1 set temp_name = null where id = 1")
        println("TccActionOne rollback, xid:" + xid + ", a:" + actionContext.getActionContext("a"))
        return true
    }
}