package InterfaceGraphique;

import javax.swing.*;
import java.awt.*;

public class Panneau extends JPanel {

    private int idJ;
    private int posX;
    private int posY;
    private boolean nouvPion;
    private boolean nouvPionAdversaire;

    Panneau(){
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
        if(nouvPion){
            nouvPion=false;
            g.drawString("X",posX,posY);
        }
        if(nouvPionAdversaire){
            nouvPionAdversaire=false;
            g.drawString("O",posX,posY);
        }

    }

    public void afficherPion(int id){
        this.idJ=id;
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

    public void afficherPionAdversaire(int id){
        this.idJ=id;
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

}