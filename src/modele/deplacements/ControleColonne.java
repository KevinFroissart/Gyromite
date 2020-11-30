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
    private static boolean estEnHaut = false; /** position of the column. false for down, true for up */
    private int cmptMovement = 0; /** permet d'arreter le deplacement de la colonne */
    public final int DEPLACEMENT_PILLIER = 2;

    public static ControleColonne getInstance() {
        if (cCol == null) {
            cCol = new ControleColonne();
        }
        return cCol;
    }

    public void setDirectionCourante() {
        directionCourante = estEnHaut ? Direction.bas : Direction.haut;
        if (cmptMovement == DEPLACEMENT_PILLIER)
            cmptMovement = 0;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        int nbrIter = DEPLACEMENT_PILLIER * lstEntitesDynamiques.size();
        
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null){
                if (cmptMovement < nbrIter) {
                    ret = e.avancerDirectionChoisie(directionCourante);
                    cmptMovement++;
                    estEnHaut = cmptMovement == nbrIter ? !estEnHaut : estEnHaut;
                }
            }
        }
        
        return ret;
    }

    public void resetDirection() {
        directionCourante = null;
    }

}
