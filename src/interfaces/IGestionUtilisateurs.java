package interfaces;

import models.users.Utilisateur;
import java.util.List;

/**
 * Interface définissant les opérations CRUD sur les utilisateurs.
 */
public interface IGestionUtilisateurs {

    void ajouterUtilisateur(Utilisateur u) throws Exception;

    void modifierUtilisateur(int id, String nom, String prenom,
                              String email, String motDePasse) throws Exception;

    void supprimerUtilisateur(int id) throws Exception;

    Utilisateur rechercherParId(int id) throws Exception;

    List<Utilisateur> rechercherParNom(String nom);

    Utilisateur rechercherParEmail(String email) throws Exception;

    List<Utilisateur> afficherTous();

    List<Utilisateur> afficherParRole(String role);
}