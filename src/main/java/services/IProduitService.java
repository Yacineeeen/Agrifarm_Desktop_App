package services;
import entities.GestionVente.Produit;
import java.util.List;

public interface IProduitService<T> {

    void add(Produit produit);
    void update(Produit produit);
    void delete(Produit produit);
    List<Produit> getAll();
    Produit getById(int id);
}


