package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sisrestaurant.modelo.Configuracion;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    // Puedes agregar métodos personalizados aquí si lo necesitas
} 