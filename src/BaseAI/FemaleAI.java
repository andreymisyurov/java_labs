package BaseAI;

import model.FemaleStudent;
import model.Student;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class FemaleAI extends BaseAI {
    private double radius = 5;
    private double angleStep;
    private int V = 2;

    // переменные для управления потоками
    private final Object lock = new Object();
    private boolean paused = false;

    public FemaleAI(int in_delay, LinkedList<Student> in_database, Semaphore in_semaphore, int in_prior) {
        super(in_delay, in_database, in_semaphore, in_prior);
        angleStep = V / radius;
    }

    @Override
    public void move() {

        synchronized (lock) {
            while (paused) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            m_semaphore.acquire();
            for (Student student : m_stud_list) {
                if (student instanceof FemaleStudent) {
                    // логика движения для студенток по кругу
                    FemaleStudent femaleStudent = (FemaleStudent) student;
                    double currentAngle = femaleStudent.getAngle();
                    double newAngle = currentAngle + angleStep;
                    femaleStudent.setAngle(newAngle);

                    double newX = femaleStudent.getX() + radius * Math.cos(newAngle);
                    double newY = femaleStudent.getY() + radius * Math.sin(newAngle);

                    femaleStudent.setX((int) newX);
                    femaleStudent.setY((int) newY);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            m_semaphore.release();
        }
    }

    @Override
    public void pauseMoving() {
        synchronized (lock) {
            paused = true;
        }
    }

    @Override
    public void resumeMoving() {
        synchronized (lock) {
            paused = false;
            lock.notify();
        }
    }
}






