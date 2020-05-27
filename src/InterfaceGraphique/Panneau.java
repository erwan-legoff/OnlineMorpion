package InterfaceGraphique;

import Jeu.Morpion.Morpion;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Panneau extends JPanel {

    private static String[] tableauMoprion;

    public Panneau(){
        tableauMoprion = new String[9];
        Arrays.fill(tableauMoprion," ");
    }

    public void paintComponent(Graphics g){
        int id=0;
        g.clearRect(0,0,this.getWidth(),this.getHeight());
        for(int j=2;j>=0;j--) {
            for (int i=0; i<=2;i++) {
                id++;
                g.drawRect(i * this.getWidth() / 3, j * this.getHeight() / 3, this.getWidth()/3, this.getHeight()/3);
                g.drawString(String.valueOf(id), 10 + i * this.getWidth() / 3, 20 + j * this.getHeight() / 3);
            }
        }
        Font font = new Font("Courier", Font.BOLD, 128);
        g.setFont(font);
        g.drawString(tableauMoprion[0],50,525);
        g.drawString(tableauMoprion[1],250,525);
        g.drawString(tableauMoprion[2],450,525);
        g.drawString(tableauMoprion[3],50,325);
        g.drawString(tableauMoprion[4],250,325);
        g.drawString(tableauMoprion[5],450,325);
        g.drawString(tableauMoprion[6],50,125);
        g.drawString(tableauMoprion[7],250,125);
        g.drawString(tableauMoprion[8],450,125);
    }

    public void afficherPion(int id, String apparence){
        tableauMoprion[id-1] = apparence;
    }
}