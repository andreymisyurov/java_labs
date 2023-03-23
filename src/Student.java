import java.awt.*;

public abstract class Student implements IBehaviour {
    // координаты объектов студентов(базовый класс для студентов девочек и мальчиков)
    protected int m_x;
    protected int m_y;
    protected Color m_color;

    public Student(int in_x, int in_y, Color in_color) {
        m_x = in_x;
        m_y = in_y;
        m_color = in_color;
    }

    // задается логика движения оббъектов по экрану в классах-наследниках
    public abstract void move(int in_weigth, int in_hight);

    // в аргемент заходит объект класса Graphics используемый для отрисовки
    // в классах наследниках определяется этот метод для мальчиков и девочек по разному
    // разные фигуры(квадраты и круги), разные цвета и размеры
    public abstract void draw(Graphics in_gr);
    
}