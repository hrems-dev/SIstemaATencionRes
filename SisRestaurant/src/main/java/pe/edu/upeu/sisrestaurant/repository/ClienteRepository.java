package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
