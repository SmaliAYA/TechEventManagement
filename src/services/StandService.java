package services;

import models.stands.Stand;
import utils.FileManager;
import exceptions.StandNotFoundException;

import java.util.List;

/**
 * Service qui gère toutes les opérations liées aux stands.
 * C’est ici qu’on met la logique métier (ajout, recherche, assignation, etc.)
 */
public class StandService {

    // Liste des stands chargés depuis le fichier
    private List<Stand> stands;

    /**
     * Constructeur
     * On charge automatiquement les stands depuis le fichier au démarrage
     */
    public StandService() {
        this.stands = FileManager.loadStands();
    }

    /**
     * Ajouter un nouveau stand dans la liste
     * On vérifie d’abord qu’il n’existe pas déjà (par ID)
     */
    public void ajouterStand(Stand s) {

        for (Stand st : stands) {
            if (st.getId() == s.getId()) {
                System.out.println("⚠ Stand déjà existant !");
                return;
            }
        }

        stands.add(s);
        System.out.println("✔ Stand ajouté avec succès");
    }

    /**
     * Afficher tous les stands disponibles
     */
    public void afficherStands() {

        if (stands.isEmpty()) {
            System.out.println("Aucun stand disponible");
            return;
        }

        for (Stand s : stands) {
            System.out.println(s);
        }
    }

    /**
     * Assigner un exposant à un stand
     * Si le stand n’existe pas, on lance une exception
     */
    public void assignerExposant(int standId, String exposantId)
            throws StandNotFoundException {

        for (Stand s : stands) {
            if (s.getId() == standId) {
                s.setExposantId(exposantId);
                System.out.println("✔ Exposant assigné au stand " + standId);
                return;
            }
        }

        // Cas où aucun stand n’a été trouvé
        throw new StandNotFoundException(
                "Stand avec ID " + standId + " introuvable"
        );
    }

    /**
     * Rechercher un stand par son ID
     */
    public Stand chercherStand(int id)
            throws StandNotFoundException {

        for (Stand s : stands) {
            if (s.getId() == id) {
                return s;
            }
        }

        throw new StandNotFoundException("Stand introuvable");
    }

    /**
     * Sauvegarder tous les stands dans le fichier
     */
    public void sauvegarder() {
        FileManager.saveStands(stands);
        System.out.println("✔ Données sauvegardées avec succès");
    }
}