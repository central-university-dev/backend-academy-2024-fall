package backend.academy.banditos.interfaces;

public class Interfaces {

    public interface AbstractHero {
        default void shoot() { /* Make a shot */ }
    }

    public class BanditHero implements AbstractHero {
        public void detonateBomb() { /* Bandits win */ }
    }

    public class CounterBanditHero implements AbstractHero {
        public void defuseBomb() { /* Counter-bandits win */ }
    }

    public class DoctorHero implements AbstractHero {
        public void heal() { /* Doctor heals */ }
    }

    //

    /** @return {@code true} is user was banned */
    public boolean ban(AbstractHero hero) {
        return switch (hero) {
            case BanditHero bandit ->
                /* check that bandits team members > 3 and ban */
                true;
            case CounterBanditHero counterBanditHero ->
                /* check that counter bandits team members > 2 and ban */
                true;
            default ->
                // No handing of doctor
                throw new IllegalStateException("Unknown hero");
        };
    }
}
