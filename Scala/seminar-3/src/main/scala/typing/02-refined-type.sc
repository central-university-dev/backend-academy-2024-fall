case class User(firstName: String)

val validUserQuestionMark = User("hello")

final case class FirstName private (wrapper: String) extends AnyVal // <= Values class for not overloading runtime

object FirstName {
  inline def apply(value: String): Option[FirstName] = // <= @inline to reduce calls of functions
    if (
    value.nonEmpty && value.capitalize == value && !value.contains(" ")
  ) Some(new FirstName(value))
  else None
}

case class ValidUser(firstName: FirstName)

val validUser = FirstName("Grisha").map(ValidUser.apply)


// A value class …
//
//… must have only a primary constructor with exactly one public, val parameter whose type is not a user-defined value class. (From Scala 2.11.0, the parameter may be non-public.)
//… may not have @specialized type parameters.
//… may not have nested or local classes, traits, or objects.
//… may not define concrete equals or hashCode methods.
//… must be a top-level class or a member of a statically accessible object.
//… can only have defs as members. In particular, it cannot have lazy vals, vars, or vals as members.
//… cannot be extended by another //class.