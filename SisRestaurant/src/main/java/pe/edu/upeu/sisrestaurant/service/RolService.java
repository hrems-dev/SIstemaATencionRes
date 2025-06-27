package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    Rol save(Rol rol);
    Rol getRolById(Long id);
    void deleteRolById(Long id);
    List<Rol> list();
    Optional<Rol> findByDescripcion(String descripcion);
}
