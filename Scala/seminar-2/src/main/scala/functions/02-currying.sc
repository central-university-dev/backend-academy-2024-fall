// Currying

val sum: (Int, Int) => Int = (x, y) => x + y // _ + _
val curriedSum: Int => Int => Int = x => y => x + y
sum.curried

curriedSum(2)(2) == sum(2, 2)

// Partial application

// example from previous home task #3:
def urlBuilder(protocol: String, host: String): String => String = (path: String) => protocol + "://" + host + path

def urlBuilderCurried(protocol: String, host: String)(path: String): String = protocol + "://" + host + path

val googleHost = urlBuilderCurried("https", "google.com")
val googleSearch = googleHost("/search") // https://google.com/search

// Home tasks:
// 1. Implement a method, which would make currying for bi-functor: `def curried(f: (Int, Int) => Int): Int => Int => Int = ???`
// 2. Implement a method, which would take function and 2 parameters and return a value of summary of these parameters after taking function on each of parameters. Cover with examples
