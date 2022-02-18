package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.Entity;
import by.ghoncharko.webproject.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Service<T extends Entity> {
    Logger LOG  = LogManager.getLogger(Service.class);
    List<T> findAll() throws ServiceException;



}
