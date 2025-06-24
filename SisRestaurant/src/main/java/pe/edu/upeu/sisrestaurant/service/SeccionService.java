package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Seccion;

public interface SeccionService {
    Seccion save(Seccion seccion);
    Seccion getSeccionById(Long id);
    void deleteSeccionById(Long id);
}
