package com.github.watching1981.app.common

import kotlinx.datetime.Clock
import com.github.watching1981.api.log1.mapper.toLog
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.asMkplError
import com.github.watching1981.common.models.MkplState
import kotlin.reflect.KClass

suspend inline fun <T> IMkplAppSettings.controllerHelper(
    crossinline getRequest: suspend MkplContext.() -> Unit,
    crossinline toResponse: suspend MkplContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    return try {
        //getRequest это функция, которая приходит извне (из AdControllerV1Fine при вызове оттуда метода controllerHelper)
        //в качестве первого параметра. По факту это метод fromTransport из AdControllerV1Fine. Он обогащает текущий контекст
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)//выполняем с обогащенным контекстом основной метод цепочки обязаностей
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        //ctx.toResponse() которая приходит извне (из AdControllerV1Fine при вызове оттуда метода controllerHelper)
        //в качестве второго параметра. По факту это метод toTransportAd() из AdControllerV1Fine.
        // Он преобразует получившийся контекст в ответ
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = MkplState.FAILING
        ctx.errors.add(e.asMkplError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
