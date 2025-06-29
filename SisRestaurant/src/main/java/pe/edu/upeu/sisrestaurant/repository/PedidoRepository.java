package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Pedido findByIdDocVenta(Long idDocVenta);
}
