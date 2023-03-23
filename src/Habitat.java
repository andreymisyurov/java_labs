
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import java.util.Random;

public class Habitat extends JPanel {
    // константы генерации
    private final int GEN_FEMALE_SEC = 2;
    private final int GEN_MALE_SEC = 3;
    private final int GEN_MALE_CHANCE = 70;
    private final int GEN_FEMALE_CHANCE = 85;

    // массив со студентами
    private ArrayList<Student> m_students;

    // переменные управления
    private int m_time;
    private boolean m_run;
    private boolean m_show_time;
    private boolean m_show_stats;

    // счетчики для мальчиков и девочек
    private int m_mcount;
    private int m_fcount;


    public Habitat(int in_width, int in_height) {
        // устанавливает размер панели
        setPreferredSize(new Dimension(in_width, in_height));

        // устанавливает белый фон панели
        setBackground(Color.WHITE);

        // создает пустой список объектов Student.
        m_students = new ArrayList<>();

        // устанавливаем начальное время симуляции равным 0
        m_time = 0;

        // устанавливает флаг запуска симуляции в значение false. Поэтому запускать симуляцию необходим нажатием на B
        // нажатие на B меняет флаг на тру
        m_run = false;

        // по дефолту на экране показывается время. при нажатии на T значение меняется на фолс и время исчезает с экрана
        m_show_time = true;

        // создае объект таймера. то что происходит внутри лямбда-выражения повторяется каждые 300мс
        Timer timer = new Timer(300, e -> {
            Random rand = new Random();
            // если флаг ран установлен как тру, то идет появление и добавление студентов в список
            // в соответствии с шансом который выдает функция getChance()
            if (m_run) {
                ++m_time;
                if (m_time % GEN_FEMALE_SEC == 0 && getChance(GEN_MALE_CHANCE)) {
                    m_students.add(new FemaleStudent(rand.nextInt(in_width), rand.nextInt(in_height)));
                }

                if (m_time % GEN_MALE_SEC == 0 && getChance(GEN_FEMALE_CHANCE)) {
                    m_students.add(new MaleStudent(rand.nextInt(in_width), rand.nextInt(in_height)));
                }

                // меняем положение объектов в листе из всех студентов. положение меняется в зависимости от метода
                // интерфейса определенного по разному в классе студенток и студентов
                for (Student st : m_students) {
                    st.move(in_width, in_height);
                }
                // обновляем экран. при вызове данной функции, запускается функция paintComponent определенная ниже
                repaint();
            }
        });

        // запускаем объект таймера
        // таймер так устроен что запускается в отдельном потоке
        timer.start();


        // В этой строке мы связываем нажатие клавиши на клавиатуре с действием.
        // Когда нажимается клавиша "B", система будет искать соответствующее действие "b" в "action map"(следующая функция)
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "b");

        // здесь же мы задаем определенное действие при нажатии на клавижу "b"
        // при создании объекта AbstractAction мы определяем реализацию метода actionPerformed,
        // который будет вызываться при возникновении события(в нашем случае при нажатии "b").
        getActionMap().put("b", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                m_show_stats = false;
                m_run = true;
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e");
        getActionMap().put("e", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                m_run = false;
                m_mcount = 0;
                m_fcount = 0;

                for (Student st : m_students) {
                    if (st instanceof MaleStudent) {
                        ++m_mcount;
                    } else if (st instanceof FemaleStudent) {
                        ++m_fcount;
                    }
                }

                m_show_stats = true;
                m_students.clear();
                repaint();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "t");
        getActionMap().put("t", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                m_show_time = !m_show_time;
            }
        });
    }

    // основная функция отвечающая за отрисовку и вывод информации на экран.
    // передается объект класса Graphics. Эласс в Java, который используется для рисования пользовательского интерфейса
    // метод paintComponent вызывается автоматически при вызове метода repaint()
    // В нем происходит перерисовка компонента JPanel с учетом текущего состояния, которое хранится в полях класса Habitat.
    // После перерисовки компонента, на экране будет отображаться обновленное состояние компонента с учетом новых значений полей.
    @Override
    protected void paintComponent(Graphics in_gr) {

        // вызов родительского метода через super
        // отвечает за отрисовку основных компонентов Swing, таких как рамки и кнопки
        super.paintComponent(in_gr);

        // проверяет приватный флаг m_show_stats, и если он установлен как тру(значит была нажата кноака E)
        // то отображает статистику, если нет то переходит в блок else и отрисовывает перемещение студентов
        if (m_show_stats) {
            // установка шрифта для текста статистики
            in_gr.setFont(new Font("Arial", Font.BOLD, 16));

            // установка цвета для текста статистики о мужских студентах
            in_gr.setColor(Color.BLUE);
            // вывод текста статистики о студентах мужского пола
            in_gr.drawString("Male Students: " + m_mcount, 10, getHeight() / 2 - 10);

            // установка цвета и вывод статистики о студентках
            in_gr.setColor(Color.MAGENTA);
            in_gr.drawString("Female Students: " + m_fcount, 10, getHeight() / 2 + 10);

            // установка цвета и вывод информации о времени
            in_gr.setColor(Color.BLACK);
            in_gr.drawString("Simulation Time: " + m_time, 10, getHeight() / 2 + 30);
            m_time = 0;
        } else {
            // отрисовка каждого студента
            for (Student st : m_students) {
                st.draw(in_gr);
            }

            // если флаг m_show_time установлен как тру(меняется нажатием кнопки T)
            // то отображает времян а экране
            if (m_show_time) {
                in_gr.setColor(Color.BLACK);
                in_gr.setFont(new Font("Arial", Font.BOLD, 16));
                in_gr.drawString("Time: " + m_time, 10, getHeight() - 20);
            }
        }
    }

    // функция для рандома появления объектов. Принимает число количество процентов
    // Возвращает или тру или фолс в соответствии с этой вероятностью
    public boolean getChance(int in_proc) {
        Random random = new Random();
        return random.nextInt(100) + 1 <= in_proc;
    }

}
