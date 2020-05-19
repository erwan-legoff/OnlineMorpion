package Jeu.Morpion;

public class Joueur {
    private String nomJ;
    //Recuperer l'adresse ip
    private final String ipDuJoueur;
    private String piont;
    private int Score;
    private boolean gagner;
    private int positionJ;
    private boolean cTonTour;//qui commence? WARNING

    public Joueur(String nomJ, String ipDuJoueur, String piont) {
        this.nomJ = nomJ;
        this.ipDuJoueur = ipDuJoueur;
        this.piont = piont;
        cTonTour = false;
        gagner=false;
    }
    public String getPiont() {
        return piont;
    }

    void setGagner() {
        this.gagner = true;
    }

    public boolean isGagner() {
        return gagner;
    }

    public String getIpDuJoueur() {
        return ipDuJoueur;
    }
    public int getPositionJ(){
        return positionJ;
    }

    public void setPositionJ(int positionJ) {
        this.positionJ = positionJ;
    }

    public void setcTonTour(boolean cTonTour) {
        this.cTonTour = cTonTour;
    }

    public boolean iscTonTour() {
        return cTonTour;
    }

    public void setPion(String piont) {
        if (!piont.equals("") && !piont.equals(" ") && piont.length() == 1)
            this.piont = piont;
    }

    public String getNomJ() {
        return nomJ;
    }

    public void setNomJoueur(String nomJ) {
        if(!nomJ.contains(" ")&& !nomJ.equals(""))
            this.nomJ = nomJ;
    }

    @Override
    public String toString() {
        return  nomJ +" "+piont;
    }
}
