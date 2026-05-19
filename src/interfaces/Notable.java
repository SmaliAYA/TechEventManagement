package interfaces;

import exceptions.ReservationException;
import models.reservations.Feedback;

public interface Notable {

    public void ajouterFeedback(Feedback feedback) throws ReservationException;
    public double moyenneNotes();
}