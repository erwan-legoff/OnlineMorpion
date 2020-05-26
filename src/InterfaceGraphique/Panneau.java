package InterfaceGraphique;

import Jeu.Morpion.Morpion;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Panneau extends JPanel {

    private String pionJ;
    private int posX;
    private int posY;
    private boolean nouvPion;
    private boolean nouvPionAdversaire;

    private static String[] tableauMoprion;

    public Panneau(){
        nouvPion=false;
        nouvPionAdversaire=false;
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

        g.drawString(tableauMoprion[0],100,500);
        g.drawString(tableauMoprion[1],300,500);
        g.drawString(tableauMoprion[2],500,500);
        g.drawString(tableauMoprion[3],100,300);
        g.drawString(tableauMoprion[4],300,300);
        g.drawString(tableauMoprion[5],500,300);
        g.drawString(tableauMoprion[6],100,100);
        g.drawString(tableauMoprion[7],300,100);
        g.drawString(tableauMoprion[8],500,100);


    }

    public void afficherPion(int id, String apparence){
        tableauMoprion[id-1] = apparence;
        pionJ=apparence;
        //System.out.println(id + apparence);
        System.out.println("Je suis dans afficher pion " + pionJ);
        if(id==7) {
            this.posX = 100;
            this.posY = 100;
        }
        if(id==8) {
            this.posX = 300;
            this.posY = 100;
        }
        if(id==9) {
            this.posX = 500;
            this.posY = 100;
        }
        if(id==4) {
            this.posX = 100;
            this.posY = 300;
        }
        if(id==5) {
            this.posX = 300;
            this.posY = 300;
        }
        if(id==6) {
            this.posX = 500;
            this.posY = 300;
        }
        if(id==1) {
            this.posX = 100;
            this.posY = 500;
        }
        if(id==2) {
            this.posX = 300;
            this.posY = 500;
        }
        if(id==3) {
            this.posX = 500;
            this.posY = 500;
        }
        this.nouvPion=true;

    }


}