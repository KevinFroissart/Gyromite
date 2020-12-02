package modele.plateau;

import java.util.ArrayList;

import modele.deplacements.Direction;

/**
 * Représente une colonne entiere formee de plusieurs blocs
 */
public class BlocColonne extends EntiteDynamique {

    public static final int TAILLE_COLONNE = 3; // les colonnes ont une hauteur de 3

    public BlocColonne(Jeu _jeu) { 
        super(_jeu); 
        composantes = new ArrayList<Colonne>();
    }

    public ArrayList<Colonne> composantes;

    /**
     * Ajoute un element colonne au bloc
     * @param c un element colonne
     */
    public void addColonne(Colonne c) {
        composantes.add(c);
    }

    /**
     * deplace tous les elements de la colonne
     * @param d la direction ou se deplacer
     */
    public boolean avancerDirectionChoisie(Direction d) {
        boolean ret = false;
        if (d == Direction.haut) {
            for (int i = TAILLE_COLONNE - 1; i >= 0; i--) {
                if (d != null) {
                    ret = composantes.get(i).avancerDirectionChoisie(d);
                }
            }
        } else if (d == Direction.bas) {
            for (int i = 0; i < TAILLE_COLONNE; i++) {
                if (d != null) {
                    ret = composantes.get(i).avancerDirectionChoisie(d);
                }
            }
        }
        return ret;
    }
    
    /**
     * Verifie si la colonne a un voisin en haut ou en bas
     * Pour une verif en haut on prendra l'element le plus haut
     * Pour une verif en bas on prendra le plus bas
     * @param d la direction ou regarder
     */
    public Entite regarderDansLaDirection(Direction d) {
        if (d == Direction.haut) {
            return jeu.regarderDansLaDirection(composantes.get(0), d);
        } else if (d == Direction.bas) {
            return jeu.regarderDansLaDirection(composantes.get(composantes.size() - 1), d);
        }
        return null;
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
