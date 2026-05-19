package models.reservations;

import exceptions.ReservationException;
import models.users.Participant;
import models.events.Conference;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation {
    private String id;
    private Participant participant;
    private Conference conference;
    private LocalDate dateReservation;
    private boolean annulee;

    public Reservation(Participant participant, Conference conference, LocalDate dateReservation) throws ReservationException{

        if(participant == null) throw new ReservationException("Le participant ne peut pas être null");
        if(conference == null) throw new ReservationException("La conférence ne peut pas être null");
        if(dateReservation == null || dateReservation.isBefore(LocalDate.now())) throw new ReservationException("La date ne peut pas être ni dans le passé ni null");

        this.id = UUID.randomUUID().toString();
        this.participant= participant;
        this.conference = conference;
        this.dateReservation = dateReservation;
        this.annulee = false;
    }

    public String getId() {
        return id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public boolean isAnnulee() {
        return annulee;
    }

    public void setAnnulee() {
        this.annulee = true;
    }

    @Override
    public String toString(){
        return String.format("Réservation : %s - Participant : %s - Conférence : %s - Date : %s - Statut : %s",getId(),getParticipant(),getConference(),getDateReservation(),isAnnulee()? "Annulée":"Active");
    }
}
