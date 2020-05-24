package Jeu.Morpion;

public class Joueur {
    private String nom;

    private final String ip;
    private String pion;

    private boolean gagnant;
    private int positionJ;
    private boolean hasPlayed;

    public Joueur(String nom, String ip, String pion) {
        this.nom = nom;
        this.ip = ip;
        this.pion = pion;
        hasPlayed = false;
        gagnant =false;
    }
    public String getPion() {
        return pion;
    }

    void setGagnant() {
        this.gagnant = true;
    }

    public boolean isGagnant() {
        return gagnant;
    }


    public int getPosition(){
        return positionJ;
    }

    public void setPosition(int positionJ) {
        this.positionJ = positionJ;
    }

    public void setHasPlayed(boolean cTonTour) {
        this.hasPlayed = cTonTour;
    }

    public boolean getHasPlayed() {
        return hasPlayed;
    }

    public void setPion(String piont) {
        if (!piont.equals("") && !piont.equals(" ") && piont.length() == 1)
            this.pion = piont;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if(!nom.contains(" ")&& !nom.equals(""))
            this.nom = nom;
    }

    @Override
    public String toString() {
        return  nom +" "+ pion;
    }
}
