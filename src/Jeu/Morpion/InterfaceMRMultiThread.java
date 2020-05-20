package Jeu.Morpion;

import Reseau.Client;
import Reseau.Serveur;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class InterfaceMRMultiThread {

    public static void main(String[] args) {

        ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>();
        Joueur JoueurClient = new Joueur("Client","pouet","X");
        Joueur JoueurServeur = new Joueur("Serveur","pouette","O");
        listeJoueurs.add(JoueurClient);
        listeJoueurs.add(JoueurServeur);
        Morpion morpion = new Morpion(listeJoueurs);



        System.out.println("Entrez votre choix: \n1=Jouer\n2=Regarder\n3=Héberger");
        Scanner choix = new Scanner(System.in);
        switch (choix.nextLine()){
            case "1":
                morpionCoteClient(JoueurServeur,JoueurClient, morpion);
                break;
            case "2":
                morpionCoteSpectateur(JoueurServeur,JoueurClient, morpion);
                break;
            case "3":
                if(morpion.getNbTour()<=0) {
                    morpionCoteServeur(JoueurServeur,JoueurClient, morpion);
                }
                break;
            default:
                System.out.println("Retape ton choix");
        }
    }

    public static void morpionCoteClient(Joueur joueurServeur,Joueur joueurClient,  Morpion morpion) {
        Socket socket;//on essaye de se connecter a un serveur local
        try {

                saisirInfo(joueurClient);
                socket = Client.getSocket("localhost",2000);
                DataInputStream socketEntree = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
                PrintStream socketSortie = new PrintStream ( new BufferedOutputStream(socket.getOutputStream()));

                pushInfoJoueur(joueurClient, socketSortie);

                System.out.println("Attente des infos du serveur...");
                pullInfoJoueur(joueurServeur, socketEntree);


            while(morpion.peutContinuerPartie()) {
                jouerTour(morpion,joueurClient);
                //On envoie un message puis on attend une réponse
                pushCoup(joueurClient, socketSortie);
                //test

                //on reçoit les données
                pullCoup(joueurServeur, morpion, socketEntree);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Le client n'a pas pu se connecter");
        }
    }

    private static void jouerTour(Morpion morpion,Joueur joueur) {
        System.out.println(morpion);
        morpion.incrementerNbTour();
        morpion.jouer(joueur);
        morpion.incrementerNbTour();
        System.out.println(morpion);
    }

    // TODO: 19/05/2020  le thread spectateur ne ferme pas correctement le spectateur quand le client gagne
    public static void morpionCoteSpectateur(Joueur joueurServeur, Joueur joueurClient, Morpion morpion){
        Socket socket; //on essaye de se connecter a un serveur local
        try {
            socket = Client.getSocket("localhost",2001);
            DataInputStream socketEntree = new DataInputStream (new BufferedInputStream(socket.getInputStream()));

            pullGrille(morpion,socketEntree);
            pullInfoJoueur(joueurServeur,socketEntree);
            pullInfoJoueur(joueurClient,socketEntree);
            System.out.println("Vous observez une partie se jouant entre " + joueurServeur.getNomJ() + " et " + joueurClient.getNomJ());
            morpion.setNbTour(Integer.parseInt(Client.pull(socketEntree)));
            System.out.println("vous êtes au tour n°"+morpion.getNbTour());


            while(morpion.peutContinuerPartie()) {
                String message = Client.pull(socketEntree);
                if (message.equals("FIN"))
                    break;
                remplirGrille(morpion,message);
                morpion.incrementerNbTour();
                System.out.println(morpion);

            }
            Client.pull(socketEntree);
            System.out.println("partie terminée");
            morpion.afficherGagnant();
            socket.close();
        } catch (IOException e) {
            System.err.println("Le spectateur n'a pas pu se connecter");
        }
    }

    // TODO: 19/05/2020 erreur quand le serveur gagne le client n'a pas le dernier coup 
    public static void morpionCoteServeur(Joueur joueurServeur, Joueur joueurClient, Morpion morpion) {
        ///////////////////////////////////////////////////////Partie serveur\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        //Sinon on lance le serveur
        try {
            saisirInfo(joueurServeur);
            Socket socketClient = Serveur.initialisationServeur();
            ThreadSpectateur threadSpectateur = new ThreadSpectateur();
            threadSpectateur.setMorpion(morpion);

            threadSpectateur.setJoueurClient(joueurClient);
            threadSpectateur.setJoueurServeur(joueurServeur);
            threadSpectateur.start();

            DataInputStream entreeServJoueur = new DataInputStream(new BufferedInputStream(socketClient.getInputStream()));
            PrintStream sortieServJoueur = new PrintStream(new BufferedOutputStream(socketClient.getOutputStream()));

            Socket socketSpectateur = null;
            PrintStream sortieServSpec = null;
            threadSpectateur.setS_service_spectateur(socketSpectateur);
            threadSpectateur.setSortieServSpec(sortieServSpec);

            pullInfoJoueur(joueurClient, entreeServJoueur);
            while (joueurClient.getPiont().equals(joueurServeur.getPiont())) {
                System.out.println("Change de pion stp!");
                saisirInfo(joueurServeur);
            }

            pushInfoJoueur(joueurServeur, sortieServJoueur);

            boolean packetJoueurPushed = false;


            while (morpion.peutContinuerPartie()){
                pullCoup(joueurClient,morpion,entreeServJoueur);
                joueurServeur.setcTonTour(false);
                joueurClient.setcTonTour(true);
                System.out.println("On est dans le serveur "+joueurClient.iscTonTour());

                jouerTour(morpion,joueurServeur);
                pushCoup(joueurServeur, sortieServJoueur);
                joueurClient.setcTonTour(false);
                joueurServeur.setcTonTour(true);

            }
            socketClient.close();
            threadSpectateur.getS_service_spectateur().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pushEtatPartieAuSpec(Morpion morpion, ThreadSpectateur threadSpectateur) {
        if (!morpion.peutContinuerPartie())
            Client.push("FIN", threadSpectateur.getSortieServSpec());
        else
            Client.push("CONTINUE", threadSpectateur.getSortieServSpec());
    }
    public static void pushEtatPartieAuSpec(Morpion morpion, PrintStream sortieServSpec) {
        if (!morpion.peutContinuerPartie())
            Client.push("FIN", sortieServSpec);
    }
//test

    public static void pushGrille(Morpion morpion,PrintStream sortieServSpec){
        StringBuilder aEnvoyer = new StringBuilder();
        for (int i = 0; i < 9 ; i++) {
            aEnvoyer.append(morpion.getCaseGrilleDuMorpion(i));
        }
        Client.push(aEnvoyer.toString(),sortieServSpec);
    }
    private static void pullGrille(Morpion morpion,DataInputStream entreeSpec ){
        try {
            String grille = Client.pull(entreeSpec);
            for (int i = 0; i < 9; i++) {
                morpion.ajouterUnCoup(i, String.valueOf(grille.charAt(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void remplirGrille(Morpion morpion,String grille ){
            for (int i = 0; i < grille.length(); i++) {
                morpion.ajouterUnCoup(i, String.valueOf(grille.charAt(i)));
            }
    }
    public static void pushInfoJoueurAuSpect(Joueur joueurServeur, Joueur joueurClient, PrintStream sortieServSpec) {
        pushInfoJoueur(joueurServeur, sortieServSpec);
        pushInfoJoueur(joueurClient, sortieServSpec);
    }

    public static void pullCoup(Joueur adversaire, Morpion morpion, DataInputStream socketEntree) throws IOException {
        adversaire.setPositionJ(Integer.parseInt(Client.pull(socketEntree)));
        //Puis on met a jour le morpion
        morpion.ajouterUnCoup(adversaire.getPositionJ(), adversaire.getPiont());

    }

    public static void pushCoup(Joueur j, PrintStream socketSortie) {
        Client.push(String.valueOf(j.getPositionJ()), socketSortie);
    }


    public static void saisirInfo(Joueur j) {
        Scanner saisieJoueur = new Scanner(System.in);
        saisirNomJoueur(saisieJoueur, j);
        saisirPion(saisieJoueur, j);
    }

    public static void saisirNomJoueur(Scanner saisie, Joueur j) {
        System.out.print("Ton nom : \n");
        String nomJoueur = saisie.nextLine();
        j.setNomJoueur(nomJoueur);
    }

    public static void saisirPion(Scanner saisie, Joueur j) {
        System.out.print("Ton pion : \n");
        String pion = saisie.nextLine();
        j.setPion(pion);
    }

    public static void pushInfoJoueur(Joueur j1, PrintStream socketSortie) {
        //envoie des donnees joueur
        String pack = j1.getNomJ()+" "+j1.getPiont();
        Client.push(pack,socketSortie);
    }

    public static void pullInfoJoueur(Joueur adversaire, DataInputStream entreeServ) throws IOException {
        String packRecu = Client.pull(entreeServ);
        String[]infoAdversaire = packRecu.split(" ");
        adversaire.setNomJoueur(infoAdversaire[0]);
        adversaire.setPion(infoAdversaire[1]);
    }

}
