package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.DetallePedido;

public interface DetallePedidoService {
    DetallePedido save(DetallePedido detallePedido);
    DetallePedido getDetallePedidoById(Long id);
    void deleteDetallePedidoById(Long id);
}
