package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sisrestaurant.modelo.Configuracion;
import pe.edu.upeu.sisrestaurant.repository.ConfiguracionRepository;
import pe.edu.upeu.sisrestaurant.service.ConfiguracionService;
import java.util.List;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {
    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Override
    public Configuracion save(Configuracion configuracion) {
        return configuracionRepository.save(configuracion);
    }

    @Override
    public Configuracion getConfiguracionById(Long id) {
        return configuracionRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteConfiguracionById(Long id) {
        configuracionRepository.deleteById(id);
    }

    @Override
    public List<Configuracion> list() {
        return configuracionRepository.findAll();
    }
} 