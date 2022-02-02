package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.entity.UserOrder;
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

public class DrugUserOrderDaoImpl implements DrugUserOrderDao {
    private static final Logger LOG = LogManager.getLogger(DrugUserOrderDaoImpl.class);
    private static final String SQL_CREATE_DRUG_USER_ORDER = "INSERT INTO drug_user_order (user_order_id, drug_id, drug_count, final_price) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_ALL_DRUG_USER_ORDERS = "SELECT" +
            " drug_user_order.id, uo.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name," +
            " u.is_banned, d.id, d.name," +
            " d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  drug_user_order.drug_count, drug_user_order.final_price" +
            " FROM drug_user_order" +
            " INNER JOIN drug d on drug_user_order.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id" +
            " INNER JOIN user_order uo on drug_user_order.user_order_id = uo.id" +
            " INNER JOIN user u on uo.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id";
    private static final String SQL_FIND_DRUG_USER_ORDER_BY_ID = "SELECT" +
            " drug_user_order.id, uo.id, u.id, u.login, u.password, r.role_name , u.first_name, u.last_name, u.is_banned, d.id, d.name," +
            " d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  drug_user_order.drug_count, drug_user_order.final_price FROM drug_user_order" +
            " INNER JOIN drug d on drug_user_order.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id" +
            " INNER JOIN user_order uo on drug_user_order.user_order_id = uo.id" +
            " INNER JOIN user u on uo.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id" +
            " WHERE drug_user_order.id = ?";
    private static final String SQL_UPDATE_DRUG_USER_ORDER_BY_ID = "UPDATE drug_user_order" +
            " SET user_order_id = ?, drug_id = ?, drug_count = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_DRUG_USER_ORDER_BY_ID = "DELETE FROM drug_user_order WHERE id = ?";
    private final Connection connection;

    public DrugUserOrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DrugUserOrder create(DrugUserOrder entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_DRUG_USER_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getUserOrder().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setInt(3, entity.getDrugCount());
            preparedStatement.setDouble(4,entity.getFinalPrice().doubleValue());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new DrugUserOrder.Builder().
                            withId(resultSet.getInt(1)).
                            withUserOrder(entity.getUserOrder()).
                            withDrug(entity.getDrug()).
                            withDrugCount(entity.getDrugCount()).
                            build();
                }
            }
        } catch (SQLException e) {
            LOG.error("Cannot create DrugUserOrder", e);
            throw new DaoException("Cannot create DrugUserOrder", e);
        }
        LOG.error("Cannot create DrugUserOrder");
        throw new DaoException("Cannot create DrugUserOrder");
    }

    @Override
    public List<DrugUserOrder> findAll() throws DaoException {
        final List<DrugUserOrder> drugUserOrderList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUG_USER_ORDERS);
            while (resultSet.next()) {
                final DrugUserOrder drugUserOrder = new DrugUserOrder.Builder().
                        withId(resultSet.getInt(1)).
                        withUserOrder(new UserOrder.Builder().
                                withId(resultSet.getInt(2)).
                                withUser(new User.Builder().
                                        withId(resultSet.getInt(3)).
                                        withLogin(resultSet.getString(4)).
                                        withPassword(resultSet.getString(5)).
                                        withRole(Role.valueOf(resultSet.getString(6))).
                                        withFirstName(resultSet.getString(7)).
                                        withLastName(resultSet.getString(8)).
                                        withBannedStatus(resultSet.getBoolean(9)).
                                        build()).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(10)).
                                withName(resultSet.getString(11)).
                                withPrice(BigDecimal.valueOf(resultSet.getDouble(12))).
                                withCount(resultSet.getInt(13)).
                                withDescription(resultSet.getString(14)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(15)).
                                        withName(resultSet.getString(16)).
                                        build()).
                                withNeedReceip(resultSet.getBoolean(17)).
                                withIsDeleted(resultSet.getBoolean(18)).
                                build()).
                        withDrugCount(resultSet.getInt(19)).
                        withFinalPrice(BigDecimal.valueOf(resultSet.getDouble(20))).
                        build();
                drugUserOrderList.add(drugUserOrder);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all drugUserOrders", e);
            throw new DaoException("Cannot find all drugUserOrders", e);
        }
        return drugUserOrderList;
    }

    @Override
    public Optional<DrugUserOrder> findEntityById(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_USER_ORDER_BY_ID)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new DrugUserOrder.Builder().
                        withId(resultSet.getInt(1)).
                        withUserOrder(new UserOrder.Builder().
                                withId(resultSet.getInt(2)).
                                withUser(new User.Builder().
                                        withId(resultSet.getInt(3)).
                                        withLogin(resultSet.getString(4)).
                                        withPassword(resultSet.getString(5)).
                                        withRole(Role.valueOf(resultSet.getString(6))).
                                        withFirstName(resultSet.getString(7)).
                                        withLastName(resultSet.getString(8)).
                                        withBannedStatus(resultSet.getBoolean(9)).
                                        build()).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(10)).
                                withName(resultSet.getString(11)).
                                withPrice(BigDecimal.valueOf(resultSet.getDouble(12))).
                                withCount(resultSet.getInt(13)).
                                withDescription(resultSet.getString(14)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(15)).
                                        withName(resultSet.getString(16)).
                                        build()).
                                withNeedReceip(resultSet.getBoolean(17)).
                                withIsDeleted(resultSet.getBoolean(18)).
                                build()).
                        withDrugCount(resultSet.getInt(19)).
                        withFinalPrice(BigDecimal.valueOf(resultSet.getDouble(20))).
                        build());
            }
        } catch (SQLException e) {
            LOG.error("Cannot find drugUserOrder by id", e);
            throw new DaoException("Cannot find drugUserOrder by id", e);
        }
        LOG.info("Cannot find drugUserOrder by id");
        return Optional.empty();
    }

    @Override
    public DrugUserOrder update(DrugUserOrder entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_DRUG_USER_ORDER_BY_ID)) {
            preparedStatement.setInt(1, entity.getUserOrder().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setInt(3, entity.getDrugCount());
            preparedStatement.setInt(4, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update drugUserOrder by id", e);
            throw new DaoException("Cannot update drugUserOrder by id", e);
        }
        LOG.error("Cannot update drugUserOrder by id");
        throw new DaoException("Cannot update drugUserOrder by id");
    }

    @Override
    public boolean delete(DrugUserOrder entity) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_DRUG_USER_ORDER_BY_ID)) {
            preparedStatement.setInt(1, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            return countUpdatedRows > 0;
        } catch (SQLException e) {
            LOG.error("Cannot delete drugUserOrder by id", e);
            throw new DaoException("Cannot delete drugUserOrder by id", e);
        }
    }
}
