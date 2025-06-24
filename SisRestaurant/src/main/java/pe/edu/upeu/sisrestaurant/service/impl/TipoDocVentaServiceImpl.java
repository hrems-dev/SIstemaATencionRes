package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;
import pe.edu.upeu.sisrestaurant.repository.TipoDocVentaRepository;
import pe.edu.upeu.sisrestaurant.service.TipoDocVentaService;

@Service
public class TipoDocVentaServiceImpl implements TipoDocVentaService {

    @Autowired
    private TipoDocVentaRepository tipoDocVentaRepository;

    @Override
    public TipoDocVenta save(TipoDocVenta tipoDocVenta) {
        return tipoDocVentaRepository.save(tipoDocVenta);
    }

    @Override
    public TipoDocVenta getTipoDocVentaById(Long id) {
        return tipoDocVentaRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTipoDocVentaById(Long id) {
        tipoDocVentaRepository.deleteById(id);
    }
}
