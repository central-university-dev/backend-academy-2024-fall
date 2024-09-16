package backend.academy.banditos.sealed;

public class Sealed {

    sealed interface Hero
        permits Bandit, CounterBandit, Doctor {

        default void shoot() { /* Make a shot */ }
    }

    non-sealed class Doctor implements Hero {
        public void heal() { /* Doctor heals */ }
    }

    non-sealed class Bandit implements Hero {
        public void detonateBomb() { /* Terrorists win */ }
    }

    non-sealed class CounterBandit implements Hero {
        public void defuseBomb() { /* Counter-terrorists win */ }
    }

    private void hero_bar() {

        var bandit = new Bandit();
        var counterBandit = new CounterBandit();

        bandit.detonateBomb();
        counterBandit.defuseBomb();

        // Compile-time error
//        bandit.defuseBomb();
//        counterBandit.detonateBomb();
    }

    /** @return {@code true} if user was banned */
    private boolean ban (Hero hero) {
        return switch (hero) {
            case Bandit bandit ->
                /* check that bandits team members > 3 and ban */
                true;
            case CounterBandit counterBandit ->
                /* check that counter bandits team members > 2 and ban */
                true;
            case Doctor doctor ->
            /** no specific action is needed */
                true;
        };
    }

}
