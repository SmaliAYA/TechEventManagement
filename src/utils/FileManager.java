package utils;

import models.stands.Stand;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire responsable de la gestion des fichiers.
 * Elle permet de sauvegarder et charger les stands depuis un fichier texte.
 */
public class FileManager {

    // Nom du fichier où les données des stands sont stockées
    private static final String FILE_NAME = "stands.txt";

    /**
     * Sauvegarde la liste des stands dans un fichier texte.
     * Chaque stand est enregistré sur une ligne sous forme CSV.
     */
    public static void saveStands(List<Stand> stands) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Stand s : stands) {
                bw.write(
                        s.getId() + "," +
                        s.getNom() + "," +
                        s.getLocalisation() + "," +
                        (s.getExposantId() != null ? s.getExposantId() : "")
                );
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des stands");
        }
    }

    /**
     * Charge les stands depuis le fichier texte.
     * Chaque ligne du fichier est convertie en objet Stand.
     */
    public static List<Stand> loadStands() {

        List<Stand> stands = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            // Lecture du fichier ligne par ligne
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                // Création d’un objet Stand à partir des données du fichier
                Stand s = new Stand(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2]
                );

                // Si un exposant existe, on l’associe au stand
                if (data.length > 3 && !data[3].isEmpty()) {
                    s.setExposantId(data[3]);
                }

                stands.add(s);
            }

        } catch (IOException e) {
            System.out.println("Fichier stands introuvable ou vide");
        }

        return stands;
    }
}