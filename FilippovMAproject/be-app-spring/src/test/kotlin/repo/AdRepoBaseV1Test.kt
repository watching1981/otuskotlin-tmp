package com.github.watching1981.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import com.github.watching1981.api.v1.models.*

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.mappers.v1.*
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.Test

internal abstract class AdRepoBaseV1Test {
    protected abstract var webClient: WebTestClient //это реактивный клиент для тестирования веб-приложений Spring,
    // позволяет тестировать контроллеры (AdControllerV1Fine) без запуска полноценного сервера.
    protected val testAdId = 1

    @Test
    open fun createAd() = testRepoAd(
        "create",
       MkplAdStub.get().toAdCreateRequest(),
       prepareCtx(MkplAdStub.prepareResult {
           id = MkplAdvertisementId(testAdId.toLong())
           status=McplAdvertisementStatus.DRAFT
           authorId = MkplUserId.NONE
           lock = MkplAdLock.NONE
       })
           .toAdCreateResponse()
    )

    @Test
    open fun readAd() = testRepoAd(
        "get",
        AdGetRequest(
            requestType = "get",
            id =MkplAdStub.take().toAdGetRequest().id,
        ),
        prepareCtx(MkplAdStub.take())
            .toAdGetResponse()
    )

    @Test
    open fun updateAd() = testRepoAd(
        "update",
        MkplAdStub.prepareResult {
            title = "Обновленный Toyota Camry"
            description = "Обновленное описание"
            price = 1000000.0
            lock =MkplAdLock("124-354")

        }.toAdUpdateRequest(),
        prepareCtx(MkplAdStub.prepareResult {
            title = "Обновленный Toyota Camry"
            description = "Обновленное описание"
            price = 1000000.0
        })
            .toAdUpdateResponse()
    )

    @Test
    open fun deleteAd() = testRepoAd(
        "delete",
        MkplAdStub.get().toAdDeleteRequest(),  //преобразуем стабовое объявление в формат запроса на удаление для трансп. модели
        prepareCtx(MkplAdStub.get()) //обогащаем контекст стабовым объявлением.
            .toAdDeleteResponse() //преобразуем текущий обогащенный контекст в ожидаемый ответ для удаления
    )

    @Test
    open fun searchAd() = testRepoAd(
        "search",
        AdSearchRequest(
                brand = "Toyota",
                minPrice = 1_000_000.0,
                maxPrice = 2_000_000.0
        ),

        MkplContext(
            state = MkplState.RUNNING,
            adsResponse = (MkplAdStub.prepareSearchList("xx", McplAdvertisementStatus.DRAFT) + MkplAdStub.take()).toMutableList(),
        ).toAdSearchResponse()

    )


    private fun prepareCtx(ad: MkplAdvertisement, workMode: MkplWorkMode = MkplWorkMode.TEST) = MkplContext(
        state = MkplState.RUNNING,
        workMode = workMode,
        adResponse = ad,
    )

    private inline fun <reified Req : Any, reified Res : Any> testRepoAd(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/ad/$url") //обращаемся к соответствующему url эндпоинта (все действия при вызове описаны в AdControllerV1Fine )
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj)) //вставка тела запроса для последующей отправки
            .exchange()  // Отправка запроса (вызов по url в эндпоинте)
            .expectStatus().isOk
            .expectBody(Res::class.java) //проверка тела ответа как объекта
            .value { it -> //проверка тела ответа как объекта
                println("requestObj: $requestObj")
                println("fact RESPONSE: $it")
                println("expected RESPONSE: $expectObj")

                when (it) {
                    is AdCreateResponse -> {
                        assertThat(it.result).isEqualTo(ResponseResult.SUCCESS)
                        val expected = expectObj as AdCreateResponse
                        assertThat(it.ad?.title).isEqualTo(expected.ad!!.title)
                        assertThat(it.ad?.price).isEqualTo(expected.ad!!.price)
                        assertThat(it.ad?.carInfo).isEqualTo(expected.ad!!.carInfo)
                        assertThat(it.ad?.status).isEqualTo(expected.ad!!.status)
                    }
                    is AdGetResponse -> {
                         val expected = expectObj as AdGetResponse
                         assertThat(it.ad?.title).isEqualTo(expected.ad!!.title)
                         assertThat(it.ad?.price).isEqualTo(expected.ad!!.price)
                    }
                    is AdUpdateResponse -> {
                        val expected = expectObj as AdUpdateResponse
                        assertThat(it.ad?.title).isEqualTo(expected.ad!!.title)
                        assertThat(it.ad?.price).isEqualTo(expected.ad!!.price)
                    }
                    is AdDeleteResponse -> {
                        val expected = expectObj as AdDeleteResponse
                        assertThat(it.result).isEqualTo(expected.result)
                    }
                    is AdSearchResponse -> {
                        val expected = expectObj as AdSearchResponse
                        val sortedResp=it.copy(
                            timestamp = "IGNORED",
                            ads = it.ads?.sortedBy { it.id }?.map { it.ignoreAdTimestamps() }
                        )
                        assertThat(sortedResp.responseType).isEqualTo(expected.responseType)
                        assertThat(sortedResp.result).isEqualTo(expected.result)


                    }
                }
            }
    }

}
