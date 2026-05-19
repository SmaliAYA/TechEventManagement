package services;

import exceptions.CapacityFullException;
import exceptions.ReservationException;
import models.reservations.Feedback;
import models.reservations.Reservation;
import models.events.Conference;
import models.users.Participant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Service gérant la logique métier des réservations et des feedbacks.
 * Permet de créer/annuler des réservations et de gérer les feedbacks des participants.
 */
public class ReservationService {

    /* Liste de toutes les réservations du système */
    private List<Reservation> reservations;

    /* Liste de tous les feedbacks du système */
    private List<Feedback> feedbacks;

    public ReservationService() {
        this.reservations = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
    }

    // ─── Réservations ────

    public String creerReservation(Participant participant, Conference conference)
            throws ReservationException, CapacityFullException {   //Throws ReservationException  si les données sont invalides
        if (!conference.isPlaceDisponible())     //Vérifie que la conférence a encore des places disponibles.
            throw new CapacityFullException("La conférence est pleine!");
        Reservation reservation = new Reservation(participant, conference, LocalDate.now());
        reservations.add(reservation);
        return reservation.toString();
    }

    //Méthode utilitaire réutilisée par d'autres méthodes du service.
    public Reservation findReservationById(String id) throws ReservationException {
        for (Reservation res : reservations) {
            if (res.getId().equals(id)) return res;
        }
        throw new ReservationException("La réservation n'existe pas !");
    }

    // Annule une réservation existante (idReservation) en la marquant comme annulée. On ne la supprime pas de la liste pour conserver l'historique.

    public void annulerReservation(String idReservation) throws ReservationException {
        Reservation reservationTrouve = findReservationById(idReservation);
        if (reservationTrouve.isAnnulee())
            throw new ReservationException("La réservation est déjà annulée!");
        reservationTrouve.setAnnulee();
    }

    /**
     * Utile pour la modularité du code - réduit la complexité de l’implémentation des autres méthodes.
     */
    public List<Reservation> findReservationsByParticipant(Participant participant)
            throws ReservationException {
        List<Reservation> participantReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            // L'ID des utilisateurs est un entier (hérité de Utilisateur)
            if (res.getParticipant().getId() == participant.getId()) {
                participantReservations.add(res);
            }
        }
        if (participantReservations.isEmpty())
            throw new ReservationException("Le participant n'a pas de réservation!");
        return participantReservations;
    }

    public void afficherReservationsByConference(Conference conference)
            throws ReservationException {
        boolean empty = true;
        for (Reservation res : reservations) {
            if (res.getConference().getId() == conference.getId()) {
                empty = false;
                System.out.println(res);
            }
        }
        if (empty)
            throw new ReservationException("La conférence n'a pas encore de réservation!");
    }

    // ─── Feedbacks ───

    public void ajouterFeedback(Feedback feedback) throws ReservationException {
        boolean found = false;
        for (Reservation res : findReservationsByParticipant(feedback.getParticipant())) {
            // On vérifie que le participant a réservé cette conférence
            // et que la conférence est déjà passée
            if (res.getConference().getId() == feedback.getConference().getId() &&
                    res.getConference().getDate().isBefore(LocalDate.now())) {
                found = true;
                feedbacks.add(feedback);
                break;
            }
        }
        if (!found)
            throw new ReservationException("Vous n'avez pas assisté à cette conférence!");
    }

    // Méthode utilitaire réutilisée par calculerMoyenneNotes().
    public List<Feedback> findFeedbacksByConference(Conference conf)
            throws ReservationException {
        List<Feedback> conferenceFeedbacks = new ArrayList<>();
        for (Feedback fb : feedbacks) {
            if (fb.getConference().getId() == conf.getId()) {
                conferenceFeedbacks.add(fb);
            }
        }
        if (conferenceFeedbacks.isEmpty())
            throw new ReservationException("La conférence n'a aucun feedback!");
        return conferenceFeedbacks;
    }

    public double calculerMoyenneNotes(Conference conference) throws ReservationException {
        List<Feedback> confFeedbacks = findFeedbacksByConference(conference);
        int somme = 0;
        for (Feedback fb : confFeedbacks) {
            somme += fb.getNote();
        }
        // Cast en double pour éviter la division entière
        return (double) somme / confFeedbacks.size();
    }
}
package services;

import exceptions.ReservationException;
import models.reservations.Feedback;
import models.reservations.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService  {
    private List<Reservation> reservations ;
    private List<Feedback> feedbacks ;

    public ReservationService(){
        this.reservations = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
    }

    public String creerReservation(Participant participant, Conference conference) throws ReservationException, CapacityFullException {
         LocalDate dateReservation = LocalDate.now();
         if (!conference.isPlaceDisponible()) throw new CapacityFullException("La conférence est pleine!");
         Reservation reservation = new Reservation(participant,conference,dateReservation);
         reservations.add(reservation);
         return reservation.toString();
    }

    public Reservation findReservationById(String id) throws ReservationException{
        for (Reservation res : reservations){
            if (res.getId().equals(id)) return res;
        }
        throw new ReservationException("La réservation n'existe pas !");
    }

    public void annulerReservation(String idReservation) throws ReservationException{

        Reservation reservationTrouve = findReservationById(idReservation);
        if (reservationTrouve.isAnnulee()) throw new ReservationException("La réservation déjà annulée!");
        reservationTrouve.setAnnulee();
    }

    public void afficherReservationsByParticipant(Participant participant) throws ReservationException{
        boolean empty = true;
        for (Reservation res: reservations){
            if (res.getParticipant().getId().equals(participant.getId())){
                empty = false;
                System.out.println(res.toString());
            }
        }
        if (empty) throw new ReservationException("Le participant n'a pas de réservation!");
    }

}