package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class DeplacementCorde extends RealisateurDeDeplacement{

    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {

            if(e.avancerDirectionChoisie(Direction.bas)){
                Entite eBas = e.regarderDansLaDirection(Direction.bas);
                if(eBas.peutPermettreDeMonterDescendre() && eBas != null) ret = true;
            }
            if(e.avancerDirectionChoisie(Direction.haut)){
                Entite eHaut = e.regarderDansLaDirection(Direction.haut);
                if(eHaut.peutPermettreDeMonterDescendre() && eHaut != null) ret = true;
            }
        }
        return ret;
    }
}