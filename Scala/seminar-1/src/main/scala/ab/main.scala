package ab

class A {
  private[this] val objectPrivateValue: Boolean = true

  private val privateValue: Boolean = true

  protected val protectedValue: Boolean = true

  private[ab] val packageSpecificValue: Boolean = true

  val publicValue: Boolean = true

  {
    objectPrivateValue
    privateValue
    protectedValue
    packageSpecificValue
    publicValue
  }

  def doSomething(other: A): Unit = {
    // other.objectPrivateValue так нельзя
    other.privateValue
    other.protectedValue
    other.packageSpecificValue
    other.publicValue
  }
}

class B extends A {
  val protectedValueFromExample = protectedValue
}

@main
def main(): Unit = {
  val a = new A()
  val b = new B()

//  a.objectPrivateValue
//  a.privateValue
//  a.protectedValue
  a.packageSpecificValue
  a.publicValue

  a.doSomething(b)

}
