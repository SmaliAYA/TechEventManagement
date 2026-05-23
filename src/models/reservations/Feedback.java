package models.reservations;

import exceptions.ReservationException;
import models.events.Conference;
import models.users.Participant;


import java.util.UUID;

public class Feedback   {
    private String id;
    private Participant participant;
    private Conference conference;
    private int note;
    private String commentaire;

    public Feedback(Participant participant, Conference conference, int note, String commentaire) throws ReservationException {
        if(participant == null) throw new ReservationException("Le participant ne peut être null");
        if(conference == null) throw new ReservationException("La conférence ne peut être null");
        if(note <= 0 || note > 5) throw new ReservationException("La note ne peut sortir de l'intervalle [1-5]");

        this.id = UUID.randomUUID().toString();
        this.participant = participant;
        this.conference = conference;
        this.note = note;
        this.commentaire = commentaire;
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

    public int getNote() {
        return note;
    }

    public void setNote(int note) throws ReservationException{

        if(note <= 0 || note > 5) throw new ReservationException("La note ne peut sortir de l'intervalle [1-5]");
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString(){
        return String.format("Feedback [%s] - Participant : %s Conférence : %s - Note : %d - Commentaire : %s",getId(),getParticipant(),getConference(),getNote()
                ,getCommentaire()==null || getCommentaire().isEmpty()? "Non mentionné":getCommentaire());
    }

}