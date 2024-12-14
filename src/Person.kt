class Person{
    val name: String
    var age: Int = 0
    var cart: String = ""
    var password: String = ""

    constructor(name: String, bankCard: String, pin: String) {
        this.name = name
        this.cart = bankCard
        this.password = pin
    }

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }

    override fun toString(): String {
        return if (age == 0) {
            "Person(name=$name, cart=$cart, password=$password)"
        } else {
            "Person(name=$name, age=$age)"
        }
    }
}
