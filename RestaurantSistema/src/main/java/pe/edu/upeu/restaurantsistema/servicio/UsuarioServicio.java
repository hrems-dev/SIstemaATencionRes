package pe.edu.upeu.restaurantsistema.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.restaurantsistema.modelo.Usuario;
import pe.edu.upeu.restaurantsistema.repositorio.UsuarioRepositorio;

import java.util.List;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    // Create
    public Usuario guardarEntidad(Usuario to) {
        return usuarioRepositorio.save(to);
    }
    // Report
    public List<Usuario> listarEntidad() {
        return usuarioRepositorio.findAll();
    }
    // Update
    public Usuario actualizarEntidad(Usuario to) {
        return usuarioRepositorio.save(to);
    }
    // Delete
    public void eliminarRegEntidad(Long id) {
        usuarioRepositorio.deleteById(id);
    }
    // Buscar por ID
    public Usuario buscarEntidad(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public Usuario loginUsuario(String user, String clave) {
        // This method needs to be defined in UsuarioRepositorio
        return usuarioRepositorio.loginUsuario(user, clave);
    }

}
