package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.Configuracion;
import java.util.List;

public interface ConfiguracionService {
    Configuracion save(Configuracion configuracion);
    Configuracion getConfiguracionById(Long id);
    void deleteConfiguracionById(Long id);
    List<Configuracion> list();
} 