package Jeu.Morpion;

import Reseau.Client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class SpectateurProcessor implements Runnable {
    private Socket socketClient;
    private PrintStream sortieServSpec;
    private Morpion morpion;
    private Joueur joueurClient;
    private Joueur joueurServeur;


    public SpectateurProcessor(Socket socketClient,Morpion morpion,Joueur joueurServeur,Joueur joueurClient) {
        this.joueurClient = joueurClient;
        this.joueurServeur = joueurServeur;
        this.socketClient = socketClient;
        try {
            sortieServSpec = new PrintStream(new BufferedOutputStream(socketClient.getOutputStream()));
            System.out.println("On écoute sur le "+socketClient);
            InterfaceMRMultiThread.pushGrille(morpion,sortieServSpec);
            InterfaceMRMultiThread.pushInfoJoueurAuSpect(joueurServeur, joueurClient, sortieServSpec);
            Client.push(String.valueOf(morpion.getNbTour()),sortieServSpec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
            while (morpion.peutContinuerPartie()) {
                if (joueurClient.iscTonTour()) {
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
    }
}
