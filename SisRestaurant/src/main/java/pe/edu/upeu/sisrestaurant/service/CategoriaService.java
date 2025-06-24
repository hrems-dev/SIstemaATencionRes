package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Categoria;

public interface CategoriaService {
    Categoria save(Categoria categoria);
    Categoria getCategoriaById(Long id);
    void deleteCategoriaById(Long id);
}
