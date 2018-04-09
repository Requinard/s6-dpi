package com.flink.utils

import com.google.gson.Gson
import java.io.Serializable

object gsonUtils {
    val gson = Gson()

    inline fun <reified T> decode(input: String) = this.gson.fromJson(input, T::class.java)
    inline fun encode(obj: Any) = this.gson.toJson(obj)
    inline fun <reified T> encodeTyped(obj: Any) = this.gson.toJson(obj, T::class.java)
}