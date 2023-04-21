package habitat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.util.*;
import model.Student;
import model.MaleStudent;
import model.FemaleStudent;
import data.Singleton;

public class Habitat extends JPanel {
    private int GEN_FEMALE_SEC = 2;
    private int GEN_MALE_SEC = 3;
    private int GEN_MALE_CHANCE = 70;
    private int GEN_FEMALE_CHANCE = 85;
    private int m_time;
    private boolean m_run;
    private boolean m_show_time;
    private boolean m_show_stats;
    private boolean m_pause = false;
    private boolean m_check_modal = true;
    private int m_mcount;
    private int m_fcount;
    private int lifetime_male;
    private int lifetime_female;
    private Long m_start_time;

    private final LinkedList<Student> m_students;
    private HashSet<Integer> m_unique_ids;
    private TreeMap<Integer, Long> m_birthday_time;

    public void setGenFemaleBorn(int in_value) {
        GEN_FEMALE_SEC = in_value;
    }
    public void setLifeTimeMale(int in_value) {
        lifetime_male = in_value;
    }
    public void setLifeTimeFemale(int in_value) {
        lifetime_female = in_value;
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
        m_students = Singleton.getInstance().getStudList();
        m_time = 0;
        m_run = false;
        m_show_time = true;
        // добавлены некоторые переменный, отвечающие за время жизни студента. изначально время жизни равно 10 сек, берутся данные с ползунков
        lifetime_male = 10000;
        lifetime_female = 10000;
        // две коллекции. в сете хранятся уникальные идентификаторы, в мапе ключ - значение (id - время жизни)
        m_unique_ids = new HashSet<>();
        m_birthday_time = new TreeMap<>();
        m_start_time = 0L;

        Timer timer = new Timer(300, e -> {
            Random rand = new Random();
            Long currentTime = System.currentTimeMillis();
            if (m_run && !m_pause) {
                ++m_time;
                if (m_time % GEN_FEMALE_SEC == 0 && getChance(GEN_MALE_CHANCE)) {
                    // ранздомайзер(описал ниже)
                    int random_id;
                    do {
                        random_id = (int) (Math.random() * Integer.MAX_VALUE);
                    } while (m_unique_ids.contains(random_id));

                    m_unique_ids.add(random_id);
                    m_birthday_time.put(random_id, currentTime);
                    m_students.add(new FemaleStudent(rand.nextInt(in_width), rand.nextInt(in_height), currentTime, lifetime_female, random_id));
                }

                if (m_time % GEN_MALE_SEC == 0 && getChance(GEN_FEMALE_CHANCE)) {
                    // следующие 4 строки - рандомайзер. генерируем случайное число для id, проверяем что оно уникальное и присваиваем студенту
                    int random_id;
                    do {
                        random_id = (int) (Math.random() * Integer.MAX_VALUE);
                    } while (m_unique_ids.contains(random_id));
                    m_unique_ids.add(random_id);
                    m_birthday_time.put(random_id, currentTime);
                    m_students.add(new MaleStudent(rand.nextInt(in_width), rand.nextInt(in_height), currentTime, lifetime_male, random_id));
                }
                // добавлен временный лист, в него в цикле аносятся студенты которых следует удалить по времени жизни
                LinkedList<Student> temp_list = new LinkedList<>();
                for (Student st : m_students) {
                    if(st.checkIfExpired()) {
                        temp_list.add(st);
                        m_unique_ids.remove(st.getId());
                        m_birthday_time.remove(st.getId());
                    }
                    st.move(in_width, in_height);
                }
                // здесь происходит удаление из основного листа студентов(синглтона)
                m_students.removeAll(temp_list);

                repaint();
            }
        });
        timer.start();


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
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            Object[] options = {"OK", "Cancel"};

            m_pause = true;
            int result = JOptionPane.showOptionDialog(this, scrollPane, "Simulation Results",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (result == JOptionPane.YES_OPTION) {
                m_run = false;
                m_time = 0;
                m_show_stats = true;
                m_birthday_time.clear();
                // добавлена очистка двух новых структур данных. дерева и хеш мап
                m_unique_ids.clear();
                m_students.clear();
                m_start_time = 0L;
                repaint();
            }
            m_pause = false;
        } else {
            m_run = false;
            m_time = 0;
            m_show_stats = true;
            m_students.clear();
            // добавлена очистка двух новых структур данных. дерева и хеш мап
            m_birthday_time.clear();
            m_unique_ids.clear();
            m_start_time = 0L;
            repaint();
        }
    }

    public void runButton() {
        // назначаем значение новой переменной. точка отсчета в момент старта
        if(m_start_time == 0L) {
            m_start_time = System.currentTimeMillis();
        }
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

    // добавлена функция для показа живых объектов(их ID и время рождения)
    // вызывается модальное окно, выводится ID и время рождения
    public void showCurrentObjects() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        for (Map.Entry<Integer, Long> entry : m_birthday_time.entrySet()) {
            textArea.append("ID: " + entry.getKey() + " - Birth time: " + ((entry.getValue() - m_start_time)/300) + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Current Objects", JOptionPane.INFORMATION_MESSAGE);
    }
}
