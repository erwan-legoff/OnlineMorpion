package InterfaceGraphique;

import Jeu.Morpion.Morpion;

import javax.swing.*;
import java.awt.*;

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
    }

    public void paintComponent(Graphics g){
        int id=0;
        for(int j=2;j>=0;j--) {
            for (int i=0; i<=2;i++) {
                id++;
                g.drawRect(i * this.getWidth() / 3, j * this.getHeight() / 3, this.getWidth()/3, this.getHeight()/3);
                g.drawString(String.valueOf(id), 10 + i * this.getWidth() / 3, 20 + j * this.getHeight() / 3);
            }
        }
        //if(nouvPion){
            //nouvPion=false;
            //g.drawString(pionJ,posX,posY);
        //System.out.println(pionJ);
        //}
    }

    public void afficherPion(int id, String apparence){
        pionJ=apparence;
        //System.out.println(id + apparence);
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
        this.repaint();
    }

    public void actualiserGrille(Morpion morpion){
        tableauMoprion = morpion.getGrilleMorpion();
/*
        for (int i = 0; i < morpion.getGrilleMorpion().length; i++) {
            tableauMoprion[i]=morpion.getCaseGrilleMorpion(i);
        }
*/
        for(int i=0;i<tableauMoprion.length;i++){
            this.afficherPion(i+1,"X");
            //System.out.println(tableauMoprion[i]);
        }
    }
/*
    public void afficherPionAdversaire(int id){
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
        this.nouvPionAdversaire=true;
        this.repaint();
    }

 */

}