package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.Pedido;
import pe.edu.upeu.sisrestaurant.repository.PedidoRepository;
import pe.edu.upeu.sisrestaurant.service.PedidoService;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePedidoById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<Pedido> list() {
        return pedidoRepository.findAll();
    }
}
