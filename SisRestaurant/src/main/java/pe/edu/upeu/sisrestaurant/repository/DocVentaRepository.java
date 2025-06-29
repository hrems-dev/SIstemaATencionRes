package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pe.edu.upeu.sisrestaurant.modelo.DocVenta;
import java.util.List;

public interface DocVentaRepository extends JpaRepository<DocVenta, Long> {
    
    @Query("SELECT dv FROM DocVenta dv ORDER BY dv.id DESC")
    List<DocVenta> findAllOrderByFechaDesc();
}
