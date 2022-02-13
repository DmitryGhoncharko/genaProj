package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import by.ghoncharko.webproject.model.dao.RecipeRequestDao;
import by.ghoncharko.webproject.model.dao.RecipeRequestDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class RecipeRequestServiceImpl implements RecipeRequestService{
    private static final Logger LOG  = LogManager.getLogger(RecipeRequestServiceImpl.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private RecipeRequestServiceImpl(){

    }



    @Override
    public List<RecipeRequest> findAll() throws ServiceException {
        return null;
    }

    public static RecipeRequestServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder{
        private static final RecipeRequestServiceImpl INSTANCE = new RecipeRequestServiceImpl();
    }
}
