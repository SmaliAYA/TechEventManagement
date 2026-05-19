package interfaces;

import exceptions.ReservationException;
import exceptions.CapacityFullException;
import models.users.Participant;

public interface Reservable {
    public void reserver(Participant participant) throws CapacityFullException;
    public void annulerReservation(String idReservation) throws ReservationException;
    public boolean isPlaceDisponible();
}
