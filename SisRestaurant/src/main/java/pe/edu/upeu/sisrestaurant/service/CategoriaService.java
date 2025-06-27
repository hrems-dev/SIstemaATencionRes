package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> list();
    Categoria save(Categoria categoria);
    Categoria getCategoriaById(Long id);
    void deleteCategoriaById(Long id);
    Optional<Categoria> findByNombre(String nombre);
}
