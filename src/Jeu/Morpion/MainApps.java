package Jeu.Morpion;

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


        System.out.println("Entrez votre choix: \n1=Jouer\n2=Regarder\n3=Héberger");
        Scanner choix = new Scanner(System.in);
        switch (choix.nextLine()){
            case "1":
                InterfaceMRMultiThread.morpionCoteClient(JoueurServeur,JoueurClient, morpion);
                break;
            case "2":
                InterfaceMRMultiThread.morpionCoteSpectateur(JoueurServeur,JoueurClient, morpion);
                break;
            case "3":
                if(morpion.getNbTour()<=0) {
                    InterfaceMRMultiThread.morpionCoteServeur(JoueurServeur,JoueurClient, morpion);
                }
                break;
            default:
                System.out.println("Retape ton choix");
        }
    }
}
