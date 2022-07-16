package Assignment_5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main_Menu extends JFrame {

    JButton Search_B,Exit_B;
    JLabel Menu_L;
    JPanel main;

    public Main_Menu(){

        JPanel Menu_P = new JPanel()
        {@Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            ImageIcon img = new ImageIcon("background.jpg");
            g.drawImage(img.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }};


        setSize(1240, 700);
        setTitle("IMDb");
        add(Menu_P);
        Menu_P.setLayout(null);
        Menu_P.setBackground(new Color(153,153,153));

        main = new JPanel();
        main.setBackground(Color.white);
//        main.setLayout(BorderLayout);
        Menu_L = new JLabel("MAIN MENU");
        main.setBounds(270,60,270,70);
        Menu_L.setFont(new Font("Impact", Font.BOLD,50));
        Menu_L.setBounds(250,70,300,30);
        Menu_L.setForeground(Color.BLACK);
        main.add(Menu_L);
        Menu_P.add(main);

        Search_B = new JButton("SEARCH");
        Search_B.setBounds(850,160,300,80);
        Search_B.setForeground(Color.black);
        Search_B.setBackground(new Color(245,204,0));
        Search_B.setFont(new Font("Impact", Font.BOLD,35));
        Menu_P.add(Search_B);

        Exit_B = new JButton("EXIT");
        Exit_B.setBounds(850,560,100,25);
        Exit_B.setBackground(Color.BLUE);
        Exit_B.setForeground(Color.white);
        Exit_B.setFont(new Font("Serif", Font.BOLD,15));
        Menu_P.add(Exit_B);

        setVisible(true);

        MyListener listener = new MyListener();
        Search_B.addActionListener(listener);

        Exit_B.addActionListener(listener);
    }

    public class MyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("SEARCH")) {
                dispose();
                new Search();
            } else if (e.getActionCommand().equalsIgnoreCase("EXIT")) {
                dispose();
            }
        }
    }
}
