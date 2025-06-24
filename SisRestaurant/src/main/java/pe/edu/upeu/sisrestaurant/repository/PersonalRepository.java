package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.Personal;

public interface PersonalRepository extends JpaRepository<Personal, Long> {
}
