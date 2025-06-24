package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.DetallePedido;
import pe.edu.upeu.sisrestaurant.repository.DetallePedidoRepository;
import pe.edu.upeu.sisrestaurant.service.DetallePedidoService;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Override
    public DetallePedido save(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public DetallePedido getDetallePedidoById(Long id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDetallePedidoById(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}
