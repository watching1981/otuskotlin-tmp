package com.github.watching1981.biz.validation

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MkplAdStub.get()

fun validationPriceCorrect(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdvertisement(
            id = stub.id,
            title = "Test_Car",
            description = "Test_Description",
            price = 1500000.0,
            lock = MkplAdLock("123-234-abc-ABC"),
            car = MkplCar(
                brand = "Toyota",
                model = "Camry",
                year = 2020,
                mileage = 50000,
                color = "Black",
                engine = MkplEngine(MkplEngineType.GASOLINE, 2.5, 200),
                transmission = MkplTransmission.AUTOMATIC
            ),
            status = McplAdvertisementStatus.ACTIVE,
            location = "Moscow",
            contactPhone = "+79991234567",
            authorId = MkplUserId(123),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("Test_Description", ctx.adValidated.description)
}


fun priceRange(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdvertisement(
            id = stub.id,
            title = "Test_Car",
            description = "Test_Description",
            price =-10000.0,
            lock = MkplAdLock("123-234-abc-ABC"),
            car = MkplCar(
                brand = "Toyota",
                model = "Camry",
                year = 2020,
                mileage = 50000,
                color = "Black",
                engine = MkplEngine(MkplEngineType.GASOLINE, 2.5, 200),
                transmission = MkplTransmission.AUTOMATIC
            ),
            status = McplAdvertisementStatus.ACTIVE,
            location = "Moscow",
            contactPhone = "+79991234567",
            authorId = MkplUserId(123),
        ),
    )
    processor.exec(ctx)
    assertEquals(MkplState.FAILING, ctx.state)
    assertEquals(2, ctx.errors.size)  //2 ошибки так как нарушены 2 проверки (1 цена положительна и 2 находится в диапазоне)
    assertEquals("validation-price-empty", ctx.errors.first().code)
}

