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