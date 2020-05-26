package Jeu.Morpion;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServeurEcouteSpectateur extends Thread {

    private Morpion morpion;
    private Joueur jClient;
    private Joueur jServeur;


    @Override
    public void run() {

            try {
                ServerSocket s_ecoute = new ServerSocket(2001);
                while (morpion.peutContinuerPartie()) {
                    Socket spectateur = s_ecoute.accept();
                    System.out.println("Connexion d'un nouveau spectateur...");
                    Thread t = new Thread(new ThreadGestionSpectateur(spectateur, morpion, jServeur, jClient));
                    t.start();
                    Thread.sleep(10);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
    }


    public void setMorpion(Morpion morpion) {
        this.morpion = morpion;
    }

    public void setjClient(Joueur jClient) {
        this.jClient = jClient;
    }

    public void setjServeur(Joueur jServeur) {
        this.jServeur = jServeur;
    }

}
