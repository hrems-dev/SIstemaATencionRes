package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sisrestaurant.modelo.Personal;
import pe.edu.upeu.sisrestaurant.modelo.Rol;
import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import pe.edu.upeu.sisrestaurant.repository.PersonalRepository;
import pe.edu.upeu.sisrestaurant.service.PersonalService;
import pe.edu.upeu.sisrestaurant.service.RolService;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;
import java.util.List;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PersonalRepository personalRepository;
    
    @Autowired
    private RolService rolService;
    
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Personal save(Personal personal) {
        return personalRepository.save(personal);
    }

    @Override
    public Personal getPersonalById(Long id) {
        return personalRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePersonalById(Long id) {
        personalRepository.deleteById(id);
    }
    
    @Override
    public List<Personal> list() {
        return personalRepository.findAll();
    }
    
    @Override
    public List<Personal> findByRolId(Long idRol) {
        return personalRepository.findByRolId(idRol);
    }
    
    @Override
    public Personal findByUsuarioId(Long idUser) {
        return personalRepository.findByUsuarioId(idUser);
    }
}
