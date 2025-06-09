package services.GestionAnimal;

import entities.GestionAnimal.Veterinaire;
import java.util.List;

public interface VeterinaireService {
    void addVeterinaire(Veterinaire v);
    void updateVeterinaire(Veterinaire v);
    void deleteVeterinaire(int id);
    List<Veterinaire> getAllVeterinaires();
    Veterinaire getVeterinaireById(int id);
}
