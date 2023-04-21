package model;

import java.awt.*;

public class FemaleStudent extends Student {
    private int SPEED = 4;
    private int SIZE = 11;

    public FemaleStudent(int in_x, int in_y, long birthTime, long lifeTime, int in_id) {
        super(in_x, in_y, Color.PINK, birthTime, lifeTime, in_id);
    }

    @Override
    public void draw(Graphics in_gr) {
        in_gr.setColor(m_color);
        in_gr.fillOval(m_x, m_y, SIZE, SIZE);
    }

    @Override
    public void move(int in_width, int in_height) {
        int dx = (int) (Math.random() * SPEED * 2 - SPEED);
        int dy = (int) (Math.random() * SPEED * 2 - SPEED);
        if (m_x + dx < 0 || m_x + dx > in_width - SIZE) {
            dx = -dx;
        }

        if (m_y + dy < 0 || m_y + dy > in_height - SIZE) {
            dy = -dy;
        }
        m_x += dx;
        m_y += dy;
    }

}