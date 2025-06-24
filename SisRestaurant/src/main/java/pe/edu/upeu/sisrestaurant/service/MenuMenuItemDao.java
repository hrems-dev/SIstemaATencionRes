package pe.edu.upeu.sisrestaurant.service;

import org.springframework.stereotype.Service;
import pe.edu.upeu.sisrestaurant.dto.MenuMenuItenTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MenuMenuItemDao implements MenuMenuItenDaoI {

    @Override
    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma) {
        List<MenuMenuItenTO> lista = new ArrayList<>();
        lista.clear();
        lista.add(new MenuMenuItenTO("Principal", "Inicio", "miinicio"));
        lista.add(new MenuMenuItenTO("Archivo", "Salir", "misalir"));
        lista.add(new MenuMenuItenTO("Producto", "Reg. Producto", "miregproduct"));
        lista.add(new MenuMenuItenTO("Producto", "Lista de Productos", "milistaproducto"));
        lista.add(new MenuMenuItenTO("Producto", "Stock", "mistockproducto"));
        lista.add(new MenuMenuItenTO("Pedido", "Reg. Pedido", "miregpedido"));
        lista.add(new MenuMenuItenTO("Pedido", "Listar Pedidos", "milistarpedidos"));
        lista.add(new MenuMenuItenTO("Pedido", "Nuevo Pedido", "minuevopedido"));
        lista.add(new MenuMenuItenTO("Venta", "Reg. Venta", "miventa"));
        lista.add(new MenuMenuItenTO("Venta", "Lista de Pedidos", "milistapedido"));
        lista.add(new MenuMenuItenTO("Venta", "Pedidos Pendientes", "mipendientepedido"));
        List<MenuMenuItenTO> accesoReal = new ArrayList<>();
        switch (perfil) {
            case "Administrador":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(1));
                accesoReal.add(lista.get(2));
                accesoReal.add(lista.get(3));
                accesoReal.add(lista.get(4));
                accesoReal.add(lista.get(5));
                accesoReal.add(lista.get(6));
                break;
            case "Root":
                accesoReal = lista;
                break;
            case "Reporte":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(5));
                accesoReal.add(lista.get(6));
                break;
            default:
                throw new AssertionError();
        }
        return accesoReal;
    }
} 