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
}
