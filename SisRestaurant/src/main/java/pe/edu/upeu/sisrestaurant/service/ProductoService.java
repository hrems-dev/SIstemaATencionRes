package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> list();
    Producto save(Producto producto);
    Producto getProductoById(Long id);
    void deleteProductoById(Long id);
    Optional<Producto> findByNombre(String nombre);
}
