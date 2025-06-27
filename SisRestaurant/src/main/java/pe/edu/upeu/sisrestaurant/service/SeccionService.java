package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Seccion;
import java.util.List;
import java.util.Optional;

public interface SeccionService {
    List<Seccion> list();
    Seccion save(Seccion seccion);
    Seccion getSeccionById(Long id);
    void deleteSeccionById(Long id);
    Optional<Seccion> findByNombre(String nombre);
}
