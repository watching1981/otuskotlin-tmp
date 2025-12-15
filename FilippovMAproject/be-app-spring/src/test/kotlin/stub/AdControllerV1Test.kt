package com.github.watching1981.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import com.github.watching1981.app.spring.config.AdConfig
import com.github.watching1981.app.spring.controllers.AdControllerV1Fine
import com.github.watching1981.api.v1.models.*
//import com.github.watching1981.api.v1.mappers.toAdDeleteResponse
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.mappers.v1.*
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(AdControllerV1Fine::class, AdConfig::class)
internal class AdControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: MkplAdProcessor


    @Test
    fun createAd() = testStubAd(
        "/v1/ad/create",
        AdCreateRequest(
            requestType = "create",
            title = "Toyota Camry 2018 года",
            description = "Автомобиль в отличном состоянии, один владелец",
            price = 1500000.0,
            carInfo = CarInfo(
                brand = "Toyota",
                model = "Camry",
                year = 2018,
                mileage = 75000,
                color = "Черный",
                engineType = EngineType.GASOLINE,
                engineVolume = 2.5,
                horsePower = 181,
                transmission = Transmission.AUTOMATIC
            ),
            location = "Москва",
            contactPhone = "+79991234567"
        ),
        MkplContext().toAdCreateResponse().copy(responseType = "create",
        )


    )


    @Test
    fun readAd() = testStubAd(
        "/v1/ad/get",
        AdGetRequest(
            requestType = "get",
            id = 1
        ),
        MkplContext().toAdGetResponse().copy(responseType = "get")
    )

    @Test
    fun updateAd() = testStubAd(
        "/v1/ad/update",
        AdUpdateRequest(
            requestType = "update",
            id = 1,
            title = "Обновленное название",
            description = "Обновленное описание",
            price = 1600000.0
        ),
        MkplContext().toAdUpdateResponse().copy(responseType = "update")
    )

    @Test
    fun deleteAd() = testStubAd(
        "/v1/ad/delete",
        AdDeleteRequest(
            requestType = "delete",
            id = 1),
        MkplContext().toAdDeleteResponse().copy(responseType = "delete")
    )

    @Test
    fun searchAd() = testStubAd(
        "/v1/ad/search",
        AdSearchRequest(
            requestType = "search",
            brand = "Toyota",
            minPrice = 1000000.0,
            maxPrice = 2000000.0
        ),
        MkplContext().toAdSearchResponse().copy(responseType = "search")
    )





    private inline fun <reified Req : Any, reified Res : Any> testStubAd(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value { actualResponse ->
                val actualForComparison = ignoreTimestamps(actualResponse) as Res
                val expectedForComparison = ignoreTimestamps(responseObj) as Res

                assertThat(actualForComparison).isEqualTo(expectedForComparison)
            }
    }
    private fun ignoreTimestamps(obj: Any): Any {
        return when (obj) {
            is AdCreateResponse -> obj.copy(
                timestamp = "IGNORED",
                ad = obj.ad?.ignoreAdTimestamps()
            )
            is AdGetResponse -> obj.copy(
                timestamp = "IGNORED",
                ad = obj.ad?.ignoreAdTimestamps()
            )
            is AdUpdateResponse -> obj.copy(
                timestamp = "IGNORED",
                ad = obj.ad?.ignoreAdTimestamps(),
                previousVersion = obj.previousVersion?.ignoreAdTimestamps()
            )
            is AdDeleteResponse -> obj.copy(
                timestamp = "IGNORED",
                deletionTime = "IGNORED"
            )
            is AdSearchResponse -> obj.copy(
                timestamp = "IGNORED",
                ads = obj.ads?.map { it.ignoreAdTimestamps() }
            )
            else -> obj
        }
    }
    private fun AdData.ignoreAdTimestamps(): AdData = this.copy(
        createdAt = "IGNORED",
        updatedAt = "IGNORED"
    )
}
