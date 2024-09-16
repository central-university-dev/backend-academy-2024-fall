package backend.academy;

public class Main {
    /** ---------------------- */

    public final class Base { }

    /* Compile-time error: Cannot inherit from final 'Base' */
//    public class Derived extends Base { }

    /** ---------------------- */

    sealed class SealedClass { }
    sealed abstract class SealedAbstractClass { }
    sealed interface SealedInterface { }

    non-sealed class SealedSubClass extends SealedClass { }
    non-sealed class SealedSubClass2 extends SealedAbstractClass { }
    non-sealed class SealedImplementation implements SealedInterface { }

    /** ---------------------- */

    public static void main(String[] args) { }
}

