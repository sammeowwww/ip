import java.util.List;
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
        Scanner input = new Scanner(System.in);
        ListStorage list = new ListStorage();

        //welcome statement
        printWelcome();

        while (true) {
            String command =  input.nextLine();
            if  (command.equals("bye")) {
                break;
            } else if (command.equals("list")) {
                list.printTasks();
            } else {
                System.out.println("        " + "added: " + command); // reads a command from user
                printLine();
                list.addTask(command);
            }
        }

        input.close();
        printBye();
        printLine();
    }
}