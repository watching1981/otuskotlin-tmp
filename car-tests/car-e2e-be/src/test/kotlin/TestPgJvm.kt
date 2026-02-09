import com.github.watching1981.api.v1.models.WorkMode
import com.github.watching1981.e2e.be.base.BaseContainerTest
import com.github.watching1981.e2e.be.base.client.Client
import com.github.watching1981.e2e.be.base.client.RestClient
import com.github.watching1981.e2e.be.docker.SpringDockerCompose
import com.github.watching1981.e2e.be.scenarios.v1.ScenariosV1
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestPgJvm: BaseContainerTest(SpringDockerCompose) {
    private val client: Client = RestClient(compose)

    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, WorkMode.PROD)


}