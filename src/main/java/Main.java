import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Simulation simulation = new Simulation();
        System.out.println("Выберите режим:\n" +
                "1 - автоматический\n" +
                "2 - пошаговый");
        int mode = input.nextInt();
        switch (mode) {
            case 1:
                simulation.init();
                simulation.run(1);
                break;
            case 2:
                simulation.init();
                simulation.run(2);
                break;
            default:
                System.out.println("Такого режима нет");
        }
    }
}
