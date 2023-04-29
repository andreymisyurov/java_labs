package BaseAI;

import model.Student;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

// класс BaseAI описывает движение во втором потоке. класс абстрактный, поэтому от него
// ответвляются два класса, где раскрываются движения для мальчиков и для девочек
public abstract class BaseAI {
    // создается поток, в котором происходят действия
    private Thread m_movement_thread;
    // семафор передается по ссылке в конструкторе, этот симофор один на всех,
    // везде передается по ссылкам и контролирует доступ к общим данным
    protected Semaphore m_semaphore;
    // список студентов, берем из синглтона. он один на всю программу
    protected static LinkedList<Student> m_stud_list;
    // приоритет для потока
    protected int m_prior;

    public BaseAI(int in_delay, LinkedList<Student> in_database, Semaphore in_semaphore, int in_prior) {
        m_prior = in_prior;
        m_semaphore = in_semaphore;
        m_stud_list = in_database;
        m_movement_thread = new Thread(() -> {
            while (true) {
                // вот этот метод определяет разное движение для студенток и студентов и он раскрыт в каждом отдельном классе
                move();
                try {
                    Thread.sleep(in_delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public abstract void move();
    public abstract void pauseMoving();
    public abstract void resumeMoving();
    public void startMoving() {
        m_movement_thread.setPriority(m_prior);
        m_movement_thread.start();
    }

    public void changePrior(int in_value) {
        m_prior = in_value;
        m_movement_thread.setPriority(m_prior);
    }
}
