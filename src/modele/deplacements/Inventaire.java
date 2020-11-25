package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class Inventaire extends RealisateurDeDeplacement{
        
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
     
            if(e.equals(/*turnip*/null)){
                
            }

        }

        return ret;
    }

}
