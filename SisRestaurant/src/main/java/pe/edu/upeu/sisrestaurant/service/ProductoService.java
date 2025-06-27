package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> list();
    Producto save(Producto producto);
    Producto getProductoById(Long id);
    void deleteProductoById(Long id);
}
