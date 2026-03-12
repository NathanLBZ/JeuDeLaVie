package fr.nathan;

import java.util.ArrayList;
import java.util.Scanner;

import fr.nathan.UI.ConsoleUI;
import fr.nathan.UI.JeuDeLaVieUI;
import fr.nathan.UI.Observable;
import fr.nathan.UI.Observateur;
import fr.nathan.cellule.Cellule;
import fr.nathan.cellule.CelluleEtatMort;
import fr.nathan.cellule.CelluleEtatVivant;
import fr.nathan.commande.Commande;
import fr.nathan.modePause.Pause;
import fr.nathan.visiteur.Visiteur;
import fr.nathan.visiteur.VisiteurClassique;
import fr.nathan.vitesse.Vitesse;
import javafx.application.Application;



public class JeuDeLaVie implements Observable{
    ArrayList<Observateur> observateurs = new ArrayList<>();
    ArrayList<Commande> commandes = new ArrayList<>();
    Visiteur visiteur = new VisiteurClassique(this);

    Pause modePause;
    Vitesse cooldown;


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

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int largeur, hauteur;

        do { 
            System.out.print("Nombre de cellules de large (>5 et <140): ");
            largeur = scanner.nextInt();
            System.out.print("Nombre de cellules en hauteur (>5 et <80): ");
            hauteur = scanner.nextInt();  
        } while (largeur < 5 || largeur > 140 || hauteur < 5 || hauteur > 80);

        scanner.close();

        JeuDeLaVie jeu = new JeuDeLaVie(largeur, hauteur);
        jeu.initialiseGrille();
        
        JeuDeLaVieUI.jeu = jeu;

        JeuDeLaVieUI.setJeuStatic(jeu);
        ConsoleUI consoleUI = new ConsoleUI();
        ConsoleUI.setJeuStatic(jeu);

        
        //jeu.attacheObservateur(consoleUI);
        
        Thread simulation = new Thread(() -> {
            try {
                // temps pour charger la fenetre
                Thread.sleep(2000); 
                
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