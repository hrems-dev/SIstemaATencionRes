package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.DocVenta;
import java.util.Date;
import java.util.List;

public interface DocVentaRepositorio extends JpaRepository<DocVenta, Long> {
    List<DocVenta> findByFechaHoraBetween(Date fechaInicio, Date fechaFin);
    DocVenta findByCodigoVenta(String codigoVenta);
}
