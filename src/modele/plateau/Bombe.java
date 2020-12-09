package modele.plateau;

public class Bombe extends EntiteStatique {
    public Bombe(Jeu _jeu) { super(_jeu); }
    public boolean peutServirDeSupport() { return false; }
}
