package habitat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import java.util.ArrayList;
import java.util.Random;

import model.Student;
import model.MaleStudent;
import model.FemaleStudent;
import data.Singleton;

public class Habitat extends JPanel {
    // переменные генерации стали не константными, их можно изменять из UI ползунками и окошками ввода
    private int GEN_FEMALE_SEC = 2;
    private int GEN_MALE_SEC = 3;
    private int GEN_MALE_CHANCE = 70;
    private int GEN_FEMALE_CHANCE = 85;

    private final ArrayList<Student> m_students;

    private int m_time;
    private boolean m_run;
    private boolean m_show_time;
    private boolean m_show_stats;
    private boolean m_pause = false;
    private boolean m_check_modal = true;

    private int m_mcount;
    private int m_fcount;


    // добавлены сеттеры для данных, чтобы можно было управлять данными из UI
    // то есть когда мы на UI двигаем ползунок, или ставим галочку, происходят изменения как раз таки этих данных
    public void setGenFemaleBorn(int in_value) {
        GEN_FEMALE_SEC = in_value;
    }

    public void setGenMaleBorn(int in_value) {
        GEN_MALE_SEC = in_value;
    }

    public void setModal(boolean in_value) {
        m_check_modal = in_value;
    }

    public void setGenMaleChance(int in_value) {
        GEN_MALE_CHANCE = in_value;
    }

    public void setGenFemaleChance(int in_value) {
        GEN_FEMALE_CHANCE = in_value;
    }

    public Habitat(int in_width, int in_height) {
        setPreferredSize(new Dimension(in_width, in_height));
        setBackground(Color.WHITE);

        // по заданию нужно было вынести лист с данными в отдельный класс, который представлен в виде паттерна Синглтон
        // вот это преподаватель может спросить. Синглтон это такой класс, объект которого создается толкьо один раз.
        // чтото вроде глобальной переменной. чтобы обратиться к этому элементу класса мы вызываем метод getInstance()
        // это возвращает экземпляр класса который ЕДИНСТВЕННЫЙ в программе. сделано для того чтобы нельзя было создать
        // две базы данных(два листа) со студентами, и отовсюду был доступ к базе данных
        // функцией getStudList мы получаем лист студентов. так как это ссылочный тип данных(не приметивный), который
        // передается по ссылке. мы приравниваем значению m_student ссылку на лист нашего синглтона
        m_students = Singleton.getInstance().getStudList();
        m_time = 0;
        m_run = false;
        m_show_time = true;

        Timer timer = new Timer(300, e -> {
            Random rand = new Random();
            //
            if (m_run && !m_pause) {
                ++m_time;
                if (m_time % GEN_FEMALE_SEC == 0 && getChance(GEN_MALE_CHANCE)) {
                    m_students.add(new FemaleStudent(rand.nextInt(in_width), rand.nextInt(in_height)));
                }

                if (m_time % GEN_MALE_SEC == 0 && getChance(GEN_FEMALE_CHANCE)) {
                    m_students.add(new MaleStudent(rand.nextInt(in_width), rand.nextInt(in_height)));
                }

                for (Student st : m_students) {
                    st.move(in_width, in_height);
                }
                repaint();
            }
        });
        timer.start();


        // обработку клавиш нажатия не трогал спрошлого проекта
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "b");
        getActionMap().put("b", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                runButton();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e");
        getActionMap().put("e", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                stopButton();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "t");
        getActionMap().put("t", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                showButton();
            }
        });
    }


    // основная функция изменена, логига отрисовки статистики теперь здесь ненужна. поэтому тут остается только
    // отрисовка каждого студента и отрисовка таймера. вывод статистики переходит в модальное окно что вынесено в
    // функцию stopButton(ниже)
    @Override
    protected void paintComponent(Graphics in_gr) {
        super.paintComponent(in_gr);

        if (!m_show_stats) {
            for (Student st : m_students) {
                st.draw(in_gr);
            }

            if (m_show_time) {
                in_gr.setColor(Color.BLACK);
                in_gr.setFont(new Font("Georgia", Font.BOLD, 16));
                in_gr.drawString("Time: " + m_time, 10, getHeight() - 20);
            }
        }
    }


    // функция стоп баттон, выводит модальное(модальным называется окно которое перекрывает основное),
    // и чтобы продолжить процесс нужно сделать какое то действие в модальном окне.
    // или если не стоит галочка вывода модального окна просто молча закрывает процесс и обнуляет счетчики
    public void stopButton() {
        for (Student st : m_students) {
            if (st instanceof MaleStudent) {
                ++m_mcount;
            } else if (st instanceof FemaleStudent) {
                ++m_fcount;
            }
        }

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setText("Male Students: " + m_mcount + "\nFemale Students: " + m_fcount + "\nSimulation Time: " + m_time);
        m_mcount = 0;
        m_fcount = 0;

        if (m_check_modal) {
            // открытие модального окна если стоит m_check_modal это та самая галочка в UI интерфейсе
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            Object[] options = {"OK", "Cancel"};

            m_pause = true;
            int result = JOptionPane.showOptionDialog(this, scrollPane, "Simulation Results",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            // обработка нажатий в модальном окне. при нажатии на OK заходим в if и обнуляем счетчики,
            // останавливаем процесс
            if (result == JOptionPane.YES_OPTION) {
                m_run = false;
                m_time = 0;
                m_show_stats = true;
                m_students.clear();
                repaint();
            }
            m_pause = false;
        } else {
            m_run = false;
            m_time = 0;
            m_show_stats = true;
            m_students.clear();
            repaint();
        }
    }

    // остальные коммандные кнопки также вынесены в маленькие функции чтобы можно было удобно управлять этим
    // из UI
    public void runButton() {
        m_show_stats = false;
        m_run = true;
    }

    public void showButton() {
        m_show_time = !m_show_time;
    }


    public boolean getChance(int in_proc) {
        Random random = new Random();
        return random.nextInt(100) + 1 <= in_proc;
    }

}
