package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.Rol;
import pe.edu.upeu.sisrestaurant.repository.RolRepository;
import pe.edu.upeu.sisrestaurant.service.RolService;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol getRolById(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRolById(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public List<Rol> list() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findByDescripcion(String descripcion) {
        return rolRepository.findByDescripcion(descripcion);
    }
}
