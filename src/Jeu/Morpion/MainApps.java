package Jeu.Morpion;

import InterfaceGraphique.Interface;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;


public class MainApps {
    public static void main(String[] args) {




        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>();
        Joueur JoueurClient = new Joueur("Client","pouet","X");
        Joueur JoueurServeur = new Joueur("Serveur","pouette","O");


            listeJoueurs.add(JoueurClient);
            listeJoueurs.add(JoueurServeur);
            Morpion morpion = new Morpion(listeJoueurs);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Interface frame = new Interface(morpion);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
            System.out.println("Entrez votre choix: \n1=Jouer\n2=Regarder\n3=Héberger\n4=Quitter");
            Scanner choix = new Scanner(System.in);
            switch (choix.nextLine()) {
                case "1":
                    MorpionReseau.morpionClient(JoueurServeur, JoueurClient, morpion);
                    break;
                case "2":
                    MorpionReseau.morpionSpectateur(JoueurServeur, JoueurClient, morpion);
                    break;
                case "3":
                    if (morpion.getNbTour() <= 0) {
                        MorpionReseau.morpionServeur(JoueurServeur, JoueurClient, morpion);
                    }
                case "4":
                    break;
                default:
                    System.out.println("Retape ton choix");
            }

    }
}
