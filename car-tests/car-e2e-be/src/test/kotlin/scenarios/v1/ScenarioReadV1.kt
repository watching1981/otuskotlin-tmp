package com.github.watching1981.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import com.github.watching1981.api.v1.models.*
import com.github.watching1981.e2e.be.base.client.Client
import com.github.watching1981.e2e.be.scenarios.v1.base.sendAndReceive
import com.github.watching1981.e2e.be.scenarios.v1.base.someCreateAd
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadV1(
    private val client: Client,
    private val mode: WorkMode = WorkMode.TEST,
    private val stub:Stub = Stub.SUCCESS
) {
    @Test
    fun read() = runBlocking {
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

        val rObj = AdGetRequest(
            id = cObj.id!!,
        )

        val resRead = client.sendAndReceive(
            "ad/get",
            AdGetRequest(
                requestType = "read",
                id=rObj.id,
                workMode = mode,
                stub = stub

            )
        ) as AdGetResponse
        assertEquals(ResponseResult.SUCCESS, resRead.result)


        val rrObj = resRead.ad ?: fail("No ad in Read response")
        assertEquals(obj.title, rrObj.title)
        assertEquals(obj.description, rrObj.description)
        assertEquals(obj.price, rrObj.price)



        val resDelete = client.sendAndReceive(
            "ad/delete",
            AdDeleteRequest(
                requestType = "delete",
                id = cObj.id!!,
                lock = cObj.lock,
                workMode = mode,
                stub = stub
            )
        ) as AdDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)
        assertEquals(cObj.id, resDelete.deletedAdId)

    }
}