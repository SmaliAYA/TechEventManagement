package services;


import exceptions.EmailDejaUtiliseException;
import exceptions.UtilisateurIntrouvableException;
import interfaces.IGestionUtilisateurs;
import models.users.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des utilisateurs.
 * Implémente les opérations CRUD et la persistance dans users.txt.
 */
public class UtilisateurService implements IGestionUtilisateurs {

    private List<Utilisateur> utilisateurs;
    private static final String FICHIER = "data/users.txt";

    // ─── Constructeur ────────────────────────────────────────────────────────
    public UtilisateurService() {
        this.utilisateurs = new ArrayList<>();
        chargerDepuisFichier();
    }



    /**
     * Ajoute un utilisateur après vérification de l'unicité de l'email.
     */
    @Override
    public void ajouterUtilisateur(Utilisateur u) throws EmailDejaUtiliseException {
        for (Utilisateur existant : utilisateurs) {
            if (existant.getEmail().equalsIgnoreCase(u.getEmail())) {
                throw new EmailDejaUtiliseException(u.getEmail());
            }
        }
        utilisateurs.add(u);
        sauvegarderDansFichier();
        System.out.println("✔ Utilisateur ajouté : " + u);
    }

    
    @Override
    public void modifierUtilisateur(int id, String nom, String prenom,
                                     String email, String motDePasse)
            throws UtilisateurIntrouvableException, EmailDejaUtiliseException {

        Utilisateur u = rechercherParId(id);

        // Vérifier que le nouvel email n'est pas déjà pris par quelqu'un d'autre
        if (email != null && !email.equalsIgnoreCase(u.getEmail())) {
            for (Utilisateur autre : utilisateurs) {
                if (autre.getId() != id && autre.getEmail().equalsIgnoreCase(email)) {
                    throw new EmailDejaUtiliseException(email);
                }
            }
            u.setEmail(email);
        }

        if (nom != null && !nom.isBlank())       u.setNom(nom);
        if (prenom != null && !prenom.isBlank())  u.setPrenom(prenom);
        if (motDePasse != null && !motDePasse.isBlank()) u.setMotDePasse(motDePasse);

        sauvegarderDansFichier();
        System.out.println("✔ Utilisateur modifié : " + u);
    }

    /**
     * Supprime un utilisateur par son ID.
     */
    @Override
    public void supprimerUtilisateur(int id) throws UtilisateurIntrouvableException {
        Utilisateur u = rechercherParId(id);
        utilisateurs.remove(u);
        sauvegarderDansFichier();
        System.out.println("✔ Utilisateur supprimé : " + u);
    }

   
    @Override
    public Utilisateur rechercherParId(int id) throws UtilisateurIntrouvableException {
        return utilisateurs.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UtilisateurIntrouvableException(id));
    }

    @Override
    public Utilisateur rechercherParEmail(String email) throws UtilisateurIntrouvableException {
        return utilisateurs.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new UtilisateurIntrouvableException(email));
    }

    @Override
    public List<Utilisateur> rechercherParNom(String nom) {
        return utilisateurs.stream()
                .filter(u -> u.getNom().equalsIgnoreCase(nom)
                        || u.getPrenom().equalsIgnoreCase(nom))
                .collect(Collectors.toList());
    }

  
    @Override
    public List<Utilisateur> afficherTous() {
        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur enregistré.");
        } else {
            System.out.println("\n══════════════════════ LISTE DES UTILISATEURS ══════════════════════");
            utilisateurs.forEach(System.out::println);
            System.out.println("════════════════════════════════════════════════════════════════════");
        }
        return new ArrayList<>(utilisateurs);
    }

    @Override
    public List<Utilisateur> afficherParRole(String role) {
        List<Utilisateur> filtres = utilisateurs.stream()
                .filter(u -> u.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());

        System.out.println("\n── Utilisateurs [" + role.toUpperCase() + "] ──");
        if (filtres.isEmpty()) System.out.println("Aucun utilisateur avec ce rôle.");
        else filtres.forEach(System.out::println);

        return filtres;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PERSISTANCE FICHIER  (data/users.txt)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Sauvegarde tous les utilisateurs dans users.txt.
     * Format par ligne : id;ROLE;nom;prenom;email;motDePasse;[champs spécifiques]
     */
    public void sauvegarderDansFichier() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER))) {
            for (Utilisateur u : utilisateurs) {
                writer.write(u.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge les utilisateurs depuis users.txt au démarrage.
     */
    public void chargerDepuisFichier() {
        File fichier = new File(FICHIER);
        if (!fichier.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) continue;
                Utilisateur u = parseLigne(ligne);
                if (u != null) utilisateurs.add(u);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
        }
    }

    /**
     * Parse une ligne du fichier users.txt et retourne l'objet correspondant.
     */
    private Utilisateur parseLigne(String ligne) {
        try {
            String[] parts = ligne.split(";");
            int id         = Integer.parseInt(parts[0]);
            String role    = parts[1];
            String nom     = parts[2];
            String prenom  = parts[3];
            String email   = parts[4];
            String mdp     = parts[5];

            switch (role.toUpperCase()) {
                case "PARTICIPANT":
                    String org = parts.length > 6 ? parts[6] : "";
                    return new Participant(id, nom, prenom, email, mdp, org);

                case "EXPOSANT":
                    String entreprise = parts.length > 6 ? parts[6] : "";
                    String secteur    = parts.length > 7 ? parts[7] : "";
                    return new Exposant(id, nom, prenom, email, mdp, entreprise, secteur);

                case "ORGANISATEUR":
                    Organisateur.Niveau niveau = parts.length > 6
                            ? Organisateur.Niveau.valueOf(parts[6])
                            : Organisateur.Niveau.JUNIOR;
                    return new Organisateur(id, nom, prenom, email, mdp, niveau);

                default:
                    System.err.println("Rôle inconnu : " + role);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur parsing ligne : " + ligne + " → " + e.getMessage());
            return null;
        }
    }

    // ─── Utilitaire ──────────────────────────────────────────────────────────
    public int getNombreUtilisateurs() { return utilisateurs.size(); }


}