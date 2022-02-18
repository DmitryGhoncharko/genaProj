package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DrugDaoImpl extends AbstractDao<Drug> implements DrugDao {
    private static final Logger LOG = LogManager.getLogger(DrugDaoImpl.class);
    private static final String SQL_CREATE_DRUG = "INSERT INTO drug(name, price, drug_count, description, producer_id, need_recipe)" +
            " VALUES (?,?,?,?,?,?);";
    private static final String SQL_FIND_ALL_DRUGS = "SELECT" +
            " drug.id, name,price,drug_count,description,need_recipe,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id";

    private static final String SQL_FIND_DRUG_BY_DRUG_ID = "SELECT drug.id, name,price,drug_count,description,need_recipe,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id" +
            " WHERE drug.id = ?";
    private static final String SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO_AND_CALCULATE_COUNT_WITH_COUNT_IN_ORDER_AND_DRUG_IS_NOT_DELETED_LIMIT_OFFSET_PAGINATION = "SELECT drug.id as dr, name,price,drug_count-ifnull((SELECT drug_count from drug_user_order" +
            "    inner join user_order uo on drug_user_order.user_order_id = uo.id" +
            "    left join paid_user_order puo on uo.id = puo.user_order_id" +
            "    where dr = drug_user_order.drug_id and user_id = ? and puo.id is null" +
            "    ),0),description,need_recipe,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id" +
            " WHERE drug.drug_count > 0 AND drug.is_deleted = false" +
            " LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO_AND_DRUG_IS_NOT_DELETED = "SELECT drug.id as dr, name,price,drug_count,description,need_recipe,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id" +
            " WHERE drug.drug_count > 0 AND drug.is_deleted = false" +
            " LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_DRUGS_COUNT_WHERE_COUNT_MORE_THAN_ZERO_AND_DRUG_IS_NOT_DELETED = "SELECT COUNT(drug.id)" +
            " FROM drug" +
            " WHERE drug.drug_count > 0 AND drug.is_deleted = false";
    private static final String SQL_FIND_DRUG_BY_DRUG_ID_WHERE_COUNT_MORE_THAN_ZERO_WITH_CALCULATE_COUNT_FROM_USER_ORDER_AND_DRUG_DONT_DELETED = "SELECT drug.id as dr, name,price,drug_count-ifnull((SELECT drug_count from drug_user_order" +
            " inner join user_order uo on drug_user_order.user_order_id = uo.id" +
            " left join paid_user_order puo on uo.id = puo.user_order_id" +
            " where dr = drug_user_order.drug_id and user_id = ? and puo.id is null" +
            " ),0),description,need_recipe,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id" +
            " WHERE drug.id = ? AND drug_count>0 AND is_deleted=false";
    private static final String SQL_UPDATE_DRUG_BY_DRUG_ID = "UPDATE drug SET" +
            " name = ?, price = ?, drug_count = ? , description = ?, producer_id = ? , need_recipe = ? , is_deleted = ?" +
            " WHERE id = ?";
    private static final String SQL_FIND_ALL_DRUGS_LIMIT_OFFSET_PAGINATION = "SELECT" +
            " drug.id, name,price,drug_count,description,need_recipe,is_deleted, p.id, p.producer_name" +
            " FROM drug" +
            " INNER JOIN producer p ON drug.producer_id = p.id" +
            " LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_DRUGS_COUNT = "SELECT COUNT(drug.id) FROM drug";
    private static final String SQL_DELETE_DRUG_BY_DRUG_ID = "DELETE FROM drug WHERE id = ?";

    public DrugDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Drug create(Drug entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_DRUG, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDouble(2, entity.getPrice().doubleValue());
            preparedStatement.setInt(3, entity.getCount());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getProducer().getId());
            preparedStatement.setBoolean(6, entity.getNeedRecipe());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
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

        } catch (SQLException e) {
            LOG.error("Cannot create drug", e);
            throw new DaoException("Cannot create drug", e);
        }
        LOG.error("Cannot create drug");
        throw new DaoException("Cannot create drug");
    }

    @Override
    public List<Drug> findAllDrugsLimitOffsetPagination(Integer limit, Integer offset) throws DaoException {
        final List<Drug> drugList = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_DRUGS_LIMIT_OFFSET_PAGINATION)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final Drug drug = extractEntity(resultSet);
                drugList.add(drug);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all drugs", e);
            throw new DaoException("Cannot find all drugs", e);
        }
        return drugList;
    }

    @Override
    public int findCountAllDrugCount() throws DaoException {
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUGS_COUNT);
            if (resultSet.next()) {
                final int countRows = resultSet.getInt(1);
                if (countRows > 0) {
                    return countRows;
                }
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all drugs count", e);
            throw new DaoException("Cannot find all drugs count", e);
        }
        LOG.info("Zero drugs was found");
        throw new DaoException("Zero drugs was found");
    }

    @Override
    public int findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted() throws DaoException {
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUGS_COUNT_WHERE_COUNT_MORE_THAN_ZERO_AND_DRUG_IS_NOT_DELETED);
            if (resultSet.next()) {
                final int countRows = resultSet.getInt(1);
                LOG.debug("countRows ", countRows);
                if (countRows > 0) {
                    return countRows;
                }
            }
        } catch (SQLException e) {
            LOG.error("Cannot find max count rows for all drug where count more than zero and drug is not deleted", e);
            throw new DaoException("Cannot find max count rows for all drug where count more than zero and drug is not deleted", e);
        }
        LOG.error("Not found rows");
        throw new DaoException("Not found rows");
    }

    @Override
    public List<Drug> findAllDrugsWhereCountMoreThanZeroAndCalculateCountWithCountInOrderAndDrugIsNotDeletedWithLimitOffsetPagination(Integer userId, Integer limit, Integer offset) throws DaoException {
        final List<Drug> drugList = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO_AND_CALCULATE_COUNT_WITH_COUNT_IN_ORDER_AND_DRUG_IS_NOT_DELETED_LIMIT_OFFSET_PAGINATION)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final Drug drug = extractEntity(resultSet);
                drugList.add(drug);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all drugs where count drug more than zero and drug is not deleted", e);
            throw new DaoException("Cannot find all drugs where count drug more than zero and drug is not deleted", e);
        }
        return drugList;
    }

    @Override
    public List<Drug> findAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeletedLimitOffsetPagination(Integer limit, Integer offset) throws DaoException {
        final List<Drug> drugList = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_DRUGS_WHERE_COUNT_MORE_THAN_ZERO_AND_DRUG_IS_NOT_DELETED)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final Drug drug = extractEntity(resultSet);
                drugList.add(drug);
            }
        } catch (SQLException e) {

        }
        return drugList;
    }

    @Override
    public List<Drug> findAll() throws DaoException {
        final List<Drug> drugList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUGS);
            while (resultSet.next()) {
                final Drug drug = extractEntity(resultSet);
                drugList.add(drug);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all drugs", e);
            throw new DaoException("Cannot find all drugs", e);
        }
        LOG.info("Zero drugs found");
        return drugList;
    }

    @Override
    public Optional<Drug> findEntityById(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_BY_DRUG_ID)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find drug by drug id", e);
            throw new DaoException("Cannot find drug by drug id", e);
        }
        LOG.info("Drug by drug id not found");
        return Optional.empty();
    }

    @Override
    public Optional<Drug> findDrugByDrugIdWhereCountMoreThanZeroAndCalculateCountFromUserOrderAndDrugDontDeleted(Integer userId, Integer drugId) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_BY_DRUG_ID_WHERE_COUNT_MORE_THAN_ZERO_WITH_CALCULATE_COUNT_FROM_USER_ORDER_AND_DRUG_DONT_DELETED)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, drugId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find drug where count more than zero and dont deleted by drug id", e);
            throw new DaoException("Cannot find drug where count more than zero and dont deleted by drug id", e);
        }
        LOG.info("Cannot find drug where count more than zero and dont deleted by drug id");
        return Optional.empty();
    }

    @Override
    public Drug update(Drug entity) throws DaoException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_DRUG_BY_DRUG_ID)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDouble(2, entity.getPrice().doubleValue());
            preparedStatement.setInt(3, entity.getCount());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getProducer().getId());
            preparedStatement.setBoolean(6, entity.getNeedRecipe());
            preparedStatement.setBoolean(7, entity.getDeleted());
            preparedStatement.setInt(8, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update drug by id", e);
            throw new DaoException("Cannot update drug by id", e);
        }
        LOG.error("Cannot update drug by id");
        throw new DaoException("Cannot update drug by id");
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_DRUG_BY_DRUG_ID)) {
            return deleteBillet(preparedStatement, id);
        } catch (SQLException e) {
            LOG.error("Cannot delete drug by drug id", e);
            throw new DaoException("Cannot delete drug by drug id", e);
        }
    }

    @Override
    protected Drug extractEntity(ResultSet resultSet) throws SQLException {
        return new Drug.Builder().
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
    }
}
