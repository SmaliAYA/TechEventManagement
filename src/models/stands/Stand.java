package models.stands;

/**
 * Classe modèle qui représente un Stand dans le système.
 * Un stand appartient à un exposant et possède des informations de base.
 */
public class Stand {

    // Identifiant unique du stand
    private int id;

    // Nom du stand
    private String nom;

    // Emplacement du stand dans la salle ou le salon
    private String localisation;

    // Identifiant de l’exposant associé (peut être vide)
    private String exposantId;

    /**
     * Constructeur du Stand.
     * On initialise les informations principales du stand.
     */
    public Stand(int id, String nom, String localisation) {
        this.id = id;
        this.nom = nom;
        this.localisation = localisation;
    }

    // ================= GETTERS =================

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getExposantId() {
        return exposantId;
    }

    // ================= SETTERS =================

    /**
     * Permet d’assigner ou modifier l’exposant du stand.
     */
    public void setExposantId(String exposantId) {
        this.exposantId = exposantId;
    }

    /**
     * Représentation textuelle de l’objet Stand.
     * Utilisée pour l’affichage dans la console.
     */
    @Override
    public String toString() {
        return "Stand{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", localisation='" + localisation + '\'' +
                ", exposantId='" + exposantId + '\'' +
                '}';
    }
}