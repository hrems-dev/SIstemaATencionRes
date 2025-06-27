package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;
import pe.edu.upeu.sisrestaurant.repository.TipoDocVentaRepository;
import pe.edu.upeu.sisrestaurant.service.TipoDocVentaService;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<TipoDocVenta> list() {
        return tipoDocVentaRepository.findAll();
    }

    @Override
    public TipoDocVenta getById(Long id) {
        return tipoDocVentaRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        tipoDocVentaRepository.deleteById(id);
    }

    @Override
    public Optional<TipoDocVenta> findByNombre(String nombre) {
        return tipoDocVentaRepository.findByNombre(nombre);
    }
}
