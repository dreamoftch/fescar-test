package com.tch.fescartest.tcc.service

import com.tch.test.tcc.api.TccActionOne
import com.tch.test.tcc.api.TccActionTwo
import io.seata.core.context.RootContext
import io.seata.spring.annotation.GlobalTransactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Service
class TccTransactionService {

    @Resource
    private lateinit var tccActionOne: TccActionOne

    @Resource
    private lateinit var tccActionTwo: TccActionTwo

    /**
     * 发起分布式事务
     *
     * @return string string
     */
    @GlobalTransactional
    fun doTransactionCommit(): String {
        //第一个TCC 事务参与者
        var result = tccActionOne.prepare(null, 1)
        if (!result) {
            throw RuntimeException("TccActionOne failed.")
        }
        val list = ArrayList<Any>()
        list.add("c1")
        list.add("c2")
        result = tccActionTwo.prepare(null, "two", list)
        if (!result) {
            throw RuntimeException("TccActionTwo failed.")
        }
        println("before commit...")
        TimeUnit.SECONDS.sleep(6)
        return RootContext.getXID()
    }

    /**
     * Do transaction rollback string.
     *
     * @param map the map
     * @return the string
     */
    @GlobalTransactional
    fun doTransactionRollback(map: MutableMap<Any, Any>): String {
        //第一个TCC 事务参与者
        var result = tccActionOne.prepare(null, 1)
        if (!result) {
            throw RuntimeException("TccActionOne failed.")
        }
        val list = ArrayList<Any>()
        list.add("c1")
        list.add("c2")
        result = tccActionTwo.prepare(null, "two", list)
        if (!result) {
            throw RuntimeException("TccActionTwo failed.")
        }
        map["xid"] = RootContext.getXID()
        println("before throw exception")
        TimeUnit.SECONDS.sleep(6)
        throw RuntimeException("transaction rollback")
    }

}