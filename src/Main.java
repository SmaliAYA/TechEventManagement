import exceptions.*;
import models.reservations.Feedback;
import models.reservations.Reservation;
import models.users.*;
import services.ReservationService;
import services.UtilisateurService;
 
import models.events.*;
import services.PlanningService;

import models.stands.Stand;
import services.StandService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
 
/**
 * Point d'entrée principal — Menu interactif de gestion des utilisateurs.
 * (À intégrer avec les autres modules au fur et à mesure)
 */
public class Main {
 
    private static final UtilisateurService service = new UtilisateurService();
    
    private static final PlanningService planningService = new PlanningService();
    
    private static final StandService standService = new StandService();

    private static final ReservationService reservationService = new ReservationService();
    
    private static final Scanner scanner = new Scanner(System.in);
 
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    Gestion des Utilisateurs      ║");
        System.out.println("╚══════════════════════════════════╝");
 
        boolean running = true;
        while (running) {
            afficherMenu();
            int choix = lireEntier("Votre choix : ");
            switch (choix) {
                case 1 -> ajouterUtilisateur();
                case 2 -> modifierUtilisateur();
                case 3 -> supprimerUtilisateur();
                case 4 -> rechercherUtilisateur();
                case 5 -> service.afficherTous();
                case 7 -> menuConferences();
                case 6 -> afficherParRole();
                case 8 -> menuStands();
                case 9 -> menuReservations();
                case 0 -> { running = false; System.out.println("Au revoir !"); }
                default -> System.out.println("⚠ Choix invalide.");
            }
        }
        scanner.close();
    }
 
    // ════════════════════════════════════════════════════════════════════════
    //  MENU
    // ════════════════════════════════════════════════════════════════════════
 
    private static void afficherMenu() {
        System.out.println("\n──────────────────────────────────");
        System.out.println("  1. Ajouter un utilisateur");
        System.out.println("  2. Modifier un utilisateur");
        System.out.println("  3. Supprimer un utilisateur");
        System.out.println("  4. Rechercher un utilisateur");
        System.out.println("  5. Afficher tous les utilisateurs");
        System.out.println("  6. Afficher par rôle");
        System.out.println("  7. Gestion des conférences");
        System.out.println("  8. Gestion des stands");
        System.out.println("  9. Gestion des réservations & feedbacks");
        System.out.println("  0. Quitter");
        System.out.println("──────────────────────────────────");
    }
 
    // ════════════════════════════════════════════════════════════════════════
    //  ACTIONS
    // ════════════════════════════════════════════════════════════════════════
 
    private static void ajouterUtilisateur() {
        System.out.println("\n── Quel type d'utilisateur ? ──");
        System.out.println("  1. Participant");
        System.out.println("  2. Exposant");
        System.out.println("  3. Organisateur");
        int type = lireEntier("Type : ");
 
        String nom     = lireChaine("Nom : ");
        String prenom  = lireChaine("Prénom : ");
        String email   = lireChaine("Email : ");
        String mdp     = lireChaine("Mot de passe : ");
 
        try {
            switch (type) {
                case 1 -> {
                    String org = lireChaine("Organisation (laisser vide si aucune) : ");
                    service.ajouterUtilisateur(new Participant(nom, prenom, email, mdp, org));
                }
                case 2 -> {
                    String entreprise = lireChaine("Entreprise : ");
                    String secteur    = lireChaine("Secteur : ");
                    service.ajouterUtilisateur(new Exposant(nom, prenom, email, mdp, entreprise, secteur));
                }
                case 3 -> {
                    System.out.println("Niveau (JUNIOR / SENIOR / ADMIN) : ");
                    String niveauStr = scanner.nextLine().trim().toUpperCase();
                    Organisateur.Niveau niveau;
                    try { niveau = Organisateur.Niveau.valueOf(niveauStr); }
                    catch (IllegalArgumentException e) { niveau = Organisateur.Niveau.JUNIOR; }
                    service.ajouterUtilisateur(new Organisateur(nom, prenom, email, mdp, niveau));
                }
                default -> System.out.println("⚠ Type invalide.");
            }
        } catch (EmailDejaUtiliseException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }
 
    private static void modifierUtilisateur() {
        int id = lireEntier("ID de l'utilisateur à modifier : ");
        System.out.println("(Laisser vide pour ne pas modifier un champ)");
        String nom    = lireChaine("Nouveau nom : ");
        String prenom = lireChaine("Nouveau prénom : ");
        String email  = lireChaine("Nouvel email : ");
        String mdp    = lireChaine("Nouveau mot de passe : ");
 
        try {
            service.modifierUtilisateur(
                id,
                nom.isBlank()    ? null : nom,
                prenom.isBlank() ? null : prenom,
                email.isBlank()  ? null : email,
                mdp.isBlank()    ? null : mdp
            );
        } catch (UtilisateurIntrouvableException | EmailDejaUtiliseException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }
 
    private static void supprimerUtilisateur() {
        int id = lireEntier("ID de l'utilisateur à supprimer : ");
        try {
            service.supprimerUtilisateur(id);
        } catch (UtilisateurIntrouvableException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }
 
    private static void rechercherUtilisateur() {
        System.out.println("  1. Par ID\n  2. Par email\n  3. Par nom");
        int choix = lireEntier("Choix : ");
        try {
            switch (choix) {
                case 1 -> {
                    int id = lireEntier("ID : ");
                    System.out.println(service.rechercherParId(id));
                }
                case 2 -> {
                    String email = lireChaine("Email : ");
                    System.out.println(service.rechercherParEmail(email));
                }
                case 3 -> {
                    String nom = lireChaine("Nom / Prénom : ");
                    service.rechercherParNom(nom).forEach(System.out::println);
                }
                default -> System.out.println("⚠ Choix invalide.");
            }
        } catch (UtilisateurIntrouvableException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }
 
    private static void afficherParRole() {
        System.out.println("Rôle (PARTICIPANT / EXPOSANT / ORGANISATEUR) : ");
        String role = scanner.nextLine().trim();
        service.afficherParRole(role);
    }
 
    /**
     * Menu de gestion des conférences et du planning
     */
    private static void menuConferences() {
 
        boolean retour = false;
 
        while (!retour) {
 
            System.out.println("\n══════ GESTION DES CONFERENCES ══════");
            System.out.println("1. Ajouter une conférence");
            System.out.println("2. Afficher planning");
            System.out.println("3. Rechercher conférence");
            System.out.println("4. Supprimer conférence");
            System.out.println("0. Retour");
 
            int choix = lireEntier("Votre choix : ");
 
            switch (choix) {
 
                case 1 -> ajouterConference();
 
                case 2 -> planningService.afficherPlanning();
 
                case 3 -> {
                    String motCle = lireChaine("Mot clé : ");
                    planningService.rechercher(motCle);
                }
 
                case 4 -> {
                    int id = lireEntier("ID conférence : ");
                    planningService.supprimer(id);
                }
 
                case 0 -> retour = true;
 
                default -> System.out.println("⚠ Choix invalide.");
            }
        }
    }
 
    /**
     * Ajoute une conférence au planning avec une salle associée
     */
    private static void ajouterConference() {
 
        int id = lireEntier("ID conférence : ");
        String titre = lireChaine("Titre : ");
        String theme = lireChaine("Thème : ");
        LocalDate date;
        try {
            date = LocalDate.parse(lireChaine("Date sous forme \"yyyy-MM-dd\" : "));
        } catch (DateTimeParseException e) {
            System.out.println("✖ Format de date invalide. Utilisez yyyy-MM-dd (ex: 2024-05-20)");
            return;
        }        String heure = lireChaine("Heure : ");
 
        System.out.println("\n--- SALLE ---");
 
        int salleId = lireEntier("ID salle : ");
        String nomSalle = lireChaine("Nom salle : ");
        int capacite = lireEntier("Capacité : ");
 
        Salle salle = new Salle(salleId, nomSalle, capacite);
 
        Conference conference = new Conference(
                id,
                titre,
                theme,
                date,
                heure,
                salle
        );
 
        try {
            planningService.ajouterConference(conference);
            System.out.println("✔ Conférence ajoutée avec succès");
        } catch (ConflitHoraireException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }
 
    /**
     * Menu de gestion des stands
     */
    private static void menuStands() {
 
        boolean retour = false;
 
        while (!retour) {
 
            System.out.println("\n========== GESTION DES STANDS ==========");
            System.out.println("1. Ajouter un stand");
            System.out.println("2. Afficher les stands");
            System.out.println("3. Assigner un exposant");
            System.out.println("4. Rechercher un stand");
            System.out.println("5. Sauvegarder les données");
            System.out.println("0. Retour");
 
            int choix = lireEntier("Votre choix : ");
 
            switch (choix) {
 
                // =========================
                // 1. AJOUTER UN STAND
                // =========================
                case 1 -> {
                    int id = lireEntier("ID du stand : ");
                    String nom = lireChaine("Nom du stand : ");
                    String localisation = lireChaine("Localisation : ");
 
                    Stand stand = new Stand(id, nom, localisation);
                    standService.ajouterStand(stand);
                }
 
                // =========================
                // 2. AFFICHER LES STANDS
                // =========================
                case 2 -> standService.afficherStands();
 
                // =========================
                // 3. ASSIGNER UN EXPOSANT
                // =========================
                case 3 -> {
                    try {
                        int standId = lireEntier("ID du stand : ");
                        String exposantId = lireChaine("ID de l'exposant : ");
                        standService.assignerExposant(standId, exposantId);
                    } catch (StandNotFoundException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
 
                // =========================
                // 4. RECHERCHER UN STAND
                // =========================
                case 4 -> {
                    try {
                        int searchId = lireEntier("ID du stand : ");
                        Stand s = standService.chercherStand(searchId);
                        System.out.println(s);
                    } catch (StandNotFoundException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
 
                // =========================
                // 5. SAUVEGARDER LES DONNÉES
                // =========================
                case 5 -> {
                    standService.sauvegarder();
                    System.out.println("✔ Données sauvegardées");
                }
 
                case 0 -> retour = true;
 
                default -> System.out.println("⚠ Choix invalide.");
            }
        }
    }

    /**
     * MENU RÉSERVATIONS & FEEDBACKS
     */
    private static void menuReservations() {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n══════ GESTION DES RÉSERVATIONS & FEEDBACKS ══════");
            System.out.println("1. Créer une réservation");
            System.out.println("2. Annuler une réservation");
            System.out.println("3. Afficher les réservations d'un participant");
            System.out.println("4. Afficher les réservations d'une conférence");
            System.out.println("5. Ajouter un feedback");
            System.out.println("6. Voir moyenne des notes d'une conférence");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> ajouterReservation();
                case 2 -> annulerReservation();
                case 3 -> afficherReservationsParticipant();
                case 4 -> afficherReservationsConference();
                case 5 -> ajouterFeedback();
                case 6 -> afficherMoyenneNotes();
                case 0 -> retour = true;
                default -> System.out.println("⚠ Choix invalide.");
            }
        }
    }

    private static void ajouterReservation() {
        System.out.println("\n── Participants disponibles ──");
        service.afficherTous();
        System.out.println("\n── Conférences disponibles ──");
        if (!planningService.afficherConferencesFutures()) {
            System.out.println("✖ Aucune conférence disponible pour le moment.");
            return;
        }

        int participantId = lireEntier("ID du participant : ");
        int conferenceId  = lireEntier("ID de la conférence : ");

        try {
            Participant participant = (Participant) service.rechercherParId(participantId);
            Conference  conference  = planningService.rechercherParId(conferenceId);

            String result = reservationService.creerReservation(participant, conference);
            System.out.println("✔ Réservation créée : " + result);

        } catch (UtilisateurIntrouvableException e) {
            System.out.println("✖ Participant introuvable : " + e.getMessage());
        } catch (CapacityFullException e) {
            System.out.println("✖ Conférence complète : " + e.getMessage());
        } catch (ReservationException e) {
            System.out.println("✖ " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("✖ Cet utilisateur n'est pas un participant.");
        }
    }

    private static void annulerReservation() {
        String idReservation = lireChaine("ID de la réservation : ");
        try {
            reservationService.annulerReservation(idReservation);
            System.out.println("✔ Réservation annulée avec succès.");
        } catch (ReservationException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }

    private static void afficherReservationsParticipant() {
        int participantId = lireEntier("ID du participant : ");
        try {
            Participant participant = (Participant) service.rechercherParId(participantId);
            List<Reservation> reservations =
                    reservationService.findReservationsByParticipant(participant);
            reservations.forEach(System.out::println);
        } catch (UtilisateurIntrouvableException e) {
            System.out.println("✖ Participant introuvable : " + e.getMessage());
        } catch (ReservationException e) {
            System.out.println("✖ " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("✖ Cet utilisateur n'est pas un participant.");
        }
    }

    private static void afficherReservationsConference() {
        int conferenceId = lireEntier("ID de la conférence : ");
        try {
            Conference conference = planningService.rechercherParId(conferenceId);
            reservationService.afficherReservationsByConference(conference);
        } catch (ReservationException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }

    private static void ajouterFeedback() {
        int participantId = lireEntier("ID du participant : ");
        int conferenceId  = lireEntier("ID de la conférence : ");
        int note          = lireEntier("Note (1 à 5) : ");
        String commentaire = lireChaine("Commentaire : ");

        try {
            Participant participant = (Participant) service.rechercherParId(participantId);
            Conference  conference  = planningService.rechercherParId(conferenceId);

            Feedback feedback = new Feedback(participant, conference, note, commentaire);
            reservationService.ajouterFeedback(feedback);
            System.out.println("✔ Feedback ajouté avec succès.");

        } catch (UtilisateurIntrouvableException e) {
            System.out.println("✖ Participant introuvable : " + e.getMessage());
        } catch (ReservationException e) {
            System.out.println("✖ " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("✖ Cet utilisateur n'est pas un participant.");
        }
    }

    private static void afficherMoyenneNotes() {
        int conferenceId = lireEntier("ID de la conférence : ");
        try {
            Conference conference = planningService.rechercherParId(conferenceId);
            double moyenne = reservationService.calculerMoyenneNotes(conference);
            System.out.printf("⭐ Moyenne des notes : %.2f / 5%n", moyenne);
        } catch (ReservationException e) {
            System.out.println("✖ " + e.getMessage());
        }
    }
 
    // ════════════════════════════════════════════════════════════════════════
    //  UTILITAIRES
    // ════════════════════════════════════════════════════════════════════════
 
    private static int lireEntier(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Entier attendu → " + message);
        }
        int valeur = scanner.nextInt();
        scanner.nextLine();
        return valeur;
    }
 
    private static String lireChaine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}