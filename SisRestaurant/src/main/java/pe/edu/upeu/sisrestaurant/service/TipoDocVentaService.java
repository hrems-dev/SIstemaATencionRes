package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;
import java.util.List;
import java.util.Optional;

public interface TipoDocVentaService {
    TipoDocVenta save(TipoDocVenta tipoDocVenta);
    TipoDocVenta getTipoDocVentaById(Long id);
    void deleteTipoDocVentaById(Long id);
    List<TipoDocVenta> list();
    TipoDocVenta getById(Long id);
    void delete(Long id);
    Optional<TipoDocVenta> findByNombre(String nombre);
}
