package models.users;

/**
 * Classe abstraite représentant un utilisateur du système.
 * Toutes les classes concrètes (Participant, Exposant, Organisateur) héritent de cette classe.
 */
public abstract class Utilisateur {

    // Compteur statique pour générer des IDs uniques
    private static int compteur = 1;

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    // ─── Constructeur normal (nouvel utilisateur) ───────────────────────────
    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.id = compteur++;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // ─── Constructeur avec ID (chargement depuis fichier) ───────────────────
    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        if (id >= compteur) compteur = id + 1;
    }

    // ─── Méthodes abstraites (chaque sous-classe les implémente) ────────────
    public abstract String getRole();

    /**
     * Retourne une ligne formatée pour sauvegarder dans users.txt
     * Format : id;ROLE;nom;prenom;email;motDePasse;[champs spécifiques]
     */
    public abstract String toFileString();

    // ─── Getters ────────────────────────────────────────────────────────────
    public int getId()           { return id; }
    public String getNom()       { return nom; }
    public String getPrenom()    { return prenom; }
    public String getEmail()     { return email; }
    public String getMotDePasse(){ return motDePasse; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setNom(String nom)             { this.nom = nom; }
    public void setPrenom(String prenom)       { this.prenom = prenom; }
    public void setEmail(String email)         { this.email = email; }
    public void setMotDePasse(String mdp)      { this.motDePasse = mdp; }

    // ─── Réinitialiser le compteur (utile pour les tests) ───────────────────
    public static void resetCompteur(int valeur) { compteur = valeur; }

    @Override
    public String toString() {
        return String.format("%-14s | ID: %-3d | %-20s | Email: %s",
                "[" + getRole() + "]", id, prenom + " " + nom, email);
    }
}