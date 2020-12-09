/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.util.Random;

import modele.deplacements.IA;
import modele.deplacements.RealisateurDeDeplacement;
import modele.deplacements.Gravite;
import modele.deplacements.Direction;

/**
 * Ennemis (Smicks)
 */
public class Bot extends EntiteDynamique {
    private Random r = new Random();
    private IA ia = new IA();
    private Gravite g = new Gravite();

    public Bot(Jeu _jeu) {
        super(_jeu);
        ia.addEntiteDynamique(this);
        g.addEntiteDynamique(this);
    }

    public Direction directionRandom(){
        Direction d = Direction.gauche;
        if(r.nextInt(2) == 1) d = Direction.droite;
        ia.setDirectionCourante(d);
        return d;
    }

    public RealisateurDeDeplacement getGravite(){
        return g;
    }

    public RealisateurDeDeplacement getIA(){
        return ia;
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
