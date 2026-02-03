package com.github.watching1981.backend.repo.postgresql

import com.github.watching1981.common.models.MkplTransmission
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.transmissionEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.TRANSMISSION,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.TRANSMISSION_MAN -> MkplTransmission.MANUAL
            SqlFields.TRANSMISSION_VAR -> MkplTransmission.VARIATOR
            SqlFields.TRANSMISSION_ROBOT -> MkplTransmission.ROBOT
            SqlFields.TRANSMISSION_AUTOM -> MkplTransmission.AUTOMATIC
            else -> MkplTransmission.MANUAL
        }
    },
    toDb = { value ->
        when (value) {
            MkplTransmission.MANUAL -> PgTransmissionMan
            MkplTransmission.VARIATOR -> PgTransmissionVar
            MkplTransmission.ROBOT -> PgTransmissionRobot
            MkplTransmission.AUTOMATIC -> PgTransmissionAutom

        }
    }
)

sealed class PgTransmissionValue(enVal: String): PGobject() {
    init {
        type = SqlFields.TRANSMISSION
        value = enVal
    }
}

object PgTransmissionMan: PgTransmissionValue(SqlFields.TRANSMISSION_MAN) {
    private fun readResolve(): Any = PgTransmissionMan
}

object PgTransmissionVar: PgTransmissionValue(SqlFields.TRANSMISSION_VAR) {
    private fun readResolve(): Any = PgTransmissionVar
}

object PgTransmissionAutom: PgTransmissionValue(SqlFields.TRANSMISSION_AUTOM) {
    private fun readResolve(): Any = PgTransmissionAutom
}

object PgTransmissionRobot: PgTransmissionValue(SqlFields.TRANSMISSION_ROBOT) {
    private fun readResolve(): Any = PgTransmissionRobot
}