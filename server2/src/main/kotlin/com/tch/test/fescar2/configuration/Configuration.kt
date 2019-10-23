package com.tch.test.fescar2.configuration

import com.alibaba.druid.pool.DruidDataSource
import io.seata.rm.datasource.DataSourceProxy
import io.seata.spring.annotation.GlobalTransactionScanner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate


/**
 * @description:
 * @author: tianch
 * @create: 2019-01-30
 **/
@Configuration
class Configuration {

    @Bean(initMethod = "init", destroyMethod = "close")
    fun getDataSource(): DruidDataSource {
        return DruidDataSource().apply {
            url = "jdbc:mysql://localhost:3306/test2"
            username = "root"
            password = "root"
            driverClassName = "com.mysql.jdbc.Driver"
        }
    }

    @Bean
    fun getDataSourceProxy(dataSource: DruidDataSource): DataSourceProxy {
        return DataSourceProxy(dataSource)
    }

    @Bean
    fun getJdbcTemplate(dataSourceProxy: DataSourceProxy): JdbcTemplate {
        return JdbcTemplate().apply {
            this.dataSource = dataSourceProxy
        }
    }

    @Bean
    fun getGlobalTransactionScanner(): GlobalTransactionScanner {
        val applicationId = "my_test_app2"
        val transactionServiceGroup = "my_test_tx_group"
        return GlobalTransactionScanner(applicationId, transactionServiceGroup)
    }
}