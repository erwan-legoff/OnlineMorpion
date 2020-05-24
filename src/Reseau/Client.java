package Reseau;

import java.io.*;
import java.net.*;

public class Client {


    public static Socket getSocket(String ip,int port) throws IOException {
        // On crée un objet InetAdress sur  l'interface de loopback
        InetAddress adr = InetAddress.getByName(ip);
        // On crée une socket
        Socket s = new Socket (adr, port);
        System.out.println("Socket crée");
        return s;
    }


    public static String pull(DataInputStream entree) throws IOException {
        // On affiche l'écho du serveur
        return entree.readLine();
    }

    public static void push(String upload, PrintStream sortie) {
        // On envoie le texte saisi au serveur
        sortie.println(upload);
        sortie.flush();
    }






}
