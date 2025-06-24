package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Mesa;

public interface MesaService {
    Mesa save(Mesa mesa);
    Mesa getMesaById(Long id);
    void deleteMesaById(Long id);
}
