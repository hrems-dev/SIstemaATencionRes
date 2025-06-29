package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Personal;
import java.util.List;

public interface PersonalService {
    Personal save(Personal personal);
    Personal getPersonalById(Long id);
    void deletePersonalById(Long id);
    List<Personal> list();
    List<Personal> findByRolId(Long idRol);
    Personal findByUsuarioId(Long idUser);
}
