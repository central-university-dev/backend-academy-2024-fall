package backend.academy.immutable;

public class Immutable {

    public class JavaString {
        private char[] chars;

        // ...
        public char charAt(int index) {
            return chars[index];
        }
        // ...
    }

    public class MyString extends JavaString { /* ... */ }

    public void registerUser(JavaString name, JavaString password)              {}
}
