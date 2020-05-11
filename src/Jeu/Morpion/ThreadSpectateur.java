package Jeu.Morpion;

import Reseau.Serveur;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadSpectateur extends Thread {
    private Socket s_service_spectateur;
    private PrintStream sortieServSpec;
    private boolean connected = false;


    @Override
    public void run() {
        try {
            System.out.println("On rentre de la run");
            s_service_spectateur= Serveur.initialisationSpectateur();
            System.out.println("Le spectateur n'est pas enovre");
            sortieServSpec = new PrintStream(new BufferedOutputStream(s_service_spectateur.getOutputStream()));
            System.out.println("Le spectateur connecté");
            connected = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setS_service_spectateur(Socket s_service_spectateur) {
        this.s_service_spectateur = s_service_spectateur;
    }

    public void setSortieServSpec(PrintStream sortieServSpec) {
        this.sortieServSpec = sortieServSpec;
    }

    public Socket getS_service_spectateur() {
        return s_service_spectateur;
    }

    public PrintStream getSortieServSpec() {
        return sortieServSpec;
    }

    public boolean isConnected() {
        return connected;
    }
}
