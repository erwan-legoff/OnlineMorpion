package Jeu.Morpion;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApps {
    public static void main(String[] args) {

        Scanner saisieJoueur = new Scanner(System.in);
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>();
        Joueur j1 = new Joueur("Player1","pouet","X");
        Joueur adversaire = new Joueur("Player2","pouette","O");
        listeJoueurs.add(j1);
        listeJoueurs.add(adversaire);
        Morpion morpion = new Morpion(listeJoueurs);

        // TODO: 12/03/20 si les deux joueurs ont le meme nom ou pion on demande de nouveau au client de rentrer son pion ou son nom
        InterfaceMorpionReseau.saisirInfo(saisieJoueur, j1);
        System.out.println("Entrer votre choix: \n1=Jouer\n2=Regarder\n3=Héberger");
        Scanner choix = new Scanner(System.in);
        switch (choix.nextLine()){
            case "1":
                InterfaceMorpionReseau.morpionCoteClient(j1, adversaire, morpion);
                break;
            case "2":
                InterfaceMorpionReseau.morpionCoteSpectateur(j1, adversaire, morpion);
                break;
            case "3":
                if(morpion.getNbTour()<=0) {
                    InterfaceMorpionReseau.morpionCoteServeur(j1, adversaire, morpion);
                }
                break;
            default:
                System.out.println("Retape ton choix");
        }
    }
}
