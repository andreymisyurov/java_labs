import habitat.Habitat;
import front.UserInterface;

import javax.swing.*;

public class Program {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Habitat Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Habitat habitat = new Habitat(800, 600);
        UserInterface.createUI(habitat, frame);
    }
}
