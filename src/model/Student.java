package model;

import java.awt.*;

public abstract class Student {
    protected int m_x;
    protected int m_y;
    protected Color m_color;
    // добавляем две переменные контроля времени жизни и переменная идентификатора
    protected long m_birthTime;
    protected long m_lifeTime;
    protected int m_id;

    public Student(int in_x, int in_y, Color in_color, long birthTime, long lifeTime, int in_id) {
        m_x = in_x;
        m_y = in_y;
        m_color = in_color;
        m_birthTime = birthTime;
        m_lifeTime = lifeTime;
        m_id = in_id;
    }

    public void setX(int in_value) {
        m_x = in_value;
    }

    public int getX() {
        return m_x;
    }

    public void setY(int in_value) {
        m_y = in_value;
    }

    public int getY() {
        return m_y;
    }

//    public abstract void move(int in_weigth, int in_hight);
    public abstract void draw(Graphics in_gr);

    public int getId() {
        return m_id;
    }


    // добавлена функция, проверяющая живой ли объект? нужно ли его удалять?
    public boolean checkIfExpired() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - m_birthTime >= m_lifeTime) {
            return true;
        }
        return false;
    }

}