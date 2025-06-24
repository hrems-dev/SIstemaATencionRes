package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.upeu.sisrestaurant.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query(value = "SELECT u.* FROM usuario u WHERE u.nombre=:nombrex", nativeQuery = true)
    Usuario buscarUsuario(@Param("nombrex") String nombrex);

    @Query(value = "SELECT u.* FROM usuario u WHERE u.nombre=:nombre AND u.password=:password", nativeQuery = true)
    Usuario loginUsuario(@Param("nombre") String nombre, @Param("password") String password);
}
