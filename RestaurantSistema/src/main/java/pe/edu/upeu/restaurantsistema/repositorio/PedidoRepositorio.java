package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.Pedido;
import java.util.Date;
import java.util.List;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    List<Pedido> findByFechaHoraBetween(Date fechaInicio, Date fechaFin);
    List<Pedido> findByMesaId(Long mesaId);
    List<Pedido> findByClienteId(Long clienteId);
}
