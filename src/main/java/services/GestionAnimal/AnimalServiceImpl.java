package services.GestionAnimal;

import entities.GestionAnimal.Animal;
import data.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnimalServiceImpl implements IAnimalService {

    private Connection connection;
    private static final Logger logger = Logger.getLogger(AnimalServiceImpl.class.getName());

    public AnimalServiceImpl() {
        // Use the MyDataBase class to get the DB connection
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void addAnimal(Animal animal) {
        String query = "INSERT INTO animal (nom, type, race, age, poids, id_user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, animal.getNom());
            statement.setString(2, animal.getType());
            statement.setString(3, animal.getRace());
            statement.setInt(4, animal.getAge());
            statement.setFloat(5, animal.getPoids());
            statement.setInt(6, 1); // Valeur par défaut pour id_user_id

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated ID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1)); // Set the generated ID
                }
            }
        } catch (SQLException e) {
            // Si la première requête échoue, essayer sans id_user_id
            try {
                String altQuery = "INSERT INTO animal (nom, type, race, age, poids) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement altStatement = connection.prepareStatement(altQuery, Statement.RETURN_GENERATED_KEYS);
                altStatement.setString(1, animal.getNom());
                altStatement.setString(2, animal.getType());
                altStatement.setString(3, animal.getRace());
                altStatement.setInt(4, animal.getAge());
                altStatement.setFloat(5, animal.getPoids());

                int rowsAffected = altStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Retrieve the generated ID
                    ResultSet generatedKeys = altStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        animal.setId(generatedKeys.getInt(1)); // Set the generated ID
                    }
                }

                altStatement.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error adding animal (fallback method)", ex);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateAnimal(Animal updatedAnimal) {
        String query = "UPDATE animal SET nom = ?, type = ?, race = ?, age = ?, poids = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedAnimal.getNom());
            statement.setString(2, updatedAnimal.getType());
            statement.setString(3, updatedAnimal.getRace());
            statement.setInt(4, updatedAnimal.getAge());
            statement.setFloat(5, updatedAnimal.getPoids());
            statement.setInt(6, updatedAnimal.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating animal", e);
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAnimal(int id) {
        String query = "DELETE FROM animal WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting animal", e);
            e.printStackTrace();
        }
    }

    @Override
    public List<Animal> getAllAnimals() {
        List<Animal> animalList = new ArrayList<>();
        String query = "SELECT * FROM animal";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String type = resultSet.getString("type");
                String race = resultSet.getString("race");
                int age = resultSet.getInt("age");
                float poids = resultSet.getFloat("poids");

                Animal animal = new Animal(id, nom, type, age, poids, race);
                animalList.add(animal);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all animals", e);
            e.printStackTrace();
        }
        return animalList;
    }

    @Override
    public Animal getAnimalById(int id) {
        String query = "SELECT * FROM animal WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    String type = resultSet.getString("type");
                    String race = resultSet.getString("race");
                    int age = resultSet.getInt("age");
                    float poids = resultSet.getFloat("poids");
                    return new Animal(id, nom, type, age, poids, race);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving animal by ID", e);
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Long> groupAnimalsByType() {
        Map<String, Long> counts = new HashMap<>();
        String query = "SELECT type, COUNT(*) FROM animal GROUP BY type";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                String type = rs.getString("type");
                long count = rs.getLong(2);
                counts.put(type, count);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error grouping animals by type", e);
        }
        return counts;
    }

    public Double getAverageAge() {
        String query = "SELECT AVG(age) FROM animal";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error calculating average age", e);
        }
        return null;
    }

    public Map<String, Long> getRaceDistribution() {
        Map<String, Long> counts = new HashMap<>();
        String query = "SELECT race, COUNT(*) FROM animal GROUP BY race";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                String race = rs.getString("race");
                long count = rs.getLong(2);
                counts.put(race, count);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting race distribution", e);
        }
        return counts;
    }
}