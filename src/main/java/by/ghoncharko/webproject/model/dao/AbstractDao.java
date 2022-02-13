package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Entity;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao<T extends Entity> implements Dao<T> {
    protected final Connection connection;

    protected AbstractDao(Connection connection) {
        this.connection = connection;
    }

    protected abstract T extractEntity(ResultSet resultSet) throws SQLException;

    protected boolean deleteBillet(PreparedStatement preparedStatement, Integer id) throws SQLException {
        preparedStatement.setInt(1,id);
        final int countRowsUpdated = preparedStatement.executeUpdate();
        return countRowsUpdated>0;
    }
}
