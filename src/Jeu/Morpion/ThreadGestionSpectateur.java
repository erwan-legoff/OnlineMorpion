package Jeu.Morpion;

import Reseau.Client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadGestionSpectateur implements Runnable {
    private final Socket socketSpectateur;
    private PrintStream sortieServSpec;
    private Morpion morpion;
    private final Joueur joueurClient;
    private final Joueur joueurServeur;


    public ThreadGestionSpectateur(Socket socketSpectateur, Morpion morpion, Joueur joueurServeur, Joueur joueurClient) {
        this.morpion = morpion;
        this.joueurClient = joueurClient;
        this.joueurServeur = joueurServeur;
        this.socketSpectateur = socketSpectateur;
        try {
            sortieServSpec = new PrintStream(new BufferedOutputStream(socketSpectateur.getOutputStream()));
            System.out.println("On écoute sur le "+socketSpectateur);
            InterfaceMRMultiThread.pushGrille(morpion,sortieServSpec);
            InterfaceMRMultiThread.pushInfoJoueurAuSpect(joueurServeur, joueurClient, sortieServSpec);
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
                if (joueurClient.doitJouer() && !clientLu) {
                    clientLu = true;
                    serveurLu = false;
                    System.out.println("On est dans le spectateur "+joueurClient.doitJouer());
                    InterfaceMRMultiThread.pushGrille(morpion,sortieServSpec);
                    InterfaceMRMultiThread.pushEtatPartieAuSpec(morpion, sortieServSpec);
                }
                else if( joueurServeur.doitJouer() && !serveurLu) {
                    clientLu = false;
                    serveurLu = true;
                    InterfaceMRMultiThread.pushGrille(morpion,sortieServSpec);
                    InterfaceMRMultiThread.pushEtatPartieAuSpec(morpion, sortieServSpec);
                }
            }
            Client.push("FIN",sortieServSpec);
            socketSpectateur.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}