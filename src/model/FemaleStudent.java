package model;

import java.awt.*;

public class FemaleStudent extends Student {
    private int SPEED = 4;
    private int SIZE = 11;
    private double m_angle = 0.0;

    public FemaleStudent(int in_x, int in_y, long birthTime, long lifeTime, int in_id) {
        super(in_x, in_y, Color.PINK, birthTime, lifeTime, in_id);
    }

    public void setAngle(double in_value) {
        m_angle = in_value;
    }

    public double getAngle() {
        return m_angle;
    }

    @Override
    public void draw(Graphics in_gr) {
        in_gr.setColor(m_color);
        in_gr.fillOval(m_x, m_y, SIZE, SIZE);
    }
}
