package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.DocVenta;

public interface DocVentaService {
    DocVenta save(DocVenta docVenta);
    DocVenta getDocVentaById(Long id);
    void deleteDocVentaById(Long id);
}
