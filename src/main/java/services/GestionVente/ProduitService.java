package services.GestionVente;


import entities.GestionVente.Produit;
import services.IProduitService;
import data.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class   ProduitService implements IProduitService<Produit> {

    private Connection cnx;

    public ProduitService() {
        cnx = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Produit produit) {
        // ANCIEN CODE :
        // String sql = "INSERT INTO produit (type, reference, nom, description, prix, stock) VALUES (?, ?, ?, ?, ?, ?)";

        // NOUVEAU CODE (avec id_user_id) :
        String sql = "INSERT INTO produit (type, reference, nom, description, prix, stock, id_user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, produit.getType());
            pst.setString(2, produit.getReference());
            pst.setString(3, produit.getNom());
            pst.setString(4, produit.getDescription());
            pst.setFloat(5, produit.getPrix());
            pst.setFloat(6, produit.getStock());

            // NOUVELLE LIGNE : Ajouter l'ID utilisateur (1 par défaut ou utilisez l'ID actuel si disponible)
            pst.setInt(7, 1); // Remplacez 1 par l'ID de l'utilisateur actuel si nécessaire

            pst.executeUpdate();  // Execute the insert statement

            // Retrieve the generated id
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);  // The generated 'id' of the inserted record
                    produit.setId(generatedId);  // Set the generated id to the Produit object
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout produit: " + e.getMessage());
            e.printStackTrace(); // Ajoutez cette ligne pour plus de détails sur l'erreur
        }
    }



    public void update(Produit produit) {
        // SQL query without the 'image' column
        String sql = "UPDATE produit SET type = ?, reference = ?, nom = ?, description = ?, prix = ?, stock = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            // Set the parameters for each column
            pst.setString(1, produit.getType());
            pst.setString(2, produit.getReference());
            pst.setString(3, produit.getNom());
            pst.setString(4, produit.getDescription());
            pst.setFloat(5, produit.getPrix());
            pst.setFloat(6, produit.getStock());

            // Set the id to match the product to update
            pst.setInt(7, produit.getId());

            // Execute the update
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating produit: " + e.getMessage());
        }
    }



    public void delete(Produit produit) {
        String sql = "DELETE FROM produit WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, produit.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting produit: " + e.getMessage());
        }
    }

    @Override
    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getFloat("prix"),
                        rs.getInt("stock")

                );
                produit.setImagePath(rs.getString("image"));
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving produits: " + e.getMessage());
        }

        return produits;
    }

    @Override
    public Produit getById(int id) {
        String sql = "SELECT * FROM produit WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Produit(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getString("reference"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getFloat("prix"),
                            rs.getInt("stock")

                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving produit: " + e.getMessage());
        }
        return null;
    }
}
