package backend.academy.seminar14_example.services.commands;

import backend.academy.seminar14_example.annotations.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandHandler {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler() {
        Command helpCommandInstance = new HelpCommand(commands);
        registerCommand(helpCommandInstance);
    }

    // Метод публичный для упрощения.
    // По сути, можно его перевести в package и рядом написать отдельный бин для загрузки команд
    public void registerCommand(Command command) {
        CommandDefinition helpDefinition = command.getClass().getAnnotation(CommandDefinition.class);
        String helpName = helpDefinition.name();
        commands.put(helpName, command);
        for (String alias : helpDefinition.aliases()) {
            commands.put(alias, command);
        }
    }

    public void executeCommand(String input) {
        String[] args = input.split(" ");
        if (args.length == 0) {
            return;
        }
        String name = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if (!commands.containsKey(name)) {
            System.out.printf("Command '%s' doesn't exist.", name);
            commands.get("help").execute();
            return;
        }
        Command command = commands.get(name);
        CommandDefinition commandDefinition = command.getClass().getAnnotation(CommandDefinition.class);
        if (commandDefinition.argsNumber() != args.length) {
            System.err.printf("Invalid call! Right signature is '%s'\n", commandDefinition.arguments());
            return;
        }
        // Можно тут во время практики усложнить и добавить аннотацию,
        // которая будет отвечать за метод выполнения:
        // например, динамически валидировать и рассчитывать аргументы
        command.execute(args);
        System.out.println("Please, input the new command:");
    }

    @CommandDefinition(
        name = "help",
        description = "List all possible commands"
    )
    private static class HelpCommand implements Command {
        private final Map<String, Command> commands;

        public HelpCommand(Map<String, Command> commands) {
            this.commands = commands;
        }

        @Override
        public void execute(String... args) {
            System.out.println("Available commands:");
            commands.forEach((k, v)->{
                CommandDefinition definition = v.getClass().getAnnotation(CommandDefinition.class);
                System.out.printf(" | %s %s - %s\n", k, definition.arguments(), definition.description());
            });
        }
    }

}
