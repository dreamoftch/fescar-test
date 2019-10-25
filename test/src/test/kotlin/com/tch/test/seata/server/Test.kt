package com.tch.test.seata.server

import java.util.concurrent.ConcurrentHashMap

fun main(args: Array<String>) {
    val map = ConcurrentHashMap<String, String>()
    map["1"] = "1"
    map["2"] = "2"
    map["3"] = "3"
//    map.forEach {
//        if (it.key == "2") {
//            map.remove(it.key)
//        }
//    }
    map.entries.forEach {
        if (it.key == "2") {
            map.remove(it.key, it.value)
        }
    }
    println(map)
}