package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Personal;

public interface PersonalService {
    Personal save(Personal personal);
    Personal getPersonalById(Long id);
    void deletePersonalById(Long id);
}
