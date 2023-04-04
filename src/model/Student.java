package model;

import java.awt.*;

public abstract class Student implements IBehaviour {
    protected int m_x;
    protected int m_y;
    protected Color m_color;

    public Student(int in_x, int in_y, Color in_color) {
        m_x = in_x;
        m_y = in_y;
        m_color = in_color;
    }

    public abstract void move(int in_weigth, int in_hight);
    public abstract void draw(Graphics in_gr);

}