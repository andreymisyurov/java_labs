import javax.swing.JFrame;

public class Program {
    public static void main(String[] args) {
        // создаем окно при помощи JFrame из библиотеки swing
        JFrame frame = new JFrame("Habitat Simulation");

        // устанавливаем опцию чтобы при закрытии окна процесс завершался
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // создаем объект Hibitan(созданный нами класс, он наследует JFrame), передаем в аргементах размеры окна
        Habitat habitat = new Habitat(800, 600);

        // добавляем объект хибитан(вся наша среда симуляции) в наше созданное окно JFrame
        frame.add(habitat);

        // Подгоняет размер окна JFrame в соответствии с размерами его содержимого.
        frame.pack();

        //Делает окно JFrame видимым на экране.
        frame.setVisible(true);

    }
}
