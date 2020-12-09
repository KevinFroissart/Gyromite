
import VueControleur.VueControleurGyromite;
import modele.plateau.Jeu;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        VueControleurGyromite vc = new VueControleurGyromite(jeu);

        jeu.getOrdonnanceur().addObserver(vc);
        
        vc.setVisible(true);
        jeu.start(400);
    }
}
