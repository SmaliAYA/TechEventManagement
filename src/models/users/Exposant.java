package models.users;

/**
 * Représente un exposant qui tient un stand lors de l'événement.
 * Un exposant est lié à une entreprise et un secteur d'activité.
 */
public class Exposant extends Utilisateur {

    private String entreprise;
    private String secteur;

    // ─── Constructeurs ───────────────────────────────────────────────────────
    public Exposant(String nom, String prenom, String email, String motDePasse,
                    String entreprise, String secteur) {
        super(nom, prenom, email, motDePasse);
        this.entreprise = entreprise;
        this.secteur    = secteur;
    }

    // Constructeur avec ID (chargement fichier)
    public Exposant(int id, String nom, String prenom, String email, String motDePasse,
                    String entreprise, String secteur) {
        super(id, nom, prenom, email, motDePasse);
        this.entreprise = entreprise;
        this.secteur    = secteur;
    }

    // ─── Méthodes abstraites implémentées ───────────────────────────────────
    @Override
    public String getRole() {
        return "EXPOSANT";
    }

    @Override
    public String toFileString() {
        // Format : id;EXPOSANT;nom;prenom;email;motDePasse;entreprise;secteur
        return getId() + ";EXPOSANT;" + getNom() + ";" + getPrenom() + ";"
                + getEmail() + ";" + getMotDePasse() + ";" + entreprise + ";" + secteur;
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────
    public String getEntreprise()            { return entreprise; }
    public void setEntreprise(String e)      { this.entreprise = e; }
    public String getSecteur()               { return secteur; }
    public void setSecteur(String s)         { this.secteur = s; }

    @Override
    public String toString() {
        return super.toString() + " | Entreprise: " + entreprise + " | Secteur: " + secteur;
    }
}