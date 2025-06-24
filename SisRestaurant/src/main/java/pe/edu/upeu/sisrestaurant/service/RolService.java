package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Rol;

public interface RolService {
    Rol save(Rol rol);
    Rol getRolById(Long id);
    void deleteRolById(Long id);
}
