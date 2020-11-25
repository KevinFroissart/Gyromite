/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Héros du jeu
 */
public class Heros extends EntiteDynamique {

    private int nbPoint;
    private final String FILEPATH = "ressources/highscore.txt";

    public Heros(Jeu _jeu) {
        super(_jeu);
        nbPoint = 0;
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };

    public int getPoint(){
        return nbPoint;
    }

    public void setPoint(int point){
        this.nbPoint += point;
    }

    public boolean meilleurScore(){
        String score = "0";

        try(BufferedReader lectureFichier = new BufferedReader(new FileReader(FILEPATH))) {
            while((score = lectureFichier.readLine()) != null){}
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        return this.nbPoint > Integer.parseInt(score) ? nouveauMeilleurScore() : false;
    }

    public boolean nouveauMeilleurScore(){
        boolean retour = false;
        
        try (BufferedWriter ecritureFichier = new BufferedWriter(new FileWriter(FILEPATH))) {
            ecritureFichier.write(nbPoint);
            retour = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retour;
    }
}
