import java.util.Scanner;

public class Bob {
    public static void printLine() {
        System.out.println("        ___________________________________________________________________");
    }

    public static void printBanner() {
        System.out.println("        ____        _     ");
        System.out.println("        |  _ \\      | |    ");
        System.out.println("        | |_) | ___ | |__  ");
        System.out.println("        |  _ < / _ \\| '_ \\ ");
        System.out.println("        | |_) | (_) | |_) |");
        System.out.println("        |____/ \\___/|_.__/ ");
    }

    private static void printWelcome() {
        printLine();
        printBanner();
        printLine();
        System.out.println("        Hey!! I'm Mr Bob, your friendly and personalised assistant.");
        System.out.println("        Anyways, what can i do for you today?");
        printLine();
    }

    private static void printAddedTask(TaskList list, Task task) {
        System.out.println("        Nice!! I've got your task and have added it into the list.");
        System.out.println("        " + task);
        System.out.println("        You have " + list.getTaskCount() + " tasks.");
        printLine();
    }

    private static void printBye() {
        System.out.println("        Bye! See you later alligator!");
    }

    private static void printHelp() {
        System.out.println("        HEEELPPP is on the way!");
        System.out.println("        Here is a list of commands you'll need for this chatbot");
        System.out.println("        1. todo <task>\n" +
                "        2. deadline <task> /by <date>\n" +
                "        3. event <task> /from <date> /to <date>\n" +
                "        4. list\n" +
                "        5. mark <task index>\n" +
                "        6. unmark <task index>\n" +
                "        7. delete <task index>\n" +
                "        8. bye");
        printLine();
    }

    // used perplexity to help refine code
    private static void addToDo(TaskList list, String arg) throws BobException {
        if (arg.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        Task task = new ToDo(arg);
        list.addTask(task);
        printAddedTask(list, task);
    }

    // used perplexity to help refine code
    private static void addDeadline(TaskList list, String arg) throws BobException {
        if (arg.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        String[] parts = arg.split("\\s+/by\\s+", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new BobException("Use: deadline <description> /by <date>.");
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        list.addTask(task);
        printAddedTask(list, task);
    }

    // used perplexity to help refine code
    private static void addEvent(TaskList list, String arg) throws BobException {
        if (arg.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        String[] fromParts = arg.split("\\s+/from\\s+", 2);

        if (fromParts.length < 2) {
            throw new BobException("Use: event <description> /from <date> /to <date>.");
        }

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);

        if (fromParts[0].trim().isEmpty()
                || toParts.length < 2
                || toParts[0].trim().isEmpty()
                || toParts[1].trim().isEmpty()) {
            throw new BobException("Use: event <description> /from <date> /to <date>.");
        }

        Task task = new Event(
                fromParts[0].trim(), // task
                toParts[0].trim(), // from
                toParts[1].trim() // to
        );

        list.addTask(task);
        printAddedTask(list, task);

    }

    private static void unmarkTask(TaskList list, String arg) throws BobException {
        if (arg.isEmpty()) {
            throw new BobException("Use: unmark <task index>.");
        }
        try {
            Integer i = Integer.parseInt(arg);
            list.unmarkTask(i);
            System.out.println("        I have unmarked the task. Please complete it.");
            printLine();
        } catch (NumberFormatException e) {
            throw new BobException("Use: unmark <task index>.");
        }

    }

    private static void markTask(TaskList list, String arg) throws BobException {
        if (arg.isEmpty()) {
            throw new BobException("Use: mark <task index>.");
        }
        try {
            Integer i = Integer.parseInt(arg);
            list.markTask(i);
            System.out.println("        I have marked the task. You're good to go!");
            printLine();
        } catch (NumberFormatException e) {
            throw new BobException("Use: mark <task index>.");
        }
    }

    private static void deleteTask(TaskList list, String arg) throws BobException {
        if (arg.isEmpty()) {
            throw new BobException("Use: delete <task index>.");
        }
        try {
            Integer i = Integer.parseInt(arg);
            list.deleteTask(i);
            System.out.println("        I have deleted the task. You're good to go!");
            System.out.println("        You have " + list.getTaskCount() + " tasks.");
            printLine();
        } catch (NumberFormatException e) {
            throw new BobException("Use: delete <task index>.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskList list = new TaskList();

        //welcome statement
        printWelcome();

        while (true) {
            try {
                String input =  scanner.nextLine().trim();

                if  (input.isEmpty()) {
                    throw new BobException("Enter a command! " +
                            "If you need a list of commands, please type 'help'. ");
                }

                String[] parts = input.split("\\s+", 2);
                String command = parts[0];
                String arg = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "help":
                        printHelp();
                        break;

                    case "bye":
                        scanner.close();
                        printBye();
                        printLine();
                        return;

                    case "list":
                        list.printTasks();
                        break;

                    case "unmark":
                        unmarkTask(list, arg);
                        break;

                    case "mark":
                        markTask(list, arg);
                        break;

                    case "todo":
                        addToDo(list, arg);
                        break;

                    case "deadline":
                        addDeadline(list, arg);
                        break;

                    case "event":
                        addEvent(list, arg);
                        break;

                    case "delete":
                        deleteTask(list, arg);
                        break;

                    default:
                        throw new BobException("Invalid command :(. If you need help, type 'help'!!\n" +
                                "        Bob will be on his wayyyy.");
                }
            } catch (BobException e) {
                System.out.println("        " + e.getMessage());
                printLine();
            }
        }
    }
}