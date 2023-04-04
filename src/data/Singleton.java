package data;

import model.Student;

import java.util.ArrayList;

// класс синглтон, для хранения даных(студентов)
public class Singleton {
    private static Singleton instance;
    private static ArrayList<Student> studList;

    private Singleton() {
        studList = new ArrayList<Student>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public static ArrayList<Student> getStudList() {
        return studList;
    }
}
