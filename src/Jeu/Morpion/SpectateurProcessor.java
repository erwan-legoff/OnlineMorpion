package Jeu.Morpion;

import Reseau.Client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class SpectateurProcessor implements Runnable {
    private Socket socketSpectateur;
    private PrintStream sortieServSpec;
    private Morpion morpion;
    private Joueur joueurClient;
    private Joueur joueurServeur;


    public SpectateurProcessor(Socket socketSpectateur,Morpion morpion,Joueur joueurServeur,Joueur joueurClient) {
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
            while (morpion.peutContinuerPartie()) {
                Thread.sleep(100);
                if (joueurClient.iscTonTour()) {
                    System.out.println("On est dans le spectateur "+joueurClient.iscTonTour());
                    joueurClient.setcTonTour(false);
                    InterfaceMRMultiThread.pushCoup(joueurClient, sortieServSpec);
                    InterfaceMRMultiThread.pushEtatPartieAuSpec(morpion, sortieServSpec);
                }
                else if( joueurServeur.iscTonTour()) {
                    joueurServeur.setcTonTour(false);
                    InterfaceMRMultiThread.pushCoup(joueurServeur, sortieServSpec);
                    InterfaceMRMultiThread.pushEtatPartieAuSpec(morpion, sortieServSpec);
                }
            }

            Client.push("1",sortieServSpec);
            socketSpectateur.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
