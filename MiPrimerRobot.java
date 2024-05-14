import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

class Racer extends Robot {
    private int maxBeepers;
    public Racer(int street, int avenue, Direction direction, int beepers, Color color, int maxBeepers) {
        super(street, avenue, direction, beepers, color);
        this.maxBeepers = maxBeepers;
        World.setupThread(this);
    }

    public void race() {
        System.out.print(anyBeepersInBeeperBag());
        // putBeeper();
        // move();
        // turnOff();
    }

    public int getMaxBeepers() {
        return maxBeepers;
    }

    public void run() {
        race();
    }
}

// this class create the robots
class RobotFactory implements  Directions {
    public static Racer[] createRobots(String[] args) {
        int r = 1; // Valor por defecto
        boolean e = false;

        // Procesar los argumentos de la línea de comandos
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-r") && i + 1 < args.length) {
                try {
                    r = Integer.parseInt(args[i + 1]);
                    if (r != 1 && r != 2 && r != 4) {
                        System.out.println("El valor de -r debe ser 1, 2, o 4");
                        return null;
                    }
                    i++; // Saltar al siguiente argumento
                } catch (NumberFormatException ex) {
                    System.out.println("El valor de -r debe ser un número");
                    return null;
                }
            } else if (args[i].equals("-e")) {
                e = true;
            }
        }

        // Create robots
        List<Color> colores = new ArrayList<>();
        colores.add(Color.blue);
        colores.add(Color.red);
        colores.add(Color.green);
        colores.add(Color.yellow);

        int[] quantity_of_beepers = { 1, 2, 4, 8 };
        Racer[] racers = new Racer[r];
        int[] start_position = { 3, 5, 6, 7 };

        if (e) {
            // -e is a parameter so all robots have the same number of beepers
            System.out.println("All robots have the same number of beepers: ");
            int n = quantity_of_beepers[(int) (Math.random() * quantity_of_beepers.length)];
            for (int i = 0; i < r; i++) {
                racers[i] = new Racer(start_position[i], 2, East, 0, colores.get(i % colores.size()), n);
            }
        } else {
            // -e is not a parameter so all robots have different number of beepers
            System.out.println("All robots have different number of beepers: ");
            for (int i = 0; i < r; i++) {
                int n = quantity_of_beepers[(int) (Math.random() * quantity_of_beepers.length)];
                racers[i] = new Racer(start_position[i], 2, East, 0, colores.get(i % colores.size()), n);
            }
        }

        // Print all parameters of racers
        for (int i = 0; i < r; i++) {
            System.out.println("Robot " + i + " has " + racers[i].getMaxBeepers() + " beepers max.");
        }

        return racers;
    }

    public static void startRobots(Racer[] racers) {
        System.out.println('\n' + "Start the searching: ");
        // Start robots
        Thread[] threads = new Thread[racers.length];
        for (int i = 0; i < racers.length; i++) {
            final int index = i;
            threads[i] = new Thread(() -> racers[index].run());
            threads[i].start();
        }
    }
}

// this class create the beepers in the map
// public class BeepersFactory {

// }

public class MiPrimerRobot {
    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);

        Racer[] racers = RobotFactory.createRobots(args);
        if (racers != null) {
            RobotFactory.startRobots(racers);
        }
    }

}
