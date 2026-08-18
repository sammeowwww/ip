public class ListStorage {
    private String[] list;
    private int count;

    public ListStorage() {
        this.list = new String[100];
        this.count = 0;
    }

    public void addTask(String task) {
        list[count] = task;
        count++;
    }

    public void printTasks() {
        if (count == 0) {
            System.out.println("Your list is empty");
            Bob.printLine();
        } else {
            for (int i = 1; i <= count; i++) {
                System.out.println("        " + i + ". " + list[i - 1]);
            }
            Bob.printLine();
        }
    }
}
