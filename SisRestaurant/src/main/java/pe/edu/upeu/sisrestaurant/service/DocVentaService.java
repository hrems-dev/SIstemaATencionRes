package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.DocVenta;
import java.util.List;

public interface DocVentaService {
    DocVenta save(DocVenta docVenta);
    DocVenta getDocVentaById(Long id);
    void deleteDocVentaById(Long id);
    List<DocVenta> list();
    List<DocVenta> listOrderByFechaDesc();
}
