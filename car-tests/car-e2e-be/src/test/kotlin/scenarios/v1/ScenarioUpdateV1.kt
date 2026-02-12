package com.github.watching1981.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import com.github.watching1981.api.v1.models.*
import com.github.watching1981.e2e.be.base.client.Client
import com.github.watching1981.e2e.be.scenarios.v1.base.sendAndReceive
import com.github.watching1981.e2e.be.scenarios.v1.base.someCreateAd
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val mode: WorkMode = WorkMode.TEST,
    private val stub:Stub = Stub.SUCCESS
) {
    @Test
    fun update() = runBlocking {
        val obj = someCreateAd
        val resCreate = client.sendAndReceive(
            "ad/create",
            AdCreateRequest(
                requestType = "create",
                title = obj.title,
                description = obj.description,
                price = obj.price,
                carInfo = obj.carInfo,
                location = obj.location,
                contactPhone = obj.contactPhone,
                workMode = mode,
                stub = stub
            )
        ) as AdCreateResponse

        assertEquals(ResponseResult.SUCCESS, resCreate.result)

        val cObj = resCreate.ad ?: fail("No ad in Create response")
        assertEquals(obj.title, cObj.title)
        assertEquals(obj.description, cObj.description)
        assertEquals(obj.price, cObj.price)
        assertEquals(cObj.carInfo?.brand, cObj.carInfo?.brand)
        assertEquals(cObj.carInfo?.model, cObj.carInfo?.model)

        val uObj = AdUpdateRequest(
            id = cObj.id!!,
            lock = cObj.lock,
            title = "Toyota Camry 2019 года в хорошем состоянии",
            description = "Автомобиль в хорошем состоянии, один хозяин, полная сервисная история. Без ДТП.",
            price = 1000000.0,
            carInfo = cObj.carInfo

        )
        val resUpdate = client.sendAndReceive(
            "ad/update",
            AdUpdateRequest(
                requestType = "update",
                id = uObj.id,
                title = uObj.title,
                description = uObj.description,
                lock = uObj.lock,
                price = uObj.price,
                carInfo = uObj.carInfo,
                workMode = mode,
                stub = stub
            )
        ) as AdUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj = resUpdate.ad ?: fail("No ad in Update response")
        assertEquals(uObj.title, ruObj.title)
        assertEquals(uObj.description, ruObj.description)
        assertEquals(uObj.price, ruObj.price)

        val resDelete = client.sendAndReceive(
            "ad/delete",
            AdDeleteRequest(
                requestType = "delete",
                id = ruObj.id!!,
                lock = ruObj.lock,
                workMode = mode,
                stub = stub
            )
        ) as AdDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)
        assertEquals(cObj.id, resDelete.deletedAdId)

    }
}