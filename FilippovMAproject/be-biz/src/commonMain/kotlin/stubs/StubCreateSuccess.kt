package com.github.watching1981.biz.stubs

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.car.logging.common.LogLevel
import com.github.watching1981.common.models.*
import com.github.watching1981.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubCreateSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания объявления
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            val stub = MkplAdStub.prepareResult {
                adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                adRequest.price.takeIf { it!! >0 }?.also { this.price = it }

                // Информация об автомобиле
                adRequest.car?.let { carInfo ->
                    this.car = MkplCar(
                        brand = carInfo.brand,
                        model = carInfo.model,
                        year = carInfo.year,
                        mileage = carInfo.mileage ?: 0,
                        color = carInfo.color ?: "",
                        engine = MkplEngine(
                            type = carInfo.engine.type.let {
                                MkplEngineType.valueOf(it.name)
                            } ,
                            volume = carInfo.engine.volume ?: 0.0,
                            horsePower = carInfo.engine.horsePower ?: 0
                        ),

                        transmission = carInfo.transmission.let {
                            MkplTransmission.valueOf(it.name)
                        }
                    )
                }

                // Местоположение и контакты
                adRequest.location.takeIf { it.isNotBlank() }?.let { this.location = it }
                adRequest.contactPhone.takeIf { it.isNotBlank() }?.let { this.contactPhone = it }

                // Статус
                adRequest.status.let { status ->
                    this.status = McplAdvertisementStatus.valueOf(status.name)
                }
            }
            adResponse = stub
        }
    }
}

