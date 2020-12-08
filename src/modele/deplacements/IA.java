package modele.deplacements;

import modele.plateau.EntiteDynamique;
import java.util.Random;

public class IA extends RealisateurDeDeplacement {
    private static Direction directionCourante;
    // Design pattern singleton
    private static IA c3d;

    public static IA getInstance() {
        if (c3d == null) {
            c3d = new IA();
        }
        return c3d;
    }

    public static IA reset() {
        c3d = new IA();
        return c3d;
    }

    public static Direction getDirectionCourante(){
        return directionCourante;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public static Direction directionRandom(){
        Random r = new Random();
        if(r.nextInt(2) == 1) return Direction.droite;
        return Direction.gauche;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
                switch (directionCourante) {
                    case gauche :
                        if (e.avancerDirectionChoisie(directionCourante))
                            ret = true;
                        else setDirectionCourante(Direction.droite);
                        break;
                    case droite :
                        if (e.avancerDirectionChoisie(directionCourante))
                            ret = true;
                        else setDirectionCourante(Direction.gauche);
                        break;
                }
            else directionRandom();
        }

        return ret;

    }

}
