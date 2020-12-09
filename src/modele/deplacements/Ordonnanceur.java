package modele.deplacements;

import modele.plateau.Jeu;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Jeu jeu;
    private ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<RealisateurDeDeplacement>();
    private long pause;

    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    public void remove(RealisateurDeDeplacement deplacement) {
        lstDeplacements.remove(deplacement);
    }

    public void clear() {
        lstDeplacements.clear();
    }

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean update = false;
        while(!jeu.gameFinished()) {
            jeu.resetCmptDepl();
            //for (int i = lstDeplacements.size() - 1; i >= 0;i--) {
                for (RealisateurDeDeplacement d : lstDeplacements) {
              //  RealisateurDeDeplacement d = lstDeplacements.get(i);
                if (d.realiserDeplacement())
                    update = true;
            }

            Controle4Directions.getInstance().resetDirection();
            ControleInteraction.getInstance().resetInteraction();

            if (update) {
                setChanged();
                notifyObservers();
            }

            try {
                sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
