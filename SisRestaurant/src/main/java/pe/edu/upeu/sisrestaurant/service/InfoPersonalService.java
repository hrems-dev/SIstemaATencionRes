package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.InfoPersonal;

public interface InfoPersonalService {
    InfoPersonal save(InfoPersonal infoPersonal);
    InfoPersonal getInfoPersonalById(String id);
    void deleteInfoPersonalById(String id);
}
