import exceptions.EmailDejaUtiliseException;
import exceptions.UtilisateurIntrouvableException;
import models.users.*;
import services.UtilisateurService;

import java.util.Scanner;

/**
 * Point d'entrée principal — Menu interactif de gestion des utilisateurs.
 * (À intégrer avec les autres modules au fur et à mesure)
 */
public class Main {

    private static final UtilisateurService service = new UtilisateurService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   Gestion des Utilisateurs       ║");
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
                case 6 -> afficherParRole();
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