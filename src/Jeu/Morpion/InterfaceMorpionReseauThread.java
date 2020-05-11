package Jeu.Morpion;

import Reseau.Client;
import Reseau.Serveur;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class InterfaceMorpionReseauThread {
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

                pushInfoJoueur(j1, socketSortie);

                pullInfoJoueur(adversaire, socketEntree);


            while(morpion.peutContinuerPartie()) {
                jouerTour(morpion);
                //On envoie un message puis on attend une réponse
                pushCoup(j1, morpion, socketSortie);
                //test

                //on reçoit les données
                pullCoup(adversaire, morpion, socketEntree);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Le client n'a pas pu se connecter");
        }
    }

    private static void jouerTour(Morpion morpion) {
        System.out.println(morpion);
        morpion.incrementerNbTour();
        morpion.jouer(0);
        morpion.incrementerNbTour();
        System.out.println(morpion);
    }

    public static void morpionCoteSpectateur(Joueur joueurServeur, Joueur joueurClient, Morpion morpion){
        Socket socket; //on se essaye de se connecter a un serveur local
        try {
            socket = Client.getSocket("localhost",2001);
            DataInputStream socketEntree = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
            pullGrille(morpion,socketEntree);
            pullInfoJoueur(joueurServeur,socketEntree);
            pullInfoJoueur(joueurClient,socketEntree);
            System.out.println("Vous observez une partie se jouant entre " + joueurServeur.getNomJ() + " et " + joueurClient.getNomJ());

            while(true) {
                System.out.println("attente du coup de " + joueurClient.getNomJ() + "...");
                pullCoup(joueurClient, morpion, socketEntree);

                if (Client.recevoirDonnee(socketEntree).equals("FIN"))
                    break;
                System.out.println(morpion);

                System.out.println("attente du coup de " + joueurServeur.getNomJ() + "...");
                pullCoup(joueurServeur, morpion, socketEntree);

                if (Client.recevoirDonnee(socketEntree).equals("FIN"))
                    break;
                System.out.println(morpion);
            }
            System.out.println("partie terminée");
            Client.recevoirDonnee(socketEntree);
            socket.close();
        } catch (IOException e) {
            System.err.println("Le spectateur n'a pas pu se connecter");
        }
    }

    public static void morpionCoteServeur(Joueur j1, Joueur adversaire, Morpion morpion) {
        ///////////////////////////////////////////////////////Partie serveur\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        //Sinon on lance le serveur
        try {
            ThreadSpectateur threadSpectateur = new ThreadSpectateur();
            Socket s_service = Serveur.initialisationServeur();

            DataInputStream entreeServ = new DataInputStream(new BufferedInputStream(s_service.getInputStream()));
            PrintStream sortieServ = new PrintStream(new BufferedOutputStream(s_service.getOutputStream()));

            threadSpectateur.start();
            Socket s_service_spectateur = null;
            PrintStream sortieServSpec = null;
            threadSpectateur.setS_service_spectateur(s_service_spectateur);
            threadSpectateur.setSortieServSpec(sortieServSpec);

            pullInfoJoueur(adversaire, entreeServ);
            pushInfoJoueur(j1, sortieServ);

            boolean packetJoueurPushed = false;


            while (morpion.peutContinuerPartie()){
                pullCoup(adversaire,morpion,entreeServ);
                if (threadSpectateur.isConnected()) {
                    if (!packetJoueurPushed) {
                        pushGrille(morpion,threadSpectateur.getSortieServSpec());
                        pushInfoJoueurAuSpect(j1, adversaire, threadSpectateur.getSortieServSpec());
                    }
                    packetJoueurPushed = true;
                    pushCoup(adversaire, morpion, threadSpectateur.getSortieServSpec());

                    envoieEtatPartieAuSpec(morpion, threadSpectateur);
                }
                jouerTour(morpion);
                pushCoup(j1,morpion,sortieServ);
                if (threadSpectateur.isConnected() && packetJoueurPushed) {
                    pushCoup(j1,morpion,threadSpectateur.getSortieServSpec());
                    envoieEtatPartieAuSpec(morpion, threadSpectateur);
                }

            }
            Client.envoyerDonnee("1",threadSpectateur.getSortieServSpec());
            s_service.close();
            threadSpectateur.getS_service_spectateur().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void envoieEtatPartieAuSpec(Morpion morpion, ThreadSpectateur threadSpectateur) {
        if (!morpion.peutContinuerPartie())
            Client.envoyerDonnee("FIN", threadSpectateur.getSortieServSpec());
        else
            Client.envoyerDonnee("CONTINUE", threadSpectateur.getSortieServSpec());
    }

    private static void pushGrille(Morpion morpion,PrintStream sortieServSpec){
        StringBuilder aEnvoyer = new StringBuilder();
        for (int i = 0; i < 9 ; i++) {
            aEnvoyer.append(morpion.getCaseGrilleDuMorpion(i));
        }
        Client.envoyerDonnee(aEnvoyer.toString(),sortieServSpec);
    }
    private static void pullGrille(Morpion morpion,DataInputStream entreeServ ){
        try {
            String grille = Client.recevoirDonnee(entreeServ);
            for (int i = 0; i < 9; i++) {
                morpion.ajouterUnCoup(i, String.valueOf(grille.charAt(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void pushInfoJoueurAuSpect(Joueur j1, Joueur adversaire, PrintStream sortieServSpec) {
        pushInfoJoueur(j1, sortieServSpec);
        pushInfoJoueur(adversaire, sortieServSpec);
    }

    public static void pullCoup(Joueur adversaire, Morpion morpion, DataInputStream socketEntree) throws IOException {
        adversaire.setPositionJ(Integer.parseInt(Client.recevoirDonnee(socketEntree)));
        //Puis on met a jour le morpion
        morpion.ajouterUnCoup(adversaire.getPositionJ(), adversaire.getPiont());

    }

    public static void pushCoup(Joueur j1, Morpion morpion, PrintStream socketSortie) {
        Client.envoyerDonnee(String.valueOf(j1.getPositionJ()), socketSortie);
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

    public static void pushInfoJoueur(Joueur j1, PrintStream socketSortie) {
        //envoie des donnees joueur
        String pack = j1.getNomJ()+" "+j1.getPiont();
        Client.envoyerDonnee(pack,socketSortie);
    }

    public static void pullInfoJoueur(Joueur adversaire, DataInputStream entreeServ) throws IOException {
        String packRecu = Client.recevoirDonnee(entreeServ);
        String[]infoAdversaire = packRecu.split(" ");
        adversaire.setNomJ(infoAdversaire[0]);
        adversaire.setPiont(infoAdversaire[1]);
    }

}
