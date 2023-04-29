package BaseAI;

import model.MaleStudent;
import model.Student;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class MaleAI extends BaseAI {
    private int moveCounter;
    private int moveInterval;
    // количество шагов для смены направления
    private int N = 7;
    // скорость
    private int V = 1;
    // lock и paused нужны нам для блокировки потоков, чтобы ими можно было управлять кнопками с интерфейса
    private final Object lock = new Object();
    private boolean paused = false;

    public MaleAI(int in_delay, LinkedList<Student> in_database, Semaphore in_semaphore, int in_prior) {
        super(in_delay, in_database, in_semaphore, in_prior);
        moveCounter = 0;
        moveInterval = (N * 1000) / in_delay;
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
            Random random = new Random();
            for (Student student : m_stud_list) {
                if (student instanceof MaleStudent) {
                    // логика движения для студента со сменой направления и отзвона от стенок
                    MaleStudent maleStudent = (MaleStudent) student;

                    if (moveCounter >= moveInterval) {
                        maleStudent.setDX(random.nextInt(V * 2) - V);
                        maleStudent.setDY(random.nextInt(V * 2) - V);
                        moveCounter = 0;
                    }

                    int newX = maleStudent.getX() + maleStudent.getDX();
                    int newY = maleStudent.getY() + maleStudent.getDY();

                    if (newX < 0 || newX >= 800 - maleStudent.getSize()) {
                        maleStudent.setDX(-maleStudent.getDX());
                        newX = maleStudent.getX() + maleStudent.getDX();
                    }

                    if (newY < 0 || newY >= 600 - maleStudent.getSize()) {
                        maleStudent.setDY(-maleStudent.getDY());
                        newY = maleStudent.getY() + maleStudent.getDY();
                    }

                    maleStudent.setX(newX);
                    maleStudent.setY(newY);
                }
            }
            moveCounter++;
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
