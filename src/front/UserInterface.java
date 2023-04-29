package front;

import habitat.Habitat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {

    public static void createUI(Habitat habitat, JFrame frame) {
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

        JButton button1 = new JButton("start");
        JButton button2 = new JButton("stop");

        JRadioButton showTimeRadioButton = new JRadioButton("Show simulation time");
        JRadioButton hideTimeRadioButton = new JRadioButton("Hide simulation time");

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));


        JLabel label_every_time_male = new JLabel("born every sec for male:");
        JLabel label_every_time_female = new JLabel("born every sec for female:");

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

        JLabel label_time_female = new JLabel("lifetime for male:");
        JLabel label_time_male = new JLabel("lifetime female:");

        JSlider slider_femalelife_time = new JSlider(JSlider.HORIZONTAL, 1, 15, 10);
        slider_femalelife_time.setMajorTickSpacing(20);
        slider_femalelife_time.setMinorTickSpacing(5);
        slider_femalelife_time.setPaintTicks(true);
        slider_femalelife_time.setPaintLabels(true);

        slider_femalelife_time.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    habitat.setLifeTimeFemale(source.getValue() * 1000);
                }
                habitat.requestFocusInWindow();
            }
        });

        JSlider slider_malelife_time = new JSlider(JSlider.HORIZONTAL, 1, 15, 10);
        slider_malelife_time.setMajorTickSpacing(20);
        slider_malelife_time.setMinorTickSpacing(5);
        slider_malelife_time.setPaintTicks(true);
        slider_malelife_time.setPaintLabels(true);

        slider_malelife_time.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    habitat.setLifeTimeMale(source.getValue() * 1000);
                }
                habitat.requestFocusInWindow();
            }
        });
        buttonPanel.add(label_every_time_male);
        buttonPanel.add(slider);
        buttonPanel.add(label_every_time_female);
        buttonPanel.add(slider2);
        buttonPanel.add(label_time_female);
        buttonPanel.add(slider_femalelife_time);
        buttonPanel.add(label_time_male);
        buttonPanel.add(slider_malelife_time);
        frame.add(buttonPanel);

        JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(80, 0, 100, 10));
        spinner1.setMaximumSize(new Dimension(80, 20));
        JLabel spinner1Label = new JLabel("chance for female born:");

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(70, 0, 100, 10));
        spinner2.setMaximumSize(new Dimension(80, 20));
        JLabel spinner2Label = new JLabel("chance for male born:");

        spinner1.addChangeListener(e -> {
            habitat.setGenMaleChance((int) spinner1.getValue());
            habitat.requestFocusInWindow();
        });

        spinner2.addChangeListener(e -> {
            habitat.setGenFemaleChance((int) spinner2.getValue());
            habitat.requestFocusInWindow();
        });

        spinner1Label.setFocusable(false);
        spinner1.setFocusable(false);
        spinner2Label.setFocusable(false);
        spinner2.setFocusable(false);

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

        JButton currentObjectsButton = new JButton("Alive obj");
        currentObjectsButton.addActionListener(e -> {
            habitat.showCurrentObjects();
        });
        buttonPanel.add(currentObjectsButton);

        // добавил некоторые кнопки
        // для управления потоками и выбора приоритета

        JButton pauseMaleAIButton = new JButton("stop MaleAI");
        pauseMaleAIButton.addActionListener(e -> {
            habitat.getMaleAI().pauseMoving();
        });
        buttonPanel.add(pauseMaleAIButton);

        JButton resumeMaleAIButton = new JButton("run MaleAI");
        resumeMaleAIButton.addActionListener(e -> {
            habitat.getMaleAI().resumeMoving();
        });
        buttonPanel.add(resumeMaleAIButton);

        JButton pauseFemaleAIButton = new JButton("stop FemaleAI");
        pauseFemaleAIButton.addActionListener(e -> {
            habitat.getFemaleAI().pauseMoving();
        });
        buttonPanel.add(pauseFemaleAIButton);

        JButton resumeFemaleAIButton = new JButton("run FemaleAI");
        resumeFemaleAIButton.addActionListener(e -> {
            habitat.getFemaleAI().resumeMoving();
        });
        buttonPanel.add(resumeFemaleAIButton);

        Integer[] priorities = new Integer[Thread.MAX_PRIORITY - Thread.MIN_PRIORITY + 1];
        for (int i = Thread.MIN_PRIORITY; i <= Thread.MAX_PRIORITY; i++) {
            priorities[i - Thread.MIN_PRIORITY] = i;
        }

        JComboBox<Integer> malePriorityComboBox = new JComboBox<>(priorities);
        JComboBox<Integer> femalePriorityComboBox = new JComboBox<>(priorities);
        malePriorityComboBox.addActionListener(e -> {
            habitat.setPriorMale((int) malePriorityComboBox.getSelectedItem());
        });

        femalePriorityComboBox.addActionListener(e -> {
            habitat.setPriorFemale((int) femalePriorityComboBox.getSelectedItem());
        });
        malePriorityComboBox.setSelectedItem(5);
        femalePriorityComboBox.setSelectedItem(5);

        buttonPanel.add(new JLabel("Male Priority:"));
        buttonPanel.add(malePriorityComboBox);
        buttonPanel.add(new JLabel("Female Priority:"));
        buttonPanel.add(femalePriorityComboBox);


        frame.add(habitat);
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
    }
}
