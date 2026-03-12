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
import fr.nathan.visiteur.Visiteur;
import fr.nathan.visiteur.VisiteurClassique;

import fr.nathan.threads.GameLoop;

import javafx.application.Application;



public class JeuDeLaVie implements Observable{
    ArrayList<Observateur> observateurs = new ArrayList<Observateur>();
    ArrayList<Commande> commandes = new ArrayList<Commande>();
    Visiteur visiteur = new VisiteurClassique(this);


    Cellule[][] grille;
    int xMax;
    int yMax;

    public JeuDeLaVie(int xM, int yM) {
        this.grille = new Cellule[xM][yM];

        this.xMax = xM;
        this.yMax = yM;
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
                System.out.print(this.grille[i][j]);
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

    public void attacheObservateur(Observateur o) {
        this.observateurs.add(o);
    }
    public void detacheObservateur(Observateur o) {
        this.observateurs.remove(o);
    }
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

    public static void main(String[] args) throws InterruptedException {
        JeuDeLaVie jeu = new JeuDeLaVie(30, 30);
        jeu.initialiseGrille();
        
        JeuDeLaVieUI.jeu = jeu;

        JeuDeLaVieUI javaFXUI = new JeuDeLaVieUI();
        JeuDeLaVieUI.setJeuStatic(jeu);
        ConsoleUI consoleUI = new ConsoleUI();
        ConsoleUI.setJeuStatic(jeu);

        jeu.attacheObservateur(consoleUI);
        jeu.attacheObservateur(javaFXUI);

        Thread simulation = new Thread(() -> {
            while (true) {

                
                try {
                    jeu.calculerGenerationSuivante();
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        simulation.start();


        Application.launch(JeuDeLaVieUI.class, args);
    }
}