package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Random;

import modele.deplacements.Controle4Directions;
import modele.deplacements.ControleColonne;
import modele.deplacements.ControleInteraction;
import modele.deplacements.Direction;
import modele.deplacements.IA;
import modele.deplacements.Interaction;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoHeroDroite;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoColonneHaut;
    private ImageIcon icoColonne;
    private ImageIcon icoColonneBas;
    private ImageIcon icoBombe;
    private ImageIcon icoPoutreHorizontale;
    private ImageIcon icoPoutreVerticale;
    private ImageIcon gameOverScreen;
    private ImageIcon icoSmick;
    private ImageIcon icoSmickDroite;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleurGyromite(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
        playMusic();
        lancerIA();
    }

    private void lancerIA(){
        Random r = new Random();
        if(r.nextInt(2) == 1) IA.getInstance().setDirectionCourante(Direction.gauche);
        IA.getInstance().setDirectionCourante(Direction.droite);
    }

    private void playMusic() { 
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("ressources/gyromite-music.wav").getAbsoluteFile()); 
            Clip clip = AudioSystem.getClip(); 
            clip.open(audioInputStream); 
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
        } catch(IOException e){
            e.printStackTrace();
        } catch(LineUnavailableException e){
            e.printStackTrace();
        } catch(UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT, KeyEvent.VK_Q : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP, KeyEvent.VK_Z : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_A : ControleColonne.getInstance().setDirectionCourante(); break;
                    case KeyEvent.VK_ENTER, KeyEvent.VK_E : ControleInteraction.getInstance().setInteractionCourante(Interaction.Entree); break;
                }
            }
        });
    }

    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/Prof_Idle.png");
        icoHeroDroite = chargerIcone("Images/Prof_Idle_droite.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoColonneHaut = chargerIcone("Images/Colonne_top.png");
        icoColonne = chargerIcone("Images/Colonne_middle.png");
        icoColonneBas = chargerIcone("Images/Colonne_bottom.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoBombe = chargerIcone("Images/Bomb.png");
        icoPoutreHorizontale = chargerIcone("Images/Poutre_Horizontale.png");
        icoPoutreVerticale = chargerIcone("Images/Poutre_Verticale.png");
        icoSmick = chargerIcone("Images/Smick_idle.png");
        icoSmickDroite = chargerIcone("Images/Smick_idle_droite.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX+1)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];
        

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue
                    //Direction d = Controle4Directions.getInstance().getDirectionCourante();
                    //if( d == Direction.droite) tabJLabel[x][y].setIcon(icoHeroDroite);
                    //if( d == Direction.gauche) tabJLabel[x][y].setIcon(icoHero);
                    tabJLabel[x][y].setIcon(icoHero);
                } else if (jeu.getGrille()[x][y] instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                } else if (jeu.getGrille()[x][y] instanceof Bot) {
                    Direction d = IA.getInstance().getDirectionCourante();
                    if( d == Direction.droite) tabJLabel[x][y].setIcon(icoSmickDroite);
                    if( d == Direction.gauche) tabJLabel[x][y].setIcon(icoSmick);
                } else if (jeu.getGrille()[x][y] instanceof Bombe) {
                    tabJLabel[x][y].setIcon(icoBombe);
                } else if (jeu.getGrille()[x][y] instanceof PoutreVerticale){
                    tabJLabel[x][y].setIcon(icoPoutreVerticale);
                } else if (jeu.getGrille()[x][y] instanceof PoutreHorizontale){
                    tabJLabel[x][y].setIcon(icoPoutreHorizontale);
                } else if (jeu.getGrille()[x][y] instanceof Colonne) {
                    //si on a une colonne, on affiche un sprite different selon la position de 
                    //la colonne (pour afficher le haut et le bas)
                    if (!(jeu.getGrille()[x][y - 1] instanceof Colonne)) {
                        tabJLabel[x][y].setIcon(icoColonneHaut);
                    } else if (!(jeu.getGrille()[x][y + 1] instanceof Colonne)) {
                        tabJLabel[x][y].setIcon(icoColonneBas);
                    } else {
                        tabJLabel[x][y].setIcon(icoColonne);
                    }

                } else {
                    tabJLabel[x][y].setIcon(icoVide);
                }
            }
        }
    }

    public void AfficherGameOver() {
        //afficher image gameover
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        if(jeu.gameFinished()) {
            AfficherGameOver();
        } ;
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
