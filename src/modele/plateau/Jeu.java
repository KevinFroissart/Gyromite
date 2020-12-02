/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.Controle4Directions;
import modele.deplacements.ControleColonne;
import modele.deplacements.ControleInteraction;
import modele.deplacements.Direction;
import modele.deplacements.Gravite;
import modele.deplacements.IA;
import modele.deplacements.Interaction;
import modele.deplacements.Ordonnanceur;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.awt.Point;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    private final String SCOREPATH = System.getProperty("user.dir") + "\\ressources\\highscore.txt";
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;
    private int bombe_restante = 0;
    public int nb_vie = 3;
    private int score = 0;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHector() {
        return hector;
    }
    
    private void initialisationDesEntites() {

        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), 19, y);
        }

        LoadLevel("level1");
    }

    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }
    
    private void supprimerEntite(Entite e, int x, int y){
        grilleEntites[x][y] = null;
        map.remove(e);
    }
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        boolean deplacement = false;
        boolean bombe = false;

        if (contenuDansGrille(pCible)){ // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            if(objetALaPosition(pCible) != null){
                if(objetALaPosition(pCible).getClass() == Bombe.class){ deplacement = true; bombe = true; }
                if(objetALaPosition(pCible).getClass() == Heros.class && 
                    e.getClass() == Colonne.class){
                        nb_vie--;
                        deplacerEntite(objetALaPosition(pCible), Direction.gauche);
                        deplacement = true; 
                    }
            }
            if(objetALaPosition(pCible) == null) deplacement = true; 

            if(deplacement)
            switch (d) {
                case bas, haut, s, z:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);
                        if(bombe){ addPoint(50); bombe_restante--; }

                        retour = true;
                    }
                    break;
                case gauche, droite, q, d:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        if(bombe){ addPoint(50); bombe_restante--; }

                        retour = true;
                    }
                    break;
            }
        }

        if (retour) {
            deplacerEntite(pCourant, pCible, e);
        }

        return retour;
    }
    
    public boolean interactionEntite(Entite e, Interaction i){
        boolean retour = false;

        Point pCourant = map.get(e);

        if(contenuDansGrille(pCourant) && i == Interaction.Entree || i == Interaction.e){
            if(objetALaPosition(pCourant).getClass() == Bombe.class && objetALaPosition(pCourant).getClass() != Heros.class){
                retour = true; 
                supprimerEntite(objetALaPosition(pCourant), (int) pCourant.getX(), (int) pCourant.getY());
                
            }
            else if(objetALaPosition(pCourant).getClass() != Bombe.class ){
                retour = true; 
                addEntite(new Bombe(this), (int) pCourant.getX(), (int) pCourant.getY());
            }
        }

        return retour;
    }


    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        
        switch(d) {
            case haut, z: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas, s : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche, q : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite, d : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }

    /**
     * Read a CSV file that contains the objects of the level
     * @param levelName
     */
    private void LoadLevel(String levelName) {
        ArrayList<String[]> objects = new ArrayList<String[]>();
        String line;

        //reads all lines of the level file
        try {
            BufferedReader br = new BufferedReader(new FileReader("Levels/" + levelName + ".csv"));
            while((line = br.readLine()) != null) {
                //split each element of a line
                objects.add(line.split(","));
            }
            br.close();
        } catch (IOException e) {
            System.err.println("le fichier de niveau n'est pas valide");
            e.printStackTrace();
        }

        for (String[] obj : objects) {
            int x = Integer.parseInt(obj[1]);
            int y = Integer.parseInt(obj[2]);

            switch(obj[0]) {
                case "Heros":
                    hector = new Heros(this);
                    addEntite(hector, x, y);


                    Controle4Directions.getInstance().addEntiteDynamique(hector);
                    ordonnanceur.add(Controle4Directions.getInstance());
                    ControleInteraction.getInstance().addEntiteDynamique(hector);
                    ordonnanceur.add(ControleInteraction.getInstance());

                    Gravite g = new Gravite();
                    g.addEntiteDynamique(hector);
                    ordonnanceur.add(g);
                    break;
                case "Colonne":
                    BlocColonne col = new BlocColonne(this);
                    for(int i = 2; i < obj.length; i++)
                    {
                        Colonne elemCol = new Colonne(this);
                        addEntite(elemCol, x, Integer.parseInt(obj[i]));
                        col.addColonne(elemCol);
                    }
                    
                    ControleColonne.getInstance().addEntiteDynamique(col);
                    ordonnanceur.add(ControleColonne.getInstance());
                    break;
                case "Mur":
                    addEntite(new Mur(this), x, y);
                    break;
                case "Bombe":
                    addEntite(new Bombe(this), x, y);
                    bombe_restante++;
                    break;
                case "PoutreVerticale":
                    addEntite(new PoutreVerticale(this), x, y);
                    break;
                case "PoutreHorizontale":
                    addEntite(new PoutreHorizontale(this), x, y);
                    break;
                case "Smick":
                    Bot smick = new Bot(this);
                    addEntite(smick, x, y);
                    Gravite g2 = new Gravite();
                    g2.addEntiteDynamique(smick);
                    ordonnanceur.add(g2);
                    IA.getInstance().addEntiteDynamique(smick);
                    ordonnanceur.add(IA.getInstance());
                    break;
            }
        }
    }
    
    public int getPoint(){
        return score;
    }

    public void addPoint(int point){
        score += point;
    }

    public boolean meilleurScore(){
        String highscore = "";

        try(BufferedReader lectureFichier = new BufferedReader(new FileReader(SCOREPATH))) {
            highscore = lectureFichier.readLine().replaceAll("[^a-zA-Z0-9]", "");
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        System.out.println(Integer.parseInt(highscore));
        return highscore == null ? nouveauMeilleurScore() : score > Integer.parseInt(highscore) ? nouveauMeilleurScore() : false;
    }

    public boolean nouveauMeilleurScore(){
        boolean retour = false;
        
        try (BufferedWriter ecritureFichier = new BufferedWriter(new FileWriter(SCOREPATH))) {
            ecritureFichier.write(String.valueOf(score));
            retour = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retour;
    }

    public boolean gameFinished(){
        boolean retour = false;

        if(bombe_restante == 0) retour = true;
        if(nb_vie == 0) retour = true;
        if(retour){
            if(meilleurScore()) System.out.println("Nouveau Record!");
            else System.out.println(score + " points");
        }

        return retour;
    }

}
