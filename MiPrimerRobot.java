import kareltherobot.*;
import java.awt.Color;

class Racer extends Robot {
    public Racer(int street, int avenue, Direction direction, int beepers) {
        super(street, avenue, direction, beepers);
        World.setupThread(this);
    }

    public void race() {
        while (!nextToABeeper()) {
            move();
            if (nextToABeeper()) {
                pickBeeper();
                turnOff();
            }
        }
    }

    public void run() {
        race();
    }
}

public class MiPrimerRobot implements Directions {

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);

        Racer first = new Racer(1, 10, North, 0);
        Racer second = new Racer(2, 2, East, 0);

        Thread thread1 = new Thread(() -> first.run());
        Thread thread2 = new Thread(() -> second.run());

        thread1.start();
        thread2.start();
    }
}
