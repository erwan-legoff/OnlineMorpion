package Jeu.Morpion;

public class Joueur {
    private String nomJ;

    private final String ipDuJoueur;
    private String piont;

    private boolean gagner;
    private int positionJ;
    private boolean cTonTour;

    public Joueur(String nomJ, String ipDuJoueur, String piont) {
        this.nomJ = nomJ;
        this.ipDuJoueur = ipDuJoueur;
        this.piont = piont;
        cTonTour = false;
        gagner=false;
    }
    public String getPion() {
        return piont;
    }

    void setGagner() {
        this.gagner = true;
    }

    public boolean isGagner() {
        return gagner;
    }


    public int getPositionJ(){
        return positionJ;
    }

    public void setPositionJ(int positionJ) {
        this.positionJ = positionJ;
    }

    public void setDoitJouer(boolean cTonTour) {
        this.cTonTour = cTonTour;
    }

    public boolean getDoitJouer() {
        return cTonTour;
    }

    public void setPion(String piont) {
        if (!piont.equals("") && !piont.equals(" ") && piont.length() == 1)
            this.piont = piont;
    }

    public String getNom() {
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
