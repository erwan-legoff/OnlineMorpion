package InterfaceGraphique;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Interface extends JFrame {

    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Interface frame = new Interface();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Interface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 608, 608);
        this.setTitle("Morpion");
        this.setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Panneau panneau = new Panneau();
        this.setContentPane(panneau);



        final int[] id = new int[1];
        int largeur = this.getWidth();
        int hauteur = this.getHeight();

        panneau.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("X = " + e.getX() + " ; Y = " + e.getY());//temporaire

                if (e.getY() < hauteur / 3) {
                    if (e.getX() < largeur / 3)
                        id[0] = 7;
                    if (e.getX() > largeur / 3 && e.getX() < largeur * 2 / 3)
                        id[0] = 8;
                    if (e.getX() > largeur * 2 / 3)
                        id[0] = 9;
                }
                if (e.getY() > hauteur / 3 && e.getX() < hauteur * 2 / 3) {
                    if (e.getX() < largeur / 3)
                        id[0] = 4;
                    if (e.getX() > largeur / 3 && e.getX() < largeur * 2 / 3)
                        id[0] = 5;
                    if (e.getX() > largeur * 2 / 3)
                        id[0] = 6;
                }
                if (e.getY() > hauteur * 2 / 3) {
                    if (e.getX() < largeur / 3)
                        id[0] = 1;
                    if (e.getX() > largeur / 3 && e.getX() < largeur * 2 / 3)
                        id[0] = 2;
                    if (e.getX() > largeur * 2 / 3)
                        id[0] = 3;
                }
                System.out.println(id[0]);

                panneau.afficherPion(id[0]);
            }

        });
    }

}
