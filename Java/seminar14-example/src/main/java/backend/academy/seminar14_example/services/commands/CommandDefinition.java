package backend.academy.seminar14_example.services.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandDefinition {
    String name();
    String description();
    String arguments() default "";
    String[] aliases() default {};
    int argsNumber() default 0;

}
