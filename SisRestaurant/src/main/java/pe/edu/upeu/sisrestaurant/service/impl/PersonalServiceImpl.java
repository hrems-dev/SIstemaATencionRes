package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.Personal;
import pe.edu.upeu.sisrestaurant.repository.PersonalRepository;
import pe.edu.upeu.sisrestaurant.service.PersonalService;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    public Personal save(Personal personal) {
        return personalRepository.save(personal);
    }

    @Override
    public Personal getPersonalById(Long id) {
        return personalRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePersonalById(Long id) {
        personalRepository.deleteById(id);
    }
}
