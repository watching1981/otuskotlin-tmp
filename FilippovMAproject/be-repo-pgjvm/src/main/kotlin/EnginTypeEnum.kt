package com.github.watching1981.backend.repo.postgresql

import com.github.watching1981.common.models.MkplEngineType
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject


fun Table.engineTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.ENGINE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.ENGINE_TYPE_GAS -> MkplEngineType.GASOLINE
            SqlFields.ENGINE_TYPE_DIESEL -> MkplEngineType.DIESEL
            SqlFields.ENGINE_TYPE_HYBRID -> MkplEngineType.HYBRID
            SqlFields.ENGINE_TYPE_ELECTRIC -> MkplEngineType.ELECTRIC
            else -> MkplEngineType.GASOLINE
        }
    },
    toDb = { value ->
        when (value) {
            MkplEngineType.GASOLINE -> PgEnginTypeGas
            MkplEngineType.DIESEL -> PgEngineTypeDiesel
            MkplEngineType.HYBRID -> PgEngineTypeHybrid
            MkplEngineType.ELECTRIC -> PgEngineTypeElectric
        }
    }
)

sealed class PgEngineTypeValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.ENGINE_TYPE
        value = eValue
    }
}

object PgEnginTypeGas: PgEngineTypeValue(SqlFields.ENGINE_TYPE_GAS) {
    private fun readResolve(): Any = PgEnginTypeGas
}

object PgEngineTypeDiesel: PgEngineTypeValue(SqlFields.ENGINE_TYPE_DIESEL) {
    private fun readResolve(): Any = PgEngineTypeDiesel
}

object PgEngineTypeHybrid: PgEngineTypeValue(SqlFields.ENGINE_TYPE_HYBRID) {
    private fun readResolve(): Any = PgEngineTypeHybrid
}
object PgEngineTypeElectric: PgEngineTypeValue(SqlFields.ENGINE_TYPE_ELECTRIC) {
    private fun readResolve(): Any = PgEngineTypeElectric
}