package services;

import models.events.Conference;
import exceptions.ConflitHoraireException;

import java.util.ArrayList;

public class PlanningService {

    private ArrayList<Conference> conferences = new ArrayList<>();

    // Ajouter conférence avec vérification conflit
    public void ajouterConference(Conference c) throws ConflitHoraireException {

        for (Conference existing : conferences) {
            if (existing.getDate().equals(c.getDate()) &&
                existing.getHeure().equals(c.getHeure()) &&
                existing.getSalle().getId() == c.getSalle().getId()) {

                throw new ConflitHoraireException(
                        "Conflit: salle déjà occupée à cette heure !");
            }
        }

        conferences.add(c);
    }

    // Afficher planning
    public void afficherPlanning() {
        System.out.println("=== PLANNING CONFERENCES ===");
        for (Conference c : conferences) {
            System.out.println(c);
        }
    }

    // Rechercher par mot clé (String)
    public void rechercher(String motCle) {
        for (Conference c : conferences) {
            if (c.getTitre().toLowerCase().contains(motCle.toLowerCase())) {
                System.out.println(c);
            }
        }
    }

    // Supprimer conférence
    public void supprimer(int id) {
        conferences.removeIf(c -> c.getId() == id);
    }
}