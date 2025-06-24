package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
    Usuario update(Usuario usuario);
    void deleteById(Long id);
    Usuario findById(Long id);
    Usuario loginUsuario(String nombre, String password);
}
