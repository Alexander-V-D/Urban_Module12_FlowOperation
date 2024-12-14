import kotlinx.coroutines.flow.*

suspend fun main() {
    //Задача 1
    println("Пример выполнения решения для задачи 1: " +
            raiseToDegreeAndReduceFlowOfNumbers(numbersListToFlow(listOf(1, 2, 3, 4, 5))))

    //Задача 2
    val persons10 = listOf(
        Person("Иван", 19),
        Person("Сергей", 22),
        Person("Дмитрий", 21),
        Person("Аркадий", 33),
        Person("Семён", 28),
        Person("Виктор", 26),
        Person("Алексей", 31),
        Person("Олег", 35),
        Person("Геннадий", 43),
        Person("Ян", 29)
    ).asFlow()
    flow { emit(persons10) }
    print("Выполнение решения задачи 2: ")
    persons10.getPerson("Д", 21)

    //Задача 3
    val persons = mutableListOf<Person>()
    val names = listOf("Петр", "Николай", "Василий").asFlow()
    val bankCards = List (3) { generateBankCardNumber() }.asFlow()
    val passwords = List (3) { generatePIN() }.asFlow()

    val personsFlow = zip3Flows(names, bankCards, passwords) { name, cart, password ->
        Person(name, cart, password)
    }
    flow { emit(personsFlow) }
    personsFlow.collect { person -> persons.add(person)}
    println("Выполнение решения задачи 3: $persons")
}

//Функции для задачи 1
fun numbersListToFlow(numbers: List<Int>) = numbers.asFlow()

suspend fun raiseToDegreeAndReduceFlowOfNumbers(numbers: Flow<Int>) =
    numbers.map { number -> number * number }.reduce { acc, value -> acc + value }

//Функция для задачи 2
suspend fun Flow<Person>.getPerson(first: String, age: Int) {
    this.filter {
            person: Person -> (person.name.first().toString() == first) && (person.age == age)
    }.collect { println(it) }
}

//Функции для задачи 3
fun generateBankCardNumber(): String {
    var number = ""
    for (i in 1..4) {
        repeat(4) { number += (0..9).random().toString() }
        if (i < 4) number += " "
    }
    return number
}

fun generatePIN(): String {
    var pin = ""
    repeat(4) { pin += (0..9).random().toString() }
    return pin
}

fun <T1, T2, T3, R> zip3Flows(first: Flow<T1>,
                              second: Flow<T2>,
                              third: Flow<T3>,
                              function: suspend (T1, T2, T3) -> R): Flow<R> {
    class TwoFlows(val t1: T1, val t2: T2)
    val twoFlows = first.zip(second) { f1, f2 -> TwoFlows(f1, f2)}
    val zipped = twoFlows.zip(third) { f1, f2 -> function(f1.t1, f1.t2, f2)}
    return zipped
}