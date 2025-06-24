package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Cliente;

public interface ClienteService {
    Cliente save(Cliente cliente);
    Cliente getClienteById(Long id);
    void deleteClienteById(Long id);
}
