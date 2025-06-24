package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.Seccion;
import pe.edu.upeu.sisrestaurant.repository.SeccionRepository;
import pe.edu.upeu.sisrestaurant.service.SeccionService;

@Service
public class SeccionServiceImpl implements SeccionService {

    @Autowired
    private SeccionRepository seccionRepository;

    @Override
    public Seccion save(Seccion seccion) {
        return seccionRepository.save(seccion);
    }

    @Override
    public Seccion getSeccionById(Long id) {
        return seccionRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSeccionById(Long id) {
        seccionRepository.deleteById(id);
    }
}
