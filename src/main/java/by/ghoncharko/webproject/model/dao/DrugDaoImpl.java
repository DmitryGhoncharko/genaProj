package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DrugDaoImpl implements DrugDao{
    private static final Logger LOG = LogManager.getLogger(DrugDaoImpl.class);
    private static final String SQL_CREATE_DRUG = "INSERT INTO drug(name, price, drug_count, description, producer_id, need_receip)" +
            " VALUES (?,?,?,?,?,?);";
    private static final String SQL_FIND_ALL_DRUGS = "SELECT" +
            " drug.id, name,price,drug_count,description,need_receip,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id";

    private static final String SQL_FIND_DRUG_BY_DRUG_ID = "SELECT drug.id, name,price,drug_count,description,need_receip,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id" +
            " WHERE drug.id = ?";
    private static final String SQL_UPDATE_DRUG_BY_DRUG_ID = "UPDATE drug SET" +
            " name = ?, price = ?, drug_count = ? , description = ?, producer_id = ? , need_receip = ? , is_deleted = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_DRUG_BY_DRUG_ID = "DELETE FROM drug WHERE id = ?";
    private final Connection connection;

    public DrugDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Drug create(Drug entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_DRUG, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setDouble(2,entity.getPrice().doubleValue());
            preparedStatement.setInt(3,entity.getCount());
            preparedStatement.setString(4,entity.getDescription());
            preparedStatement.setInt(5,entity.getProducer().getId());
            preparedStatement.setBoolean(6,entity.getNeedRecipe());
           final int countUpdatedRows = preparedStatement.executeUpdate();
           if(countUpdatedRows>0){
              final ResultSet resultSet  = preparedStatement.getGeneratedKeys();
              if(resultSet.next()){
                  return new Drug.Builder().
                          withId(resultSet.getInt(1)).
                          withName(entity.getName()).
                          withPrice(entity.getPrice()).
                          withCount(entity.getCount()).
                          withDescription(entity.getDescription()).
                          withProducer(entity.getProducer()).
                          withNeedReceip(entity.getNeedRecipe()).build();
              }
           }

        }catch (SQLException e){
            LOG.error("Cannot create drug",e);
            throw new DaoException("Cannot create drug",e);
        }
        LOG.error("Cannot create drug");
        throw new DaoException("Cannot create drug");
    }

    @Override
    public List<Drug> findAll() throws DaoException {
        final List<Drug> drugList = new ArrayList<>();
        try(final Statement statement = connection.createStatement()){
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUGS);
            while (resultSet.next()){
               final Drug drug = new Drug.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        withPrice(BigDecimal.valueOf(resultSet.getDouble(3))).
                        withCount(resultSet.getInt(4)).
                        withDescription(resultSet.getString(5)).
                        withNeedReceip(resultSet.getBoolean(6)).
                        withIsDeleted(resultSet.getBoolean(7)).
                        withProducer(new Producer.Builder().
                                withId(resultSet.getInt(8)).
                                withName(resultSet.getString(9)).build()).
                        build();
                    drugList.add(drug);
            }
        }catch (SQLException e){
            LOG.error("Cannot find all drugs",e);
            throw new DaoException("Cannot find all drugs",e);
        }
        LOG.info("Zero drugs found");
        return drugList;
    }

    @Override
    public Optional<Drug> findEntityById(Integer id) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_BY_DRUG_ID)){
            preparedStatement.setInt(1,id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
               return Optional.of(new Drug.Builder().
                       withId(resultSet.getInt(1)).
                       withName(resultSet.getString(2)).
                       withPrice(BigDecimal.valueOf(resultSet.getDouble(3))).
                       withCount(resultSet.getInt(4)).
                       withDescription(resultSet.getString(5)).
                       withNeedReceip(resultSet.getBoolean(6)).
                       withIsDeleted(resultSet.getBoolean(7)).
                       withProducer(new Producer.Builder().
                               withId(resultSet.getInt(8)).
                               withName(resultSet.getString(9)).build()).
                       build());
            }
        }catch (SQLException e){
            LOG.error("Cannot find drug by drug id",e);
            throw new DaoException("Cannot find drug by drug id",e);
        }
        LOG.info("Drug by drug id not found");
        return Optional.empty();
    }

    @Override
    public Drug update(Drug entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_DRUG_BY_DRUG_ID)){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setDouble(2,entity.getPrice().doubleValue());
            preparedStatement.setInt(3,entity.getCount());
            preparedStatement.setString(4,entity.getDescription());
            preparedStatement.setInt(5,entity.getProducer().getId());
            preparedStatement.setBoolean(6,entity.getNeedRecipe());
            preparedStatement.setBoolean(7,entity.getDeleted());
            preparedStatement.setInt(8,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if(countUpdatedRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("Cannot update drug by id", e);
            throw new DaoException("Cannot update drug by id", e);
        }
        LOG.error("Cannot update drug by id");
        throw new DaoException("Cannot update drug by id");
    }

    @Override
    public boolean delete(Drug entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_DRUG_BY_DRUG_ID)){
            preparedStatement.setInt(1,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            return countUpdatedRows>0;
        }catch (SQLException e){
            LOG.error("Cannot delete drug by drug id", e);
            throw new DaoException("Cannot delete drug by drug id", e);
        }
    }
}
