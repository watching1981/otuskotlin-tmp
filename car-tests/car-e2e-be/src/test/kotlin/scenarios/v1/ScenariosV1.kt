package com.github.watching1981.e2e.be.scenarios.v1

import com.github.watching1981.api.v1.models.Stub
import com.github.watching1981.api.v1.models.WorkMode
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import com.github.watching1981.e2e.be.base.client.Client

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosV1(
    private val client: Client,
    private val mode: WorkMode = WorkMode.PROD,
    private val stub: Stub = Stub.SUCCESS
) {
    @Nested
    internal inner class CreateDeleteV1: ScenarioCreateDeleteV1(client, mode, stub)
    @Nested
    internal inner class UpdateV1: ScenarioUpdateV1(client, mode, stub)
    @Nested
    internal inner class ReadV1: ScenarioReadV1(client, mode, stub)
    @Nested
    internal inner class SearchV1: ScenarioSearchV1(client, mode, stub)

}