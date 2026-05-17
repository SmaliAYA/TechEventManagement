package models.users;

/**
 * Représente un participant aux conférences.
 * Un participant peut réserver des conférences et laisser des feedbacks.
 */
public class Participant extends Utilisateur {

    private String organisation; // optionnel

    // ─── Constructeurs ───────────────────────────────────────────────────────
    public Participant(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.organisation = "";
    }

    public Participant(String nom, String prenom, String email, String motDePasse, String organisation) {
        super(nom, prenom, email, motDePasse);
        this.organisation = organisation;
    }

    // Constructeur avec ID (chargement fichier)
    public Participant(int id, String nom, String prenom, String email, String motDePasse, String organisation) {
        super(id, nom, prenom, email, motDePasse);
        this.organisation = organisation;
    }

    // ─── Méthodes abstraites implémentées ───────────────────────────────────
    @Override
    public String getRole() {
        return "PARTICIPANT";
    }

    @Override
    public String toFileString() {
        // Format : id;PARTICIPANT;nom;prenom;email;motDePasse;organisation
        return getId() + ";PARTICIPANT;" + getNom() + ";" + getPrenom() + ";"
                + getEmail() + ";" + getMotDePasse() + ";" + organisation;
    }

    // ─── Getter / Setter ─────────────────────────────────────────────────────
    public String getOrganisation()              { return organisation; }
    public void setOrganisation(String org)      { this.organisation = org; }

    @Override
    public String toString() {
        String base = super.toString();
        return base + (organisation.isEmpty() ? "" : " | Org: " + organisation);
    }
}