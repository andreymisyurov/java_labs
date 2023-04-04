package front;

import habitat.Habitat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import

public class UserInterface {

    public static void createUI(Habitat habitat, JFrame frame) {
        // добавляем верхний menu-bar и прослушивание нажатия на элементы меню бара
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("menu");
        JMenuItem playMenuItem = new JMenuItem("Play");
        JMenuItem stopMenuItem = new JMenuItem("Stop");
        fileMenu.add(playMenuItem);
        fileMenu.add(stopMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        playMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.runButton();
            }
        });

        stopMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.stopButton();
            }
        });


        // создаем кнопки которые в правой части экрана
        JButton button1 = new JButton("start");
        JButton button2 = new JButton("stop");

        // создаем радиобаттон(переключатель)
        JRadioButton showTimeRadioButton = new JRadioButton("Show simulation time");
        JRadioButton hideTimeRadioButton = new JRadioButton("Hide simulation time");

        // костылек, чтобы ебрать повторное нажатие на элементы радио-баттона
        // создаем массив с одним единственным элементом, меняем этот элемент в обработчиках радио-баттона
        // почему массив я хз, обычная переменная не создавалась, думаю связано с тем что массив - это
        // ссылочный тип данных, а переменная - приметивный. ну и переменная передается по значения, а массив по ссылке
        // тем самым может меняться внутри функции
        final boolean[] check = {true};

        showTimeRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!check[0]) {
                    habitat.showButton();
                    check[0] = true;
                }
                habitat.requestFocusInWindow();
            }
        });

        hideTimeRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (check[0]) {
                    habitat.showButton();
                    check[0] = false;
                }
                habitat.requestFocusInWindow();
            }
        });

        showTimeRadioButton.setSelected(true);
        // запихиваем радио-баттоны в ButtonGroup, чтобы они работали как селектор,
        // при нажатии на один второй выключался
        ButtonGroup timeGroup = new ButtonGroup();
        timeGroup.add(showTimeRadioButton);
        timeGroup.add(hideTimeRadioButton);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                habitat.runButton();
                habitat.requestFocusInWindow();
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                habitat.stopButton();
                habitat.requestFocusInWindow();
            }
        });

        // создаем JPanel, это некий контейнер под всяческие виджеты кнопки и переключатели на UI
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    habitat.setGenMaleBorn(source.getValue());
                }
                habitat.requestFocusInWindow();
            }
        });


        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
        slider2.setMajorTickSpacing(20);
        slider2.setMinorTickSpacing(5);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);

        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    habitat.setGenFemaleBorn(source.getValue());
                }
                habitat.requestFocusInWindow();
            }
        });

        // добавляем на JPanel два только что созданных слайдера и добавляем его на основной frame
        // спиннеры это ползунки для выставления раз во сколько секунд рождается студент
        buttonPanel.add(slider);
        buttonPanel.add(slider2);
        frame.add(buttonPanel);

        // слайдеры это два поле ввода под цифры которые отвечают за шанс рождения клетки
        JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(80, 0, 100, 10));
        spinner1.setMaximumSize(new Dimension(80, 20));
        JLabel spinner1Label = new JLabel("chance for female born:");

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(70, 0, 100, 10));
        spinner2.setMaximumSize(new Dimension(80, 20));
        JLabel spinner2Label = new JLabel("chance for male born:");

        // по заданию стоит обработка исключений. здесь важно пояснить что исключеия вы обработали тем самым,
        // что ограничели ввод некорректных значений на этапе UI. то есть если попробовать ввести букву в поле то она
        // не отправится. разрешен ввод только цифры от 0 до 100
        // аналогично по слайдерами. нет возможности передать в программу данные кроме данных от 1 до 10

        spinner1.addChangeListener(e -> {
            habitat.setGenMaleChance((int) spinner1.getValue());
            habitat.requestFocusInWindow();
        });

        spinner2.addChangeListener(e -> {
            habitat.setGenFemaleChance((int) spinner2.getValue());
            habitat.requestFocusInWindow();
        });


        // setFocusable функция расставляющая "акцент" приоритет некоторой части UI
        // чтобы работали клавиши с клавиатуры.
        spinner1Label.setFocusable(false);
        spinner1.setFocusable(false);
        spinner2Label.setFocusable(false);
        spinner2.setFocusable(false);



        // создаем галочку, разрешающую показ модального окна при нажатии на клавишу Stop
        JCheckBox checkBox = new JCheckBox("Modal info window");
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox.isSelected()) {
                    habitat.setModal(true);
                } else {
                    habitat.setModal(false);
                }
                habitat.requestFocusInWindow();
            }
        });
        checkBox.setSelected(true);


        // конструироуем JPanel это вся правая часть нашего интерфейса с кнопками.

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(spinner1Label);

        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(spinner1);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(spinner2Label);

        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(spinner2);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(button1);
        button1.setFocusable(false);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(button2);
        button2.setFocusable(false);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(showTimeRadioButton);
        showTimeRadioButton.setFocusable(false);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(hideTimeRadioButton);
        hideTimeRadioButton.setFocusable(false);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(checkBox);
        checkBox.setFocusable(false);

        buttonPanel.add(Box.createVerticalGlue());

        // добавляем к основному фрейму нашу среду habitat и правую JPanel
        frame.add(habitat);
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
    }
}
