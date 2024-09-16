package backend.academy.banditos.enums;

public class Enums {

    private void hero_foo() {

        EnumHero bandit = EnumHero.BANDIT;
        bandit.detonateBomb();

        EnumHero counterBandit = EnumHero.COUNTER_BANDIT;
        counterBandit.defuseBomb();

        bandit.defuseBomb(); // throws an exception
        counterBandit.detonateBomb(); // throws an exception

    }

    public enum EnumHero {
        BANDIT,
        COUNTER_BANDIT,
        DOCTOR;

        public void detonateBomb() {
            if (this == BANDIT) {
                // Bandits win
            } else {
                throw new UnsupportedOperationException(
                    "Only bandits can detonate bombs"
                );
            }
        }

        public void defuseBomb() {
            if (this == COUNTER_BANDIT) {
                // Counter-bandits win
            } else {
                throw new UnsupportedOperationException(
                    "Only counter-bandits can defuse bombs"
                );
            }
        }
    }

    /** @return {@code true} if user was banned */
    public boolean ban(EnumHero hero) {
        return switch (hero) {
            case BANDIT ->
                /* check that bandits team members > 3 and ban */
                true;
            case COUNTER_BANDIT ->
                /* check that counter bandits team members > 2 and ban */
                true;
            case DOCTOR ->
            /** no specific action is needed */
                true;
        };
    }
}
