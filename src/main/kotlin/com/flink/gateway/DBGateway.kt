package com.flink.gateway

import com.flink.models.LocationModel
import com.flink.models.LogModel
import com.flink.models.ProductInstanceModel
import com.flink.models.ProductModel
import com.flink.models.UserModel
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

class DBGateway {
    val client by lazy {
        KMongo.createClient()
    }

    val db by lazy {
        client.getDatabase("flink-warehouse")
    }

    val logDatabase by lazy {
        db.getCollection<LogModel>()
    }

    val productInstanceDatabase by lazy {
        db.getCollection<ProductInstanceModel>()
    }

    val productDatabase by lazy {
        db.getCollection<ProductModel>()
    }

    val userDatabase by lazy {
        db.getCollection<UserModel>()
    }

    val locationDatabase by lazy {
        db.getCollection<LocationModel>()
    }
}