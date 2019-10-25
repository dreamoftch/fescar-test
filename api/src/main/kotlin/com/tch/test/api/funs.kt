package com.tch.test.api

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getTime(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}