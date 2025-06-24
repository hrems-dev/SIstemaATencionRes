package pe.edu.upeu.sisrestaurant.service;

import java.util.List;
import java.util.Properties;

import pe.edu.upeu.sisrestaurant.dto.MenuMenuItenTO;

public interface MenuMenuItenDaoI {
    List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma);
} 