package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class ControleColonne extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static ControleColonne cCol;
    private boolean estEnHaut = false; /** position of the column. false for down, true for up */
    private static final int HAUTEUR_DEPLACEMENT = 2; /** nombre de mouvement des colonnes */
    private int nbrDeplacement = 0; /** nombre courant de deplacement */

    public static ControleColonne getInstance() {
        if (cCol == null) {
            cCol = new ControleColonne();
        }
        return cCol;
    }

    public void setDirectionCourante() {
        directionCourante = estEnHaut ? Direction.bas : Direction.haut;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null && nbrDeplacement < HAUTEUR_DEPLACEMENT){
                ret = e.avancerDirectionChoisie(directionCourante);
                nbrDeplacement++;
            }
            else if (nbrDeplacement >= HAUTEUR_DEPLACEMENT) {
                resetDirection();
                estEnHaut = !estEnHaut;
                nbrDeplacement = 0;
            }
        }
        System.out.println(directionCourante);
        
        return ret;
    }

    public void resetDirection() {
        directionCourante = null;
    }

}
