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
        System.out.println("Entrer votre choix: \n1=Jouer\n2=Regarder\n3=Héberger");
        Scanner choix = new Scanner(System.in);
        switch (choix.nextLine()){
            case "1":
                morpionCoteClient(j1, adversaire, morpion);
                break;
            case "2":
                morpionCoteSpectateur(j1, adversaire, morpion);
                break;
            case "3":
                if(morpion.getNbTour()<=0) {
                    morpionCoteServeur(j1, adversaire, morpion);
                }
                break;
            default:
                System.out.println("Retape ton choix");
        }
    }

    public static void morpionCoteClient(Joueur j1, Joueur adversaire, Morpion morpion) {
        Socket socket;//on se essaye de se connecter a un serveur local
        try {
                socket = Client.getSocket("iconya.fr",2000);
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

    public static void morpionCoteSpectateur(Joueur j1, Joueur adversaire, Morpion morpion){
        Socket socket; //on se essaye de se connecter a un serveur local
        try {
            socket = Client.getSocket("iconya.fr",2001);
            DataInputStream socketEntree = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
            miseAJourDonneesAdversaire(j1,socketEntree);
            miseAJourDonneesAdversaire(adversaire,socketEntree);
            while(morpion.isPasFinDeLaPartie()) {
                receptionMorpion(j1,morpion,socketEntree);
                receptionMorpion(adversaire,morpion,socketEntree);
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
            Socket s_service_spectateur = Serveur.initialisationSpectateur();
            DataInputStream entreeServ = new DataInputStream(new BufferedInputStream(s_service.getInputStream()));
            PrintStream sortieServ = new PrintStream(new BufferedOutputStream(s_service.getOutputStream()));
            PrintStream sortieServSpec = new PrintStream(new BufferedOutputStream(s_service_spectateur.getOutputStream()));

            miseAJourDonneesAdversaire(adversaire, entreeServ);
            envoyerDonneesJoueur(j1, sortieServ);
            envoyerDonneesJoueur(j1, sortieServSpec);
            envoyerDonneesJoueur(adversaire, sortieServSpec);

            while (morpion.isPasFinDeLaPartie()) {
                receptionMorpion(adversaire,morpion,entreeServ);
                envoiMorpionSpec(adversaire,morpion,sortieServSpec);
                morpion.jouer(0);
                envoiMorpion(j1,morpion,sortieServ);
                envoiMorpionSpec(j1,morpion,sortieServSpec);
            }
            s_service.close();
            s_service_spectateur.close();

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

    public static void envoiMorpionSpec(Joueur j1, Morpion morpion, PrintStream socketSortie) {
        Client.envoyerDonnee(String.valueOf(j1.getPositionJ()), socketSortie);
        morpion.incrementerNbTour();
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
