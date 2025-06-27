package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sisrestaurant.modelo.Personal;
import pe.edu.upeu.sisrestaurant.modelo.Rol;
import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import pe.edu.upeu.sisrestaurant.modelo.InfoPersonal;
import pe.edu.upeu.sisrestaurant.repository.PersonalRepository;
import pe.edu.upeu.sisrestaurant.service.PersonalService;
import pe.edu.upeu.sisrestaurant.service.RolService;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;
import pe.edu.upeu.sisrestaurant.service.InfoPersonalService;
import java.util.List;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PersonalRepository personalRepository;
    
    @Autowired
    private RolService rolService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private InfoPersonalService infoPersonalService;

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
    
    @Override
    public Personal findByDni(String dni) {
        return personalRepository.findByDni(dni);
    }
    
    @Override
    public List<Personal> findAllWithRelations() {
        return personalRepository.findAllWithRelations();
    }
    
    @Override
    public List<Personal> findByRolIdWithRelations(Long idRol) {
        return personalRepository.findByRolIdWithRelations(idRol);
    }
    
    @Override
    public Personal createPersonalWithRelations(Personal personal, Long idRol, Long idUser, String dni) {
        // Obtener las entidades relacionadas
        Rol rol = rolService.getRolById(idRol);
        Usuario usuario = usuarioService.findById(idUser);
        InfoPersonal infoPersonal = infoPersonalService.getInfoPersonalById(dni);
        
        if (rol == null || usuario == null || infoPersonal == null) {
            throw new IllegalArgumentException("Rol, Usuario o InfoPersonal no encontrado");
        }
        
        // Establecer las relaciones
        personal.setRol(rol);
        personal.setUsuario(usuario);
        personal.setInfoPersonal(infoPersonal);
        
        return personalRepository.save(personal);
    }
    
    @Override
    public Personal updatePersonalWithRelations(Long idPersonal, Personal personal, Long idRol, Long idUser, String dni) {
        Personal existingPersonal = personalRepository.findById(idPersonal).orElse(null);
        if (existingPersonal == null) {
            throw new IllegalArgumentException("Personal no encontrado");
        }
        
        // Obtener las entidades relacionadas
        Rol rol = rolService.getRolById(idRol);
        Usuario usuario = usuarioService.findById(idUser);
        InfoPersonal infoPersonal = infoPersonalService.getInfoPersonalById(dni);
        
        if (rol == null || usuario == null || infoPersonal == null) {
            throw new IllegalArgumentException("Rol, Usuario o InfoPersonal no encontrado");
        }
        
        // Actualizar las relaciones
        existingPersonal.setRol(rol);
        existingPersonal.setUsuario(usuario);
        existingPersonal.setInfoPersonal(infoPersonal);
        
        return personalRepository.save(existingPersonal);
    }
}
