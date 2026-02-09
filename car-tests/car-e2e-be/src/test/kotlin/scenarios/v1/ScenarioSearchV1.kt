package com.github.watching1981.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import com.github.watching1981.api.v1.models.*
import com.github.watching1981.e2e.be.base.client.Client
import com.github.watching1981.e2e.be.scenarios.v1.base.sendAndReceive
import com.github.watching1981.e2e.be.scenarios.v1.base.someCreateAd
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV1(
    private val client: Client,
    private val mode: WorkMode = WorkMode.TEST,
    private val stub:Stub = Stub.SUCCESS
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateAd,
            someCreateAd.copy(title = "Toyota 2020 года"),
            someCreateAd.copy(title = "Toyota 2022 года"),
            someCreateAd.copy(carInfo = CarInfo(brand = "Toyota",model = "RAV4", year = 2022, engineType = EngineType.GASOLINE, transmission = Transmission.AUTOMATIC) ),
        ).map { obj ->
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
            cObj
        }

        val resSearch = client.sendAndReceive(
            "ad/search",
            AdSearchRequest(
                requestType = "search",
                brand = "Toyota",
                workMode = mode,
                stub = stub

            )
        ) as AdSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj= resSearch.ads ?: fail("No ads in Search response")
        val titles = rsObj.map { it.title }
        assertContains(titles, "Toyota 2020 года")
        assertContains(titles, "Toyota 2022 года")


        val models = rsObj.map { it.carInfo?.model }
        assertContains(models, "RAV4")
        assertContains(models, "Camry")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "ad/delete", AdDeleteRequest(
                    requestType = "delete",
                    id = obj.id!!,
                    lock = obj.lock,
                    workMode = mode,
                    stub = stub
                )
            ) as AdDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}