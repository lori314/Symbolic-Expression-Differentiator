import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ArrayList<String> input = manager.read();
        manager.print(input);
    }
}
