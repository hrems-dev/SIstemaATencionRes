package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    DetallePedido save(DetallePedido detallePedido);
    DetallePedido getDetallePedidoById(Long id);
    void deleteDetallePedidoById(Long id);
    List<DetallePedido> list();
    Optional<DetallePedido> findByIdPedidoAndIdProducto(Long idPedido, Long idProducto);
}
