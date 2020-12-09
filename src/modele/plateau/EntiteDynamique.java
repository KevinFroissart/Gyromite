package modele.plateau;

import modele.deplacements.Direction;
import modele.deplacements.Interaction;

/**
 * Entités amenées à bouger (colonnes, ennemis)
 */
public abstract class EntiteDynamique extends Entite {
    public EntiteDynamique(Jeu _jeu) { super(_jeu); }

    public boolean avancerDirectionChoisie(Direction d) {
        return jeu.deplacerEntite(this, d);
    }
    public boolean interactionObjetCourant(Interaction i, Direction d){
        return jeu.interactionEntite(this, i, d);
    }
    public Entite regarderDansLaDirection(Direction d) {return jeu.regarderDansLaDirection(this, d);}
}
