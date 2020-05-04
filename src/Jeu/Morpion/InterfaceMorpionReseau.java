package Jeu.Morpion;

import Reseau.Client;
import Reseau.Serveur;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class InterfaceMorpionReseau {
    public static void main(String[] args) {
        Scanner saisieJoueur = new Scanner(System.in);
        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>();
        Joueur j1 = new Joueur("Player1","pouet","X");
        Joueur adversaire = new Joueur("Player2","pouette","O");
        listeJoueurs.add(j1);
        listeJoueurs.add(adversaire);
        Morpion morpion = new Morpion(listeJoueurs);

        // TODO: 12/03/20 si les deux joueurs ont le meme nom ou pion on demande de nouveau au client de rentrer son pion ou son nom
        saisirInfo(saisieJoueur, j1);
        morpionCoteClient(j1, adversaire, morpion);

        if (morpion.getNbTour()<=0) {
            morpionCoteServeur(j1, adversaire, morpion);
        }

    }

    public static void morpionCoteClient(Joueur j1, Joueur adversaire, Morpion morpion) {
        Socket socket;//on se essaye de se connecter a un serveur local
        try {
                socket = Client.getSocket("10.20.116.3",2000);
                DataInputStream socketEntree = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
                PrintStream socketSortie = new PrintStream ( new BufferedOutputStream(socket.getOutputStream()));

                envoyerDonneesJoueur(j1, socketSortie);

                miseAJourDonneesAdversaire(adversaire, socketEntree);


            while(morpion.isPasFinDeLaPartie()) {

                morpion.jouer(0);
                //On envoie un message puis on attend une réponse
                envoiMorpion(j1, morpion, socketSortie);
                //test

                //on reçoit les données
                receptionMorpion(adversaire, morpion, socketEntree);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Le client n'a pas pu se connecter");
        }
    }

    public static void morpionCoteServeur(Joueur j1, Joueur adversaire, Morpion morpion) {
        ///////////////////////////////////////////////////////Partie serveur\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        //Sinon on lance le serveur
        try {
            Socket s_service = Serveur.initialisationServeur();
            DataInputStream entreeServ = new DataInputStream(new BufferedInputStream(s_service.getInputStream()));
            PrintStream sortieServ = new PrintStream(new BufferedOutputStream(s_service.getOutputStream()));

            miseAJourDonneesAdversaire(adversaire, entreeServ);
            envoyerDonneesJoueur(j1, sortieServ);

            while (morpion.isPasFinDeLaPartie()) {
                receptionMorpion(adversaire,morpion,entreeServ);
                morpion.jouer(0);
                envoiMorpion(j1,morpion,sortieServ);
            }
            s_service.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void receptionMorpion(Joueur adversaire, Morpion morpion, DataInputStream socketEntree) throws IOException {
        adversaire.setPositionJ(Integer.parseInt(Client.recevoirDonnee(socketEntree)));
        morpion.incrementerNbTour();
        //Puis on met a jour le morpion
        morpion.remplirGrilleAvecUncoup(adversaire.getPositionJ(), adversaire.getPiont());
        System.out.println(morpion);
    }

    public static void envoiMorpion(Joueur j1, Morpion morpion, PrintStream socketSortie) {
        Client.envoyerDonnee(String.valueOf(j1.getPositionJ()), socketSortie);
        morpion.incrementerNbTour();
        System.out.println(morpion);
    }

    public static void saisirInfo(Scanner saisieJoueur, Joueur j1) {
        saisirNomJoueur(saisieJoueur, j1);
        saisirPiont(saisieJoueur, j1);
    }

    public static void saisirNomJoueur(Scanner saisieJoueur, Joueur j1) {
        System.out.print("Ton nom : \n");
        String nomJoueur = saisieJoueur.nextLine();
        j1.setNomJ(nomJoueur);
    }

    public static void saisirPiont(Scanner saisieJoueur, Joueur j1) {
        System.out.print("Ton pion : \n");
        String piontJoueur = saisieJoueur.nextLine();
        j1.setPiont(piontJoueur);
    }

    public static void envoyerDonneesJoueur(Joueur j1, PrintStream socketSortie) {
        //envoie des donnees joueur
        String pack = j1.getNomJ()+" "+j1.getPiont();
        Client.envoyerDonnee(pack,socketSortie);
    }

    public static void miseAJourDonneesAdversaire(Joueur adversaire, DataInputStream entreeServ) throws IOException {
        String packRecu = Client.recevoirDonnee(entreeServ);
        String[]infoAdversaire = packRecu.split(" ");
        adversaire.setNomJ(infoAdversaire[0]);
        adversaire.setPiont(infoAdversaire[1]);
    }

}
