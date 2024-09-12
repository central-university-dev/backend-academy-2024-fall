// Closures

// example from 1st seminar
enum Planet(mass: Double, radius: Double):
  private final val G = 6.67300e-11
  def surfaceGravity = G * mass / (radius * radius) // closure
  def surfaceWeight(otherMass: Double) =
    otherMass * surfaceGravity // also closure

  case Mercury extends Planet(3.303e+23, 2.4397e6)
  case Earth extends Planet(5.976e+24, 6.37814e6)

// No home task