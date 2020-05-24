package Reseau;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {


    public static Socket initialisationServeur(int port) throws IOException {
        // On crée une socket serveur ( le port est passé en argument)
        ServerSocket s_ecoute = new ServerSocket(port);
        System.out.println("Serveur client démarré sur la socket d'écoute " + s_ecoute);
        // Attente d'une connexion
        Socket s_service = s_ecoute.accept();
        // Une connexion a été ouverte
        System.out.println("Ouverture de la connexion sur la socket de service " + s_service);
        return s_service;
    }


}
