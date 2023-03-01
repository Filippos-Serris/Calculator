package com.example.calculator.network.models

data class Conversion(
    val date: String?,
    val historical: String?,
    val info: Info?,
    val query: Query,
    val result: Double,
    val success: Boolean
) {
    data class Info(
        val rate: Double,
        val timestamp: Int
    )

    data class Query(
        val amount: Double,
        val from: String,
        val to: String
    )
}