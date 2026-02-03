package com.github.watching1981.app.spring.config

import com.github.watching1981.backend.repo.postgresql.SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties


// Так не работает
//@ConfigurationProperties(prefix = "markeplace.repository.psql")
@ConfigurationProperties(prefix = "psql")
data class AdConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "car-pass",
    var database: String = "cars_ads",
    var schema: String = "public",
    var table: String = "ads",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
