package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.InfoPersonal;
import pe.edu.upeu.sisrestaurant.repository.InfoPersonalRepository;
import pe.edu.upeu.sisrestaurant.service.InfoPersonalService;

@Service
public class InfoPersonalServiceImpl implements InfoPersonalService {

    @Autowired
    private InfoPersonalRepository infoPersonalRepository;

    @Override
    public InfoPersonal save(InfoPersonal infoPersonal) {
        return infoPersonalRepository.save(infoPersonal);
    }

    @Override
    public InfoPersonal getInfoPersonalById(String id) {
        return infoPersonalRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteInfoPersonalById(String id) {
        infoPersonalRepository.deleteById(id);
    }
}
