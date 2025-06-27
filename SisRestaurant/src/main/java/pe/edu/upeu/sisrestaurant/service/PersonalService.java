package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Personal;
import java.util.List;

public interface PersonalService {
    Personal save(Personal personal);
    Personal getPersonalById(Long id);
    void deletePersonalById(Long id);
    
    // Métodos para manejar relaciones
    List<Personal> list();
    List<Personal> findByRolId(Long idRol);
    Personal findByUsuarioId(Long idUser);
    Personal findByDni(String dni);
    List<Personal> findAllWithRelations();
    List<Personal> findByRolIdWithRelations(Long idRol);
    
    // Métodos para crear personal con relaciones
    Personal createPersonalWithRelations(Personal personal, Long idRol, Long idUser, String dni);
    Personal updatePersonalWithRelations(Long idPersonal, Personal personal, Long idRol, Long idUser, String dni);
}
