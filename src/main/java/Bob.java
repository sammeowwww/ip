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

    private static void printBye() {
        System.out.println("        Bye! See you later alligator!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskList list = new TaskList();

        //welcome statement
        printWelcome();

        while (true) {
            String input =  scanner.nextLine().trim();

            String[] parts = input.split("\\s+", 2);
            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "bye":
                    scanner.close();
                    printBye();
                    printLine();
                    return;

                case "list":
                    list.printTasks();
                    break;

                case "unmark":
                    Integer index1 = Integer.parseInt(arg);
                    list.unmarkTask(index1);
                    break;

                case "mark":
                    Integer index2 = Integer.parseInt(arg);
                    list.markTask(index2);
                    break;

                default:
                    System.out.println("        " + "added: " + input); // reads a command from user
                    printLine();
                    list.addTask(input);
                    break;
            }
        }
    }
}