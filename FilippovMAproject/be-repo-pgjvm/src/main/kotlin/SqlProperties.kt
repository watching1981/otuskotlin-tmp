package com.github.watching1981.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "car-pass",
    val database: String = "cars_ads",
    val schema: String = "public",
    val table: String = "mkpl_advertisements",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
