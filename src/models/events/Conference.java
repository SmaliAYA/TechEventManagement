package models.events;

import java.time.LocalDate;

public class Conference {

    private int id;
    private String titre;
    private String theme;
    private LocalDate date;
    private String heure;
    private Salle salle;
    private int placesDisponibles;

    public Conference(int id, String titre, String theme,
                      LocalDate date, String heure,
                      Salle salle) {
        this.id = id;
        this.titre = titre;
        this.theme = theme;
        this.date = date;
        this.heure = heure;
        this.salle = salle;
        this.placesDisponibles = salle.getCapacite();
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getTheme() {
        
        return theme;
    }

    public String getHeure() {
        return heure;
    }

    public Salle getSalle() {
        return salle;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void reserverPlace() {
        if (placesDisponibles > 0) {
            placesDisponibles--;
        }
    }

    @Override
    public String toString() {
        return titre + " | " + date + " " + heure + " | " + salle.getNom();
    }
}