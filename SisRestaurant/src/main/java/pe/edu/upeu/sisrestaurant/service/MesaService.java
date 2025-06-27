package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Mesa;
import java.util.List;
import java.util.Optional;

public interface MesaService {
    Mesa save(Mesa mesa);
    Mesa getMesaById(Long id);
    void deleteMesaById(Long id);
    List<Mesa> list();
    Optional<Mesa> findByNumero(int numero);
}
