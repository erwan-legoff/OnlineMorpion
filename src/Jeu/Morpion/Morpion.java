package Jeu.Morpion;

import InterfaceGraphique.Interface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Morpion {
    private final ArrayList<Joueur> listeJoueurs;


    private final String[] grilleMorpion;
    private int nbTour;

    private static final int[][] casGagnants = new int[][] {{ 0, 1, 2 }, { 3, 4, 5 },{ 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 }, { 2, 4, 6 } };

    public Morpion(ArrayList<Joueur> listeJoueurs) {
        this.listeJoueurs = listeJoueurs;

        grilleMorpion = new String[9];
        Arrays.fill(grilleMorpion, " ");
        nbTour = 0;
    }
    public void incrementerNbTour(){
        nbTour++;
    }

    public String[] getGrilleMorpion() {
        return grilleMorpion;
    }

    public void jouer(Joueur joueur){
//        if (peutContinuerPartie()) {
//
//            do {
//                System.out.println("C'est a toi de jouer "+ joueur.getNom());
//                joueur.setPosition(saisirEntier() - 1);
//
//            } while (!estCoupValide(joueur));
//            ajouterUnCoup(joueur.getPosition(), joueur.getPion());
//
//        }
        jouerInterface(joueur);

    }
    public void jouerInterface(Joueur joueur){
        if (peutContinuerPartie()) {

            do {
//                System.out.println("C'est a toi de jouer "+ joueur.getNom());
                joueur.setPosition(Interface.getIdCoupJoueur() - 1);


            } while (!estCoupValide(joueur));
            ajouterUnCoup(joueur.getPosition(), joueur.getPion());
        }

    }

    public void ajouterUnCoup(int coup, String pion){
        grilleMorpion[coup] = pion;
        if(!pion.equals(" ")) {
            incrementerNbTour();
        }
    }

    public boolean estCoupValide(Joueur joueur) {

        if (joueur.getPosition() < 0 || joueur.getPosition()>8) {
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return false;
        }
        if (grilleMorpion[joueur.getPosition()] == " ") {
            return true;
        }
        else {
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return false;
        }
    }


    public void setNbTour(int nbTour) {
        this.nbTour = nbTour;
    }
    //TODO : peutContinuerPartie ne doit pas afficher...
    public boolean peutContinuerPartie(){
        boolean partieGagnee = partieGagnee();
        if (!partieGagnee && nbTour<9)
            return true;

        if (nbTour>=9 && !partieGagnee) {
            System.out.print("Egalité : Fin de la partie");
            return false;
        }

        System.out.println("Fin de la partie");
        afficherGagnant();
        return false;


    }

    protected void afficherGagnant() {
        for (Joueur joueur : listeJoueurs) {
            if (joueur.isGagnant())
                System.out.println("Le gagnant est : " + joueur);
        }
    }


    private boolean partieGagnee() {
        String piont;
        if (nbTour<3)
            return false;
        int cptDePoint;// on init un cpt de point
        for (int i = 0; i < casGagnants.length; i++) { //on vien parcourir le tableau des cas gagnants
            cptDePoint = 0;
            piont = grilleMorpion[casGagnants[i][0]];//On initialise le piont à comparer
            if (piont != " "){
                for (int j = 0; j < casGagnants[0].length ; j++) { //on parcourt les 3 positions des pionts gagnants
                   if(grilleMorpion[casGagnants[i][j]] == piont){ // on compte le nombre de fois que le joueur est positionné
                       cptDePoint ++;                              // comme indiqué
                   }
                }
            }
            if (cptDePoint >= 3) // s'il est positionné 3 fois comme un cas gagnant alors C GAGNE
            {
                for (Joueur listeDesJoueur : listeJoueurs) {
                    if (listeDesJoueur.getPion()== piont ) {
                        listeDesJoueur.setGagnant();
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

    public String getCaseGrilleMorpion(int i) {
        return grilleMorpion[i];
    }


    @Override
    public String toString() {
        StringBuilder affichage = new StringBuilder("Tour n°" + nbTour + "\n Ton profil : " + listeJoueurs.get(0) + "\n Adversaire : " + listeJoueurs.get(1) + "\n");
        for (int i = 7; i > 0  ; i-=3) {
            for (int j = 0; j < grilleMorpion.length/3 ; j++) {
                affichage.append("|").append(grilleMorpion[i + j - 1]);
            }
            affichage.append("|\n");
        }
        return affichage.toString();
    }
}
