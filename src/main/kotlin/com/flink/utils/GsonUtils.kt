package com.flink.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object gsonUtils {
    val gson = Gson()

    inline fun <reified T> decode(input: String) = this.gson.fromJson(input, T::class.java)
    inline fun encode(obj: Any) = this.gson.toJson(obj)
    inline fun <reified T> encodeTyped(obj: Any) = this.gson.toJson(obj, T::class.java)
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
inline fun <reified T> Gson.toJson(obj: Any) = this.toJson(obj, object : TypeToken<T>() {}.type)
