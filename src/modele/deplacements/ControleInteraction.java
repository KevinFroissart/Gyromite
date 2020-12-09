package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class ControleInteraction extends RealisateurDeDeplacement {
    private Interaction interactionCourante;
    private Direction directionCourante;
    // Design pattern singleton
    private static ControleInteraction c3d;

    public static ControleInteraction getInstance() {
        if (c3d == null) {
            c3d = new ControleInteraction();
        }
        return c3d;
    }

    public static ControleInteraction reset() {
        c3d = new ControleInteraction();
        return c3d;
    }

    public void setInteractionCourante(Interaction _interactionCourante, Direction _directionCourante) {
        interactionCourante = _interactionCourante;
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (interactionCourante != null && interactionCourante == Interaction.Entree || interactionCourante == Interaction.e){
                if (e.interactionObjetCourant(interactionCourante, directionCourante))
                    ret = true;
            }
        }
        return ret;
    }

    public void resetInteraction() {
        interactionCourante = null;
    }
}