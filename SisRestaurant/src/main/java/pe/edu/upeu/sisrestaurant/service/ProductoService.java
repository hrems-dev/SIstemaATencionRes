package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Producto;

public interface ProductoService {
    Producto save(Producto producto);
    Producto getProductoById(Long id);
    void deleteProductoById(Long id);
}
