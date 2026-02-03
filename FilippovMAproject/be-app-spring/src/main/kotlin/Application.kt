package com.github.watching1981.app.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication  //эта аннотация запускает всю цепочку
//над ней стоят другие аннотации, которые будут
// включать в себя все свойства "родительской" @SpringBootApplication,
// например @ComponentScan, которая сначала просканирует все классы
// внутри пакета, где расположен класс Application (пакет controllers)
//и учтет все аннотации, которые стоят над этими классами внутри пакета
//найдет там например аннотацию @RestController (в классе AdControllerV1Fine), над которой стоит
// аннотация @Controller, над которой стоит аннотация @Component
// которая создаст экземпляры всех классов над которой расположена. Соответствено, будет автоматически
//создан экземпляр класса AdControllerV1Fine например
class Application


// swagger URL: http://localhost:8080/swagger-ui.html

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
