package backend.academy.hierarchy;

public class SealedHierarchy {

    public sealed abstract class Character permits Hero, Enemy {}

    // ==== Heroes =====
    public sealed abstract class Hero extends Character permits Steve, Alex {}

    public final class Steve extends Hero  {}
    public final class Alex extends Hero {}
    /* Других героев быть не может */

    // ==== Enemies =====
    public non-sealed abstract class Enemy extends Character {}

    public final class Zombie extends Enemy {}

    public abstract class Boss extends Enemy {}
    public final class Dragon extends Boss {}
    public final class Warden extends Boss {} // Страж

    /* Могут появляться новые враги, в т.ч. боссы */

    /* ------------------------------------------------------------- */

    public String killMessage(Character character) {
        return switch (character) {
            case Hero hero -> "⚔️ Hero " + switch (hero) {
                case Alex alex -> "Magician Alex was killed: " + alex;
                case Steve steve -> "Warrior Steve was killed" + steve;
            };
            case Enemy enemy -> "💀Enemy" + switch (enemy) {
                case Zombie zombie -> "Zombie was killed: " + zombie;

                case Warden warden -> "Boss Warden was killed: " + warden;
                case Dragon dragon -> "Boss Dragon was killed: " + dragon;
                default -> throw new IllegalStateException("Unknown enemy");
            };
        };
    }
}
