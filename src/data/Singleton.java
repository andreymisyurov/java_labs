package data;

import model.Student;

import java.util.LinkedList;

public class Singleton {
    private static Singleton instance;
    private static LinkedList<Student> studList;

    private Singleton() {
        studList = new LinkedList<Student>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public static LinkedList<Student> getStudList() {
        return studList;
    }
}
