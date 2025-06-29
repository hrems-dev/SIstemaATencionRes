package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sisrestaurant.modelo.DetallePedido;
import java.util.Optional;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    Optional<DetallePedido> findByIdPedidoAndIdProducto(Long idPedido, Long idProducto);
}
