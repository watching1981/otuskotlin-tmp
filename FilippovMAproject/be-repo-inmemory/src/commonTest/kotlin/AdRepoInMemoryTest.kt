import com.github.watching1981.backend.repo.tests.*
import com.github.watching1981.repo.common.AdRepoInitialized
import com.github.watching1981.repo.inmemory.AdRepoInMemory

class AdRepoInMemoryCreateTest : RepoAdCreateTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(randomUuid = { uuidNew.asLong()}),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryDeleteTest : RepoAdDeleteTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryReadTest : RepoAdReadTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,

    )
}
// класс AdRepoInMemorySearchTest возвращает объект типа RepoAdSearchTest, который в свою очередь требует переопределения переменной repo,
//которая является в данном случае экземпляром класса AdRepoInitialized. Итак создаем repo путем передачи репозитория AdRepoInMemory,
//в котором определены методы, которые будут использоваться в зависимости от возвращаемого типа (например для возвращаемого RepoAdSearchTest()
// будет использован метод searchAd из класса AdRepoInMemory, так как в RepoAdSearchTest есть обращение к searchAd)
//initObjects будут взяты также из RepoAdSearchTest путем создания 3 инициализирующих тестовых моделей с различными описаниями
//то есть фактически будут созданы 3 объявления.
//Итак, в итоге будет передан репозиторий AdRepoInMemory, создан экземпляр RepoAdSearchTest с 3-мя тестовыми объявлениями
// , в котором будет выполнен тест searchAuthor, внутри которого будет выполнен метод репозитория searchAd
class AdRepoInMemorySearchTest : RepoAdSearchTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,

    )
}

class AdRepoInMemoryUpdateTest : RepoAdUpdateTest() {
    override val repo = AdRepoInitialized(
        AdRepoInMemory(),
        initObjects = initObjects,
    )
}
