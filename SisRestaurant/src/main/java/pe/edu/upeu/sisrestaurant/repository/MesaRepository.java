package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.Mesa;

import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByNumero(int numero);
}
