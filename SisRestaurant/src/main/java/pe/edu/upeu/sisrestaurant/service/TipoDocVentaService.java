package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;

public interface TipoDocVentaService {
    TipoDocVenta save(TipoDocVenta tipoDocVenta);
    TipoDocVenta getTipoDocVentaById(Long id);
    void deleteTipoDocVentaById(Long id);
}
