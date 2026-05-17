package models.events;

public class Salle {
    private int id;
    private String nom;
    private int capacite;

    public Salle(int id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    @Override
    public String toString() {
        return nom + " (capacité: " + capacite + ")";
    }
}