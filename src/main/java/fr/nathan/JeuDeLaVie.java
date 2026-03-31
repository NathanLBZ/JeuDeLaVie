package fr.nathan;

import java.util.ArrayList;

import fr.nathan.UI.ConsoleUI;
import fr.nathan.UI.JeuDeLaVieUI;
import fr.nathan.UI.Observable;
import fr.nathan.UI.Observateur;
import fr.nathan.cellule.Cellule;
import fr.nathan.cellule.CelluleEtatMort;
import fr.nathan.cellule.CelluleEtatVivant;
import fr.nathan.commande.Commande;
import fr.nathan.modeManuel.ModeManuel;
import fr.nathan.modePause.Pause;
import fr.nathan.visiteur.Visiteur;
import fr.nathan.visiteur.VisiteurClassique;
import fr.nathan.vitesse.Vitesse;
import javafx.application.Application;



public class JeuDeLaVie implements Observable{
    ArrayList<Observateur> observateurs = new ArrayList<>();
    ArrayList<Commande> commandes = new ArrayList<>();
    Visiteur visiteur;

    Pause modePause;
    Vitesse cooldown;
    ModeManuel modeManuel;

    private int nbGen = 0;


    public Cellule[][] grille;
    int xMax;
    int yMax;

    public JeuDeLaVie(int xM, int yM) {
        this.grille = new Cellule[xM][yM];

        this.xMax = xM;
        this.yMax = yM;

        this.modePause = new Pause();
        this.cooldown = new Vitesse();
    }   

    public int getXmax() {
        return this.xMax;
    }

    public int getYmax() {
        return this.yMax;
    }

    public int getNbGen() {
        return this.nbGen;
    }

    public void nextGen() {
        this.nbGen++;
    }

    public int nbCellulesVivantes() {
        int nb = 0;
        for (Cellule[] ligne : this.grille) {
            for (Cellule cell : ligne) {
                if (cell.estVivante()) {
                    nb++;
                }
            }
        }

        return nb;
    }

    public void setNbGen(int nb) {
        this.nbGen = nb;
    }

    public void inverserEtat(Cellule c) {
        c.inverserEtat();
    }
    
    public void initialiseGrille() {

        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                double nbRandom = Math.random();
                if (nbRandom < 0.5) {
                    this.grille[i][j] = new Cellule(i, j, CelluleEtatMort.getInstance());
                }
                else {
                    this.grille[i][j] = new Cellule(i, j, CelluleEtatVivant.getInstance());
                }
            }
        }
    }

    public void initialiseGrilleVide() {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                this.grille[i][j] = new Cellule(i, j, CelluleEtatMort.getInstance());

            }
        }
    }

    public Cellule getGrilleXY(int x, int y) {
        if ((x < 0) || (x > this.xMax - 1) || (y < 0) || (y > this.yMax - 1)) {
            return null;
        }
        return this.grille[x][y];
    }

    public void afficheGrille() {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                System.out.print(this.grille[j][i]);
            }
            System.out.println();
        }
    }

    public void afficheVoisines() {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                System.out.print(" " + this.getGrilleXY(i, j).nombreVoisinesVivantes(this) + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void attacheObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void detacheObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifieObservateurs() {
        for (Observateur o : this.observateurs) {
            o.actualise();
        }
    }

    public void setVisiteur(Visiteur v) {
        this.visiteur = v;
    }

    public void ajouteCommandes(Commande c) {
        this.commandes.add(c);
    } 

    public void executeCommandes() {
        for (Commande c : this.commandes) {
            c.executer();
        }
        this.commandes.clear();
    }

    // appele accepte pour chaque cellule
    public void distribueVisiteur() {
        for (int i = 0; i < this.xMax; i++) {
            for (int j = 0; j < this.yMax; j++) {
                this.grille[i][j].accepte(this.visiteur);
            }
        }
    }

    public void calculerGenerationSuivante() {
        this.distribueVisiteur();
        this.executeCommandes();
        this.notifieObservateurs();
    }

    public Boolean isPaused() {
        return this.modePause.isPaused();
    }

    public Pause getModPause() {
        return this.modePause;
    }

    public int getCooldown() {
        return this.cooldown.getCooldown();
    }
    public void setCooldown(int newCooldown) {
        this.cooldown.setCooldown(newCooldown);
    }

    public boolean getModeManuel() {
        return this.modeManuel.isManuel();
    }
    
    public void resetJeu() {
        this.nbGen = 0;

        for (Cellule[] ligne : this.grille) {
            for (Cellule cell : ligne) {
                cell.meurt();
            }
        }
    }

    
    public static void main(String[] args) throws InterruptedException {
        /*
        Scanner scanner = new Scanner(System.in);
        int largeur, hauteur;
        char c;
        
        do { 
            System.out.println("Nombre de cellules de large (>100 et <140)");
            System.out.print("Votre choix : ");
            largeur = scanner.nextInt();
        } while (largeur < 100 || largeur > 140);
        
        do  {
            System.out.println("\nNombre de cellules en hauteur (>50 et <80)");
            System.out.print("Votre choix : ");
            hauteur = scanner.nextInt();  
        }
        while (hauteur < 50 || hauteur > 80);
        
        do  {
            System.out.println("\nVoulez vous activer le mode manuel pour placer les cellules à la main et des paterns ?");
            System.out.print("Votre choix (y/n) : ");
            c = scanner.next().charAt(0);  
        }
        while (c != 'y' && c != 'n');
        */
        
        JeuDeLaVie jeu = new JeuDeLaVie(140, 80);
        
        
        //if (c == 'y') {
            jeu.initialiseGrilleVide();
            jeu.modeManuel = new ModeManuel(true);
        /*
    } 
    else {
        jeu.initialiseGrille();
    jeu.modeManuel = new ModeManuel(false);
    
}
*/


// Choix des règles de vie et mort
        /*
        int choixVisiteur;
        do  {
            System.out.println("\nChoix mode de jeu : \n\t1) Classique \n\t2) HighLife \n\t3) Covid");
            System.out.print("Votre choix : ");
            choixVisiteur = scanner.nextInt();
        }
        while (choixVisiteur != 1 && choixVisiteur != 2 && choixVisiteur != 3);
        
        if (choixVisiteur == 1) {
            jeu.setVisiteur(new VisiteurClassique(jeu));
        }
        else if (choixVisiteur == 2) {
            jeu.setVisiteur(new VisiteurHighLife(jeu));
        }
        else if (choixVisiteur == 3) {
            jeu.setVisiteur(new VisiteurCovid(jeu));
        }
        
        scanner.close();
        
        */
        
        JeuDeLaVieUI.jeu = jeu;

        jeu.setVisiteur(new VisiteurClassique(jeu));

        JeuDeLaVieUI.setJeuStatic(jeu);
        ConsoleUI consoleUI = new ConsoleUI();
        ConsoleUI.setJeuStatic(jeu);

        
        jeu.attacheObservateur(consoleUI);
        
        Thread simulation = new Thread(() -> {
            try {

                // attente active avant que le joueur démarre la simulation
                while (jeu.isPaused()) {
                    Thread.sleep(100);
                }
                
                while (!Thread.currentThread().isInterrupted()) {
                    if (!jeu.isPaused()) {
                        jeu.calculerGenerationSuivante();
                        Thread.sleep(jeu.getCooldown());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        simulation.setDaemon(true); 
        simulation.start();


        Application.launch(JeuDeLaVieUI.class, args);
    }
}