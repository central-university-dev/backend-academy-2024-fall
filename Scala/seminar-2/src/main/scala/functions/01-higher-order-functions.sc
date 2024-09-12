// Functions as first-class values

val salary: Int = 20_000
val doubleSalary: Int => Int = (x: Int) => x * 2
doubleSalary(salary) // 40000

// Functions that accept functions

object SalaryRaiser:
  def smallPromotion(salary: Int): Int = (salary * 1.1).toInt
  def greatPromotion(salary: Int): Int = (salary * math.log(salary)).toInt
  def hugePromotion(salary: Int): Int = salary * salary
end SalaryRaiser

SalaryRaiser.smallPromotion(salary) // 22000
SalaryRaiser.greatPromotion(salary) // 198069
SalaryRaiser.hugePromotion(salary) // 400000000

object SalaryRaiserButMoreFlexible:
  def promotion(salary: Int, promotionFunction: Int => Int): Int = promotionFunction(salary)
end SalaryRaiserButMoreFlexible

SalaryRaiserButMoreFlexible.promotion(salary, SalaryRaiser.smallPromotion) // 22000
SalaryRaiserButMoreFlexible.promotion(salary, SalaryRaiser.greatPromotion) // 198069
SalaryRaiserButMoreFlexible.promotion(salary, SalaryRaiser.hugePromotion) // 400000000
SalaryRaiserButMoreFlexible.promotion(salary, (salary: Int) => salary * 2) // 40000

// Functions that return functions

def salaryBuilder(percent: Double): Int => Int = (salary: Int) => (salary * percent).toInt

val smallPromotion = salaryBuilder(0.6)
val greatPromotion = salaryBuilder(2)
smallPromotion(salary) // 12000
greatPromotion(salary) // 40000

SalaryRaiserButMoreFlexible.promotion(salary, smallPromotion) // 12000

// Home tasks:
// 1. Remember `SalaryRaiser`? Reimplement methods from it by using `SalaryRaiserButMoreFlexible.promotion`
// 2. Implement `urlBuilder` function that accepts protocol, host and port and returns function that accepts path and returns full url
