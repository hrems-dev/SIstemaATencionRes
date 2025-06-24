package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.DocVenta;
import pe.edu.upeu.sisrestaurant.repository.DocVentaRepository;
import pe.edu.upeu.sisrestaurant.service.DocVentaService;

@Service
public class DocVentaServiceImpl implements DocVentaService {

    @Autowired
    private DocVentaRepository docVentaRepository;

    @Override
    public DocVenta save(DocVenta docVenta) {
        return docVentaRepository.save(docVenta);
    }

    @Override
    public DocVenta getDocVentaById(Long id) {
        return docVentaRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDocVentaById(Long id) {
        docVentaRepository.deleteById(id);
    }
}
