package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.Entity;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;

public interface Service<T extends Entity> {

    List<T> findAll() throws ServiceException;

}
