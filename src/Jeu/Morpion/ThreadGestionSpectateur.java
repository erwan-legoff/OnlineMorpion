package Jeu.Morpion;

import Reseau.Client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadGestionSpectateur implements Runnable {
    private final Socket socketSpectateur;
    private PrintStream sortieServSpec;
    private final Morpion morpion;
    private final Joueur joueurClient;
    private final Joueur joueurServeur;


    public ThreadGestionSpectateur(Socket socketSpectateur, Morpion morpion, Joueur joueurServeur, Joueur joueurClient) {
        this.morpion = morpion;
        this.joueurClient = joueurClient;
        this.joueurServeur = joueurServeur;
        this.socketSpectateur = socketSpectateur;
        try { //on fait passer tous les paramètres par adresses, pour pouvoir les envoyer à tout moment par ce thread
            sortieServSpec = new PrintStream(new BufferedOutputStream(socketSpectateur.getOutputStream()));
            System.out.println("On écoute le spectateur sur le "+socketSpectateur);
            MorpionReseau.pushGrille(morpion,sortieServSpec);
            MorpionReseau.pushInfoJoueurAuSpectateur(joueurServeur, joueurClient, sortieServSpec);
            Client.push(String.valueOf(morpion.getNbTour()),sortieServSpec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            boolean serveurLu = false;
            boolean clientLu = false;
            while (morpion.peutContinuerPartie()) {
                Thread.sleep(100);
                if (joueurClient.getDoitJouer() && !clientLu) {
                    clientLu = true;
                    serveurLu = false;
                    MorpionReseau.pushGrille(morpion,sortieServSpec);
                    MorpionReseau.pushEtatPartieAuSpectateur(morpion, sortieServSpec);
                } //envoie la grille quand le client a joué
                //TODO: doublon des deux fonctions, elles ne servent qu'à synchroniser finalement...
                else if( joueurServeur.getDoitJouer() && !serveurLu) {
                    clientLu = false;
                    serveurLu = true;

                    // TODO: tester en uploadant le nom

                    MorpionReseau.pushGrille(morpion,sortieServSpec);
                    MorpionReseau.pushEtatPartieAuSpectateur(morpion, sortieServSpec);
                }//envoie la grille quand le serveur a joué
            }
            Client.push("FIN",sortieServSpec);
            socketSpectateur.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
