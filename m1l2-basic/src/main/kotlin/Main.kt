package com.github.watching1981



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
inline fun <reified T: Any>typedGeneric(arg: T) {
    println ("type: ${arg::class.simpleName}"+"of T: ${T::class}")
}
fun main(vararg args:String) {
    val name = "Kotlin"
    fun twoSum(nums: IntArray, target: Int): IntArray {
        var i =0
        var res: IntArray= intArrayOf()
        while (i < nums.size - 1){
            for (j in i..nums.size - 1){
                if (nums[i] + nums[j] == target && i != j){
                    val res2= arrayOf(i ,j )
                    return res2.toIntArray()
                    break
                }
            }
            i++
        }
        return res
    }
    fun isPalindrome(x: Int): Boolean {
        val t = x.toString()
        val revText = StringBuilder()
        for (i in t.length -1 downTo 0){
            val char = t[i]
            revText.append(char)
        }
        if (revText.toString()==t ) return true
        return false
    }


    //println("Hello, " + name + "!")
   // println("Args: ${args.joinToString()}")
    //val a: DoubleArray = doubleArrayOf(1.0, 2.0)
    //println(a.joinToString())
    val b = twoSum(arrayOf(3,2,4).toIntArray(),6)
    println(b.joinToString())
    println(isPalindrome(515))
    typedGeneric(5) //почему-то пишет, что рефлексия недоступна

    //лямбды
    //1. присваивание лямбды некой переменной. Здесь после двоеточия в скобках указываются типы принимаемых параметров
    //далее после стрелки идет возвращаемый тип. Далее =  тело лямбды,
    // которое состоит из:{параметры ->действия над ними} }
    val multipl: (Int, Int) -> String = {a,b -> "answer: ${a*b}"}
    println(multipl(5,8)) //вызов переменной с лямбдой (передаются 2 параметра)
    //2. Unit не возвращает ничего, но в теле указано, что надо напечатать текст с переданным параметром
    val x3:(String)->Unit={println("hello, $it")}
    x3("I am")
    //3 передача лямбды в функцию
    fun echo (sayIt:(toPerson:String, withAge:Int)->String, name:String, age:Int){
        println(sayIt(name, age)) //внутри функции вызывается переданная ей лямбда
    }
    echo({toPerson,withAge  -> "Hello $toPerson with $withAge"}, "Alice",33)
    //3 расширение класса String некой лямбдой (расширение описывается после знака "."
    // и вызывается также после точки)
    val greetWithSurname: String.(String) -> Unit = {surname -> println("Hello, $this $surname")}
    greetWithSurname("Ivan", "Gor") //первый параметр это сама переменная типа String, который расширяется,
    //второй параметр - добавляет лямбда, которая выступает в качестве расширения
    //аналогично строке выше
    "Ivan".greetWithSurname("Gor")//на мой взгляд более понятный вызов

    //инфиксные функции (тоже расширения, но ровно с 1 аргументом. Можно вызывать без точки)
    infix fun String.withNum(num:Int)= "$this with $num"
    val m = "My String" withNum 22
    println(m)

    //scope-функции
//1. Пример let
    fun processUserInput(input: String?){
        input?.let({ it ->
            println("processing: $it")
            val processed = it.uppercase()
            println("Result: $processed")
        }) ?: println("input is null")
    }
    processUserInput("Hi!")
    processUserInput(null)

    //коллекции
    val res = (0..10)
    val m2 = res.associateBy{it}

    println(m2)
    println("Homework")

        val input = listOf(
            mapOf(
                "first" to "Иван",
                "middle" to "Васильевич",
                "last" to "Рюрикович",
            ),
            mapOf(
                "first" to "Петька",
            ),
            mapOf(
                "first" to "Сергей",
                "last" to "Королев",
            ),
        )
        val expected = listOf(
            "Рюрикович Иван Васильевич",
            "Петька",
            "Королев Сергей",
        )

    var newList = mutableListOf<String?>()

    input.forEachIndexed { index, value ->
       // println("$index: $value")
        var s:String=""
//        if (value["last"] != null) s=s+value["last"]+" "
//        if (value["first"] != null) s=s+value["first"]+" "
//        if (value["middle"] != null) s=s+value["middle"]
        newList.add("""${value["last"]?: ""} ${value["first"]?: ""} ${value["middle"]?: ""}""".trim())
        //newList.add(s.trim())
    }



    println(expected)

    println(newList)
    println(expected==newList)
}

