package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Pedido;
import java.util.List;

public interface PedidoService {
    Pedido save(Pedido pedido);
    Pedido getPedidoById(Long id);
    void deletePedidoById(Long id);
    List<Pedido> list();
    Pedido findByIdDocVenta(Long idDocVenta);
}
