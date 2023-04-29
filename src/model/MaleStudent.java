package model;

import java.awt.*;

public class MaleStudent extends Student {
    private int SPEED = 9;
    private int SIZE = 12;
    private int m_dx = 10;
    private int m_dy = 10;

    public MaleStudent(int in_x, int in_y, long birthTime, long lifeTime, int in_id) {
        super(in_x, in_y, Color.BLUE, birthTime, lifeTime, in_id);
    }

    public void setSize(int in_value) {
        SIZE = in_value;
    }

    public int getSize() {
        return SIZE;
    }

    public void setDX(int in_value) {
        m_dx = in_value;
    }

    public void setDY(int in_value) {
        m_dy = in_value;
    }

    public int getDX() {
        return m_dx;
    }


    public int getDY() {
        return m_dy;
    }

    @Override
    public void draw(Graphics in_gr) {
        in_gr.setColor(m_color);
        in_gr.fillRect(m_x, m_y, SIZE, SIZE);
    }

}