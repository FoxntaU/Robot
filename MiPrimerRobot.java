import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

class Racer extends Robot {
    private int maxBeepers;
    private int[] deliveryPosition;
    private int currentStreet;
    private int currentAvenue;
    private int startStreet;
    private int startAvenue;
    private static List<int[]> beeperLocations = new ArrayList<>();

    public Racer(int street, int avenue, Direction direction, int beepers, Color color, int maxBeepers) {
        super(street, avenue, direction, beepers, color);
        this.maxBeepers = maxBeepers;
        this.deliveryPosition = new int[]{maxBeepers, 1}; // Calle en la posición 0 y la avenida en la posición 1
        this.currentStreet = street;
        this.currentAvenue = avenue;
        this.startStreet = street;
        this.startAvenue = avenue;
        World.setupThread(this);
    }

    public void race() {
        while (true) {
            int beepersCollected = 0;
            boolean foundBeepers = false;

            // Recoger beepers hasta alcanzar la capacidad máxima o no encontrar más beepers
            while (beepersCollected < maxBeepers && !outOfBoundaries()) {
                if (nextToABeeper()) {
                    pickBeeper();
                    beepersCollected++;
                    System.out.println("Robot at (" + currentStreet + ", " + currentAvenue + ") picked up a beeper.");
                    foundBeepers = true;
                    updateBeeperLocations(currentStreet, currentAvenue);
                }

                if (beepersCollected < maxBeepers) {
                    if (frontIsClear() && !nextToARobot()) {
                        move();
                        updatePosition();
                    } else {
                        if (!turnToClearDirection()) {
                            break; // Si no hay ninguna dirección clara, termina la carrera
                        }
                    }
                }
            }

            if (beepersCollected > 0) {
                // Llevar los beepers a la ubicación designada
                deliverBeepers();

                // Volver a la posición inicial
                returnToStart();
            } else if (!foundBeepers) {
                // Si no se encontraron más beepers, terminar la carrera
                returnToStart();
                turnOff();
                break;
            }
        }
    }

    public void deliverBeepers() {
        System.out.println("Heading to deliver beepers to (" + deliveryPosition[0] + ", " + deliveryPosition[1] + ")");
        moveToLocation(deliveryPosition[0], deliveryPosition[1]);
        while (anyBeepersInBeeperBag()) {
            putBeeper();
            System.out.println("Delivered a beeper to (" + deliveryPosition[0] + ", " + deliveryPosition[1] + ")");
        }
    }

    public void returnToStart() {
        System.out.println("Returning to start at (" + startStreet + ", " + startAvenue + ")");
        moveToLocation(startStreet, startAvenue);
        System.out.println("Returned to start at (" + startStreet + ", " + startAvenue + ")");
    }

    private boolean outOfBoundaries() {
        return currentStreet < 1 || currentStreet > 8 || currentAvenue < 1 || currentAvenue > 10;
    }

    private boolean turnToClearDirection() {
        for (int i = 0; i < 4; i++) {
            turnLeft();
            if (frontIsClear() && !nextToARobot()) {
                return true;
            }
        }
        return false;
    }

    private void moveToStreet(int targetStreet) {
        while (currentStreet != targetStreet) {
            if (currentStreet < targetStreet) {
                while (!facingNorth()) {
                    turnLeft();
                }
            } else if (currentStreet > targetStreet) {
                while (!facingSouth()) {
                    turnLeft();
                }
            }

            if (frontIsClear()) {
                move();
                updatePosition();
            } else {
                break;
            }
        }
    }

    private void moveToAvenue(int targetAvenue) {
        while (currentAvenue != targetAvenue) {
            if (currentAvenue < targetAvenue) {
                while (!facingEast()) {
                    turnLeft();
                }
            } else if (currentAvenue > targetAvenue) {
                while (!facingWest()) {
                    turnLeft();
                }
            }

            if (frontIsClear()) {
                move();
                updatePosition();
            } else {
                break;
            }
        }
    }

    private void moveToLocation(int targetStreet, int targetAvenue) {
        moveToStreet(targetStreet);
        moveToAvenue(targetAvenue);
    }

    private boolean anyBeepersInWorld() {
        synchronized (beeperLocations) {
            return !beeperLocations.isEmpty();
        }
    }

    private void updateBeeperLocations(int street, int avenue) {
        synchronized (beeperLocations) {
            int[] currentLocation = {street, avenue};
            beeperLocations.removeIf(location -> location[0] == street && location[1] == avenue && !nextToABeeper());
        }
    }

