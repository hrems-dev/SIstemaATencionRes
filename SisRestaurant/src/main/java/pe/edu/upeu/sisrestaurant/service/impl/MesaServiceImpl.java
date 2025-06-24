package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.Mesa;
import pe.edu.upeu.sisrestaurant.repository.MesaRepository;
import pe.edu.upeu.sisrestaurant.service.MesaService;

@Service
public class MesaServiceImpl implements MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Override
    public Mesa save(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    @Override
    public Mesa getMesaById(Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteMesaById(Long id) {
        mesaRepository.deleteById(id);
    }
}
