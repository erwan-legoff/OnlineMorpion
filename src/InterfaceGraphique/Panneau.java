package InterfaceGraphique;

import javax.swing.*;
import java.awt.*;

public class Panneau extends JPanel {

    Panneau(){

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
    }


}