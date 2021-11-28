package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DrugDaoImpl extends AbstractDao<Drug> {
    private static final Logger LOG = LogManager.getLogger(DrugDaoImpl.class);
    private static final String SQL_CREATE_DRUG = "INSERT INTO drug" +
            " (name, price, count, description, producer_id, need_receip)" +
            " VALUES (?,?,?,?,?,?)";
    private static final String SQL_FIND_ALL_DRUGS = "SELECT" +
            " drug.id, name, price, count, description, producer.id, producer_name, need_receip" +
            " FROM drug" +
            " INNER JOIN producer ON producer_id=producer.id";
    private static final String SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO = "SELECT" +
            " drug.id, name, price, count, description, producer.id, producer_name, need_receip" +
            " FROM drug" +
            " INNER JOIN producer ON producer_id=producer.id" +
            " WHERE count > 0";
    private static final String SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO_BY_USER_ID = "SELECT DISTINCT" +
            " drug.id, name, price, drug.count- IFNULL(d.count,0), description, producer.id, producer_name, need_receip" +
            " FROM drug" +
            " INNER JOIN producer ON producer_id=producer.id " +
            " LEFT JOIN drug_order d on ? = d.user_id and drug.id = d.drug_id and d.status_id = 1" +
            " WHERE drug.count>0";
    private static final String SQL_FIND_DRUG_BY_ID = "SELECT" +
            " drug.id, name, price, count, description, producer.id, producer_name, need_receip" +
            " FROM drug " +
            " INNER JOIN producer ON producer_id=producer.id " +
            " WHERE drug.id = ?";
    private static final String SQL_UPDATE_DRUG = "UPDATE drug" +
            " SET name = ?, price = ?, count = ?, description = ?, producer_id =?, need_receip = ?" +
            " WHERE id = ?";
    private static final String SQL_UPDATE_DRUG_COUNT_BY_DRUG_ID = "UPDATE drug set" +
            " count = ?" +
            " WHERE id = ?";
    private static final String SQL_GET_DRUG_COUNT_BY_DRUG_ID = "SELECT count from drug where id = ?";
    private static final String SQL_DELETE_DRUG = "DELETE FROM drug WHERE id=?";


    public DrugDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Drug create(Drug entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_DRUG, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDouble(2, entity.getPrice());
            preparedStatement.setInt(3, entity.getCount());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getProducer().getId());
            preparedStatement.setBoolean(6, entity.isNeedRecipe());

            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (countRows > 0 & resultSet.next()) {
                return new Drug.Builder().
                        withId(resultSet.getInt(1)).
                        withName(entity.getName()).
                        withPrice(entity.getPrice()).
                        withCount(entity.getCount()).
                        withDescription(entity.getDescription()).
                        withProducer(entity.getProducer()).
                        withNeedReceip(entity.isNeedRecipe()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create drug", e);
            throw new DaoException("cannot create drug", e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot create drug");
        throw new DaoException();
    }

    @Override
    public List<Drug> findAll() throws DaoException {
        List<Drug> drugList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUGS);
            while (resultSet.next()) {
                Drug drug = new Drug.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        withPrice(resultSet.getDouble(3)).
                        withCount(resultSet.getInt(4)).
                        withDescription(resultSet.getString(5)).
                        withProducer(new Producer.Builder().
                                withId(resultSet.getInt(6)).
                                withName(resultSet.getString(7)).
                                build()).
                        withNeedReceip(resultSet.getBoolean(8)).
                        build();
                drugList.add(drug);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all drugs", e);
            throw new DaoException("cannot find all drugs", e);
        } finally {
            close(statement);
        }
        return drugList;
    }
    public List<Drug> findAllWhereCountMoreThanZero() throws DaoException {
        List<Drug> drugList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO);
            while (resultSet.next()) {
                Drug drug = new Drug.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        withPrice(resultSet.getDouble(3)).
                        withCount(resultSet.getInt(4)).
                        withDescription(resultSet.getString(5)).
                        withProducer(new Producer.Builder().
                                withId(resultSet.getInt(6)).
                                withName(resultSet.getString(7)).
                                build()).
                        withNeedReceip(resultSet.getBoolean(8)).
                        build();
                drugList.add(drug);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all drugs", e);
            throw new DaoException("cannot find all drugs", e);
        } finally {
            close(statement);
        }
        return drugList;
    }
    public List<Drug> findAllWhereCountMoreThanZeroWithStatusActiveByUserId(Integer userId) throws DaoException {
        List<Drug> drugList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO_BY_USER_ID);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Drug drug = new Drug.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        withPrice(resultSet.getDouble(3)).
                        withCount(resultSet.getInt(4)).
                        withDescription(resultSet.getString(5)).
                        withProducer(new Producer.Builder().
                                withId(resultSet.getInt(6)).
                                withName(resultSet.getString(7)).
                                build()).
                        withNeedReceip(resultSet.getBoolean(8)).
                        build();
                drugList.add(drug);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all drugs", e);
            throw new DaoException("cannot find all drugs", e);
        } finally {
            close(preparedStatement);
        }
        return drugList;
    }

    @Override
    public Optional<Drug> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Drug.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        withPrice(resultSet.getDouble(3)).
                        withCount(resultSet.getInt(4)).
                        withDescription(resultSet.getString(5)).
                        withProducer(new Producer.Builder().
                                withId(resultSet.getInt(6)).
                                withName(resultSet.getString(7)).
                                build()).
                        withNeedReceip(resultSet.getBoolean(8)).
                        build());
            }
        } catch (SQLException e) {
            LOG.error("cannot find drug by id", e);
            throw new DaoException("cannot find drug by id", e);
        } finally {
            close(preparedStatement);
        }
        return Optional.empty();
    }

    public Integer getCountByDrugId(Integer drugId) throws DaoException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_DRUG_COUNT_BY_DRUG_ID);
            preparedStatement.setInt(1, drugId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            LOG.error("Exception in method getCountByDrugId", e);
            throw new DaoException("Exception in method getCountByDrugId", e);
        } finally {
            close(preparedStatement);
        }
        throw new DaoException("Exception in method getCountByDrugId");
    }

    @Override
    public Drug update(Drug entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_DRUG);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDouble(2, entity.getPrice());
            preparedStatement.setInt(3, entity.getCount());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getProducer().getId());
            preparedStatement.setBoolean(6, entity.isNeedRecipe());
            preparedStatement.setInt(7, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update drug", e);
            throw new DaoException("cannot update drug", e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot update drug");
        throw new DaoException();
    }

    public boolean update(Integer count, Integer drugId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_DRUG_COUNT_BY_DRUG_ID);
            preparedStatement.setInt(1, count);
            preparedStatement.setInt(2, drugId);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot update drug", e);
            throw new DaoException("cannot update drug", e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot update drug");
        throw new DaoException();
    }

    @Override
    public boolean delete(Drug entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_DRUG);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete drug", e);
            throw new DaoException("cannot delete drug", e);
        } finally {
            close(preparedStatement);
        }
        return false;
    }

}

