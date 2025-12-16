package com.github.watching1981.biz.validation

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdvertisement(
            id = McplAdvertisementId(123),
            title = "Test Car",
            description = "Test Description",
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
}


fun validationPositive(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdvertisement(
            id = McplAdvertisementId(-123),
            title = "Test Car",
            description = "Test Description",
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
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationRange(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdvertisement(
            id = McplAdvertisementId(123000000000),
            title = "Test Car",
            description = "Test Description",
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
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