    public static void addBeeperLocation(int street, int avenue) {
        synchronized (beeperLocations) {
            int[] location = {street, avenue};
            beeperLocations.add(location);
        }
    }

    public int getMaxBeepers() {
        return this.maxBeepers;
    }

    public int[] getDeliveryPosition() {
        return this.deliveryPosition;
    }

    public void run() {
        race();
        System.out.println("I'm not gonna run away, I never go back on my word! That's my nindo: my ninja way! - Naruto Uzumaki");
    }

    // Obtener la ubicación actual
    public int[] getCurrentPosition() {
        return new int[]{currentStreet, currentAvenue};
    }

    // Actualizar la ubicación al moverse
    private void updatePosition() {
        if (facingNorth()) {
            currentStreet++;
        } else if (facingSouth()) {
            currentStreet--;
        } else if (facingEast()) {
            currentAvenue++;
        } else if (facingWest()) {
            currentAvenue--;
        }
    }

    // Mover a la derecha
    public void moveRight() {
        turnLeft();
        turnLeft();
        turnLeft();
        if (frontIsClear()) {
            move();
            updatePosition();
        }
    }

    // Mover a la izquierda
    public void moveLeft() {
        turnLeft();
        if (frontIsClear()) {
            move();
            updatePosition();
        }
    }

    // Mover y actualizar posición
    @Override
    public void move() {
        super.move();
        updatePosition();
    }
}

class RobotFactory implements Directions {
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

        // Crear robots
        List<Color> colores = new ArrayList<>();
        colores.add(Color.blue);
        colores.add(Color.red);
        colores.add(Color.green);
        colores.add(Color.yellow);

        int[] quantity_of_beepers = {1, 2, 4, 8};
        Racer[] racers = new Racer[r];
        int[] start_position = {3, 5, 6, 7};

        if (e) {
            // -e es un parámetro así que todos los robots tienen el mismo número de beepers
            System.out.println("All robots have the same number of beepers: ");
            int n = quantity_of_beepers[(int) (Math.random() * quantity_of_beepers.length)];
            for (int i = 0; i < r; i++) {
                racers[i] = new Racer(start_position[i], 2, East, 0, colores.get(i % colores.size()), n);
            }
        } else {
            // -e no es un parámetro así que todos los robots tienen diferentes números de beepers
            System.out.println("All robots have different number of beepers: ");
            for (int i = 0; i < r; i++) {
                int n = quantity_of_beepers[(int) (Math.random() * quantity_of_beepers.length)];
                racers[i] = new Racer(start_position[i], 2, East, 0, colores.get(i % colores.size()), n);
            }
        }

        // Imprimir todos los parámetros de los robots
        for (int i = 0; i < r; i++) {
            System.out.println("Robot " + i + " has " + racers[i].getMaxBeepers() + " beepers max.");
        }

        return racers;
    }

    public static void startRobots(Racer[] racers) {
        System.out.println('\n' + "Start the searching: ");
        // Iniciar los robots
        Thread[] threads = new Thread[racers.length];
        for (int i = 0; i < racers.length; i++) {
            final int index = i;
            threads[i] = new Thread(() -> racers[index].run());
            threads[i].start();
        }
    }
}

class BeepersFactory {
    public static void generateBeepersRandomly(String[] args) {
        int min_avenue = 3;
        int min_street = 1;
        int range_street = 8 - min_street + 1;
        int range_avenue = 10 - min_avenue + 1;
        int r = 1;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-r") && i + 1 < args.length) {
                try {
                    r = Integer.parseInt(args[i + 1]);
                    if (r != 1 && r != 2 && r != 4) {
                        System.out.println("El valor de -r debe ser 1, 2, o 4");
                        return;
                    }
                    i++; // Saltar al siguiente argumento
                } catch (NumberFormatException ex) {
                    System.out.println("El valor de -r debe ser un número");
                    return;
                }
            }
        }

        int num_beepers = r * 100; // Usar args para encontrar la cantidad de beepers
        for (int j = 0; j < num_beepers; j++) {
            int street = (int) (Math.random() * range_street + min_street);
            int avenue = (int) (Math.random() * range_avenue + min_avenue);
            World.placeBeepers(street, avenue, 1);
            Racer.addBeeperLocation(street, avenue);
        }
    }
}

public class MiPrimerRobot {
    public static void main(String[] args) {
        BeepersFactory.generateBeepersRandomly(args);
        World.readWorld("Mundo.kwld");
        World.setVisible(true);

        Racer[] racers = RobotFactory.createRobots(args);
        if (racers != null) {
            RobotFactory.startRobots(racers);
        }
    }
}
