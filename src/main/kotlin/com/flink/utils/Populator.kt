package com.flink.utils

import com.flink.gateway.DBGateway
import com.flink.models.LocationModel
import com.flink.models.ProductModel
import com.flink.models.UserModel
import com.flink.models.UserModelType.MANAGER
import com.flink.models.UserModelType.PICKER
import com.flink.models.UserModelType.SUPPLIER
import com.flink.models.UserModelType.USER

object Populator {
    val db by lazy { DBGateway() }

    val products = listOf(
            ProductModel("Smart TV"),
            ProductModel("Bluetooth Muis"),
            ProductModel("Whiteboard")
    )

    val users = listOf(
            UserModel("sjors", PICKER),
            UserModel("henk", MANAGER),
            UserModel("peter", SUPPLIER),
            UserModel("monica", PICKER),
            UserModel("marije", USER)
    )

    val locations = listOf(
            LocationModel("Warehouse 1", "sjorstraat 1"),
            LocationModel("Warehouse 2", "nope")
    )

    @JvmStatic
    fun main(args: Array<String>) {
        db.productDatabase.apply {
            drop()
            db.productDatabase.insertMany(products)
        }
        db.userDatabase.apply {
            drop()
            db.userDatabase.insertMany(users)
        }
        db.locationDatabase.apply {
            drop()
            insertMany(locations)
        }
        db.logDatabase.drop()
        db.productInstanceDatabase.drop()
    }
}
