package models.users;

/**
 * Représente un organisateur de l'événement.
 * Un organisateur peut gérer les conférences, les salles et les plannings.
 */
public class Organisateur extends Utilisateur {

    public enum Niveau { JUNIOR, SENIOR, ADMIN }

    private Niveau niveau;

    // ─── Constructeurs ───────────────────────────────────────────────────────
    public Organisateur(String nom, String prenom, String email, String motDePasse, Niveau niveau) {
        super(nom, prenom, email, motDePasse);
        this.niveau = niveau;
    }

    // Constructeur avec ID (chargement fichier)
    public Organisateur(int id, String nom, String prenom, String email, String motDePasse, Niveau niveau) {
        super(id, nom, prenom, email, motDePasse);
        this.niveau = niveau;
    }

    // ─── Méthodes abstraites implémentées ───────────────────────────────────
    @Override
    public String getRole() {
        return "ORGANISATEUR";
    }

    @Override
    public String toFileString() {
        // Format : id;ORGANISATEUR;nom;prenom;email;motDePasse;niveau
        return getId() + ";ORGANISATEUR;" + getNom() + ";" + getPrenom() + ";"
                + getEmail() + ";" + getMotDePasse() + ";" + niveau.name();
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────
    public Niveau getNiveau()            { return niveau; }
    public void setNiveau(Niveau niveau) { this.niveau = niveau; }

    @Override
    public String toString() {
        return super.toString() + " | Niveau: " + niveau;
    }
}