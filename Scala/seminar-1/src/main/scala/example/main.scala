package example

@main
def main(): Unit = {
  val a = new A()
  val b = new B()

//  a.objectPrivateValue
//  a.privateValue
//  a.protectedValue
//  a.packageSpecificValue
  a.publicValue

  a.doSomething(b)
}
