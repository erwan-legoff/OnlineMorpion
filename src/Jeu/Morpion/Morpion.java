package Jeu.Morpion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Morpion {
    private ArrayList<Joueur> listeDesJoueurs;
    private boolean finDeLaPartie;
    private Joueur leGagnant;
    private String[] grilleDuMorpion;
    private int nbTour;

    private static final int[][] casGagnants = new int[][] {{ 0, 1, 2 }, { 3, 4, 5 },{ 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 }, { 2, 4, 6 } };

    public Morpion(ArrayList<Joueur> listeDesJoueurs) {
        this.listeDesJoueurs = listeDesJoueurs;
        finDeLaPartie = false;
        leGagnant = null;
        grilleDuMorpion = new String[9];
        Arrays.fill(grilleDuMorpion, " ");
        nbTour = 0;
    }
    public void incrementerNbTour(){
        nbTour++;
    }

    public void jouer(int idJoueur){
        if (peutContinuerPartie()) {
            System.out.println("C'est a toi de jouer \n");
            Joueur joueur = listeDesJoueurs.get(idJoueur);
            do {
                joueur.setPositionJ(saisirEntier() - 1);
            } while (!estCoupValide(idJoueur));
            grilleDuMorpion[joueur.getPositionJ()] = joueur.getPiont();
        }

    }
    public void ajouterUnCoup(int coup, String piont){
        grilleDuMorpion[coup] = piont;
    }

    public boolean estCoupValide(int idJoueur){
        Joueur joueur = listeDesJoueurs.get(idJoueur);

        if (joueur.getPositionJ() < 0 || joueur.getPositionJ()>8) {
            System.out.println("mauvaise case");
            return false;
        }
        if (grilleDuMorpion[joueur.getPositionJ()] == " ") {
            return true;
        }
        else {
            System.out.println("Ooohhhh tu sais jouer  ???");
            return false;
        }
    }



    public boolean peutContinuerPartie(){

        if (partieGagnee())
            afficherGagnant();

        if (nbTour>=9 ) {
            System.out.print("Egalite : Fin de la partie");
            return false;
        }
        if (partieGagnee()){
            System.out.println("Fin de la partie");
            return false;
        }
        return true;
    }

    private void afficherGagnant() {
        for (Joueur listeDesJoueur : listeDesJoueurs) {
            if (listeDesJoueur.isGagner())
                System.out.println("Le joueur qui a gagner est le joueur : " + listeDesJoueur);
        }
    }

    private boolean partieGagnee() {
        String piont;
        int cptDePoint=0;// on init un cpt de point
        for (int i = 0; i < casGagnants.length; i++) { //on vien parcourir le tableau des cas gagnants
            cptDePoint = 0;
            piont = grilleDuMorpion[casGagnants[i][0]];//On initialise le piont à comparer
            if (piont != " "){
                for (int j = 0; j < casGagnants[0].length ; j++) { //on parcourt les 3 positions des pionts gagnants
                   if(grilleDuMorpion[casGagnants[i][j]] == piont){ // on compte le nombre de fois que le joueur est positionné
                       cptDePoint ++;                              // comme indiqué
                   }
                }
            }
            if (cptDePoint == 3) // s'il est positionné 3 fois comme un cas gagnant alors C GAGNE
            {
                for (Joueur listeDesJoueur : listeDesJoueurs) {
                    if (listeDesJoueur.getPiont()== piont ) {
                        listeDesJoueur.setGagner(true);
                    }
                }
                return true;
            }

        }
        return false;
    }
    public static int saisirEntier () {

        Scanner clavier=new Scanner(System.in);
        String s = clavier.nextLine(); //int lu = clavier.nextInt();
        int lu=456;
        try{
            lu = Integer.parseInt(s);
        }
        catch(NumberFormatException ex){
            System.err.println("Ce n'est pas un entier valide");
        }
        return lu;
    }

    public int getNbTour() {
        return nbTour;
    }

    @Override
    public String toString() {
        String affichage = "Tour :"+nbTour+"\n Toi : "+listeDesJoueurs.get(0)+"\n Adversaire : "+listeDesJoueurs.get(1)+"\n";
        for (int i = 7; i > 0  ; i-=3) {
            for (int j = 0; j < grilleDuMorpion.length/3 ; j++) {
                affichage += "|"+grilleDuMorpion[i+j-1];
            }
            affichage+= "|\n";
        }
        return affichage;
    }
}
