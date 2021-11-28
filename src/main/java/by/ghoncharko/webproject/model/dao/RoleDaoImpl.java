package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleDaoImpl implements Dao<Role> {
    private static final Logger LOG = LogManager.getLogger(RecipeDaoImpl.class);
    private static final String SQL_CREATE_ROLE = "INSERT INTO role (id,role_name) VALUES(?,?)";
    private static final String SQL_FIND_ALL_ROLES = "SELECT id, role_name FROM role";
    private static final String SQL_FIND_ROLE_BY_ID = "SELECT role_name FROM role" +
            " WHERE id = ?";
    private static final String SQL_UPDATE_ROLE = "UPDATE role SET role_name = ? WHERE id = ?";
    private static final String SQL_DELETE_ROLE = "DELETE FROM role WHERE id = ?";
    private final Connection connection;
    private RoleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Role create(Role entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_CREATE_ROLE);
            preparedStatement.setInt(1,entity.getId());
            preparedStatement.setString(2,entity.getRoleName());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return new Role.Builder().
                        withId(entity.getId()).
                        withRoleName(entity.getRoleName()).
                        build();
            }
        }catch (SQLException e){
            LOG.error("cannot cerate role",e);
            throw new DaoException("cannot cerate role",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot cerate role");
        throw new DaoException();
    }

    @Override
    public List<Role> findAll() throws DaoException {
        List<Role> roleList = new ArrayList<>();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_ROLES);
            while (resultSet.next()){
                Role role = new Role.Builder().
                        withId(resultSet.getInt(1)).
                        withRoleName(resultSet.getString(2)).
                        build();
                roleList.add(role);
            }
        }catch (SQLException e){
            LOG.error("cannot find all roles",e);
            throw new DaoException("cannot find all roles",e);
        }finally {
            Dao.closeStatement(statement);
        }
        return   roleList;
    }

    @Override
    public Optional<Role> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement  = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_FIND_ROLE_BY_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new Role.Builder().
                        withId(id).
                        withRoleName(resultSet.getString(1)).
                        build());
            }
        }catch (SQLException e){
            LOG.error("cannot find role by id",e);
            throw new DaoException("cannot find role by id",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public Role update(Role entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement= connection.prepareStatement(SQL_UPDATE_ROLE);
            preparedStatement.setString(1,entity.getRoleName());
            preparedStatement.setInt(2,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("cannot update entity",e);
            throw new DaoException("cannot update entity",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot update entity");
        throw new DaoException();
    }

    @Override
    public boolean delete(Role entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_DELETE_ROLE);
            preparedStatement.setInt(1,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return true;
            }
        }catch (SQLException e){
            LOG.error("cannot delete role",e);
            throw new DaoException("cannot delete role",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }
}
