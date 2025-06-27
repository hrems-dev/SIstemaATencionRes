package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.sisrestaurant.modelo.Personal;
import java.util.List;

public interface PersonalRepository extends JpaRepository<Personal, Long> {
    
    // Buscar personal por rol
    @Query("SELECT p FROM Personal p WHERE p.rol.idRol = :idRol")
    List<Personal> findByRolId(@Param("idRol") Long idRol);
    
    // Buscar personal por usuario
    @Query("SELECT p FROM Personal p WHERE p.usuario.iduser = :idUser")
    Personal findByUsuarioId(@Param("idUser") Long idUser);
    
    // Buscar personal por DNI
    @Query("SELECT p FROM Personal p WHERE p.infoPersonal.dni = :dni")
    Personal findByDni(@Param("dni") String dni);
    
    // Buscar personal con todas las relaciones cargadas
    @Query("SELECT p FROM Personal p LEFT JOIN FETCH p.rol LEFT JOIN FETCH p.usuario LEFT JOIN FETCH p.infoPersonal")
    List<Personal> findAllWithRelations();
    
    // Buscar personal por rol con relaciones cargadas
    @Query("SELECT p FROM Personal p LEFT JOIN FETCH p.rol LEFT JOIN FETCH p.usuario LEFT JOIN FETCH p.infoPersonal WHERE p.rol.idRol = :idRol")
    List<Personal> findByRolIdWithRelations(@Param("idRol") Long idRol);
}
