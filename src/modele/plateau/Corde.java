package modele.plateau;

public class Corde extends EntiteStatique {
    public Corde(Jeu _jeu) { super(_jeu); }
    public boolean peutServirDeSupport() { return false; }

    public boolean peutPermettreDeMonterDescendre() { return true; };
}