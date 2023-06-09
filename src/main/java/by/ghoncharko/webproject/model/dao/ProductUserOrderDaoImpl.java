package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.*;
import by.ghoncharko.webproject.entity.Product;
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

public class ProductUserOrderDaoImpl extends AbstractDao<DrugUserOrder> implements DrugUserOrderDao {
    private static final Logger LOG = LogManager.getLogger(ProductUserOrderDaoImpl.class);
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
    private static final String SQL_FIND_DRUG_USER_ORDER_BY_USER_ORDER_ID_AND_DRUG_ID = "SELECT" +
            " drug_user_order.id, uo.id, u.id, u.login, u.password, r.role_name , u.first_name, u.last_name, u.is_banned, d.id, d.name," +
            " d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  drug_user_order.drug_count, drug_user_order.final_price FROM drug_user_order" +
            " INNER JOIN drug d on drug_user_order.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id" +
            " INNER JOIN user_order uo on drug_user_order.user_order_id = uo.id" +
            " INNER JOIN user u on uo.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id" +
            " WHERE drug_user_order.user_order_id = ? AND drug_user_order.drug_id = ?";
    private static final String SQL_FIND_DRUG_USER_ORDER_BY_USER_ORDER_ID = "SELECT" +
            " drug_user_order.id, uo.id, u.id, u.login, u.password, r.role_name , u.first_name, u.last_name, u.is_banned, d.id, d.name," +
            " d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  drug_user_order.drug_count, drug_user_order.final_price FROM drug_user_order" +
            " INNER JOIN drug d on drug_user_order.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id" +
            " INNER JOIN user_order uo on drug_user_order.user_order_id = uo.id" +
            " INNER JOIN user u on uo.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id" +
            " WHERE drug_user_order.user_order_id = ?";
    private static final String SQL_UPDATE_DRUG_USER_ORDER_BY_ID = "UPDATE drug_user_order" +
            " SET user_order_id = ?, drug_id = ?, drug_count = ?" +
            " WHERE id = ?";
    private static final String SQL_UPDATE_DRUG_USER_ORDER_COUNT_AND_FINAL_PRICE_BY_ID = "UPDATE drug_user_order" +
            " SET  drug_count = ?, final_price = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_DRUG_USER_ORDER_BY_USER_ORDER_ID = "DELETE FROM drug_user_order" +
            " WHERE user_order_id = ?";
    private static final String SQL_DELETE_DRUG_USER_ORDER_BY_ID = "DELETE FROM drug_user_order WHERE id = ?";

    public ProductUserOrderDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public DrugUserOrder create(DrugUserOrder entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_DRUG_USER_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getUserOrder().getId());
            preparedStatement.setInt(2, entity.getProduct().getId());
            preparedStatement.setInt(3, entity.getDrugCount());
            preparedStatement.setDouble(4,entity.getFinalPrice().doubleValue());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new DrugUserOrder.Builder().
                            withId(resultSet.getInt(1)).
                            withUserOrder(entity.getUserOrder()).
                            withDrug(entity.getProduct()).
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
    public List<DrugUserOrder> findDrugUserOrdersByUserOrderId(Integer userOrderId) throws DaoException {
        final List<DrugUserOrder> drugUserOrderList = new ArrayList<>();
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_USER_ORDER_BY_USER_ORDER_ID)){
           preparedStatement.setInt(1,userOrderId);
           final ResultSet resultSet = preparedStatement.executeQuery();
           while (resultSet.next()){
               final DrugUserOrder drugUserOrder = extractEntity(resultSet);
               drugUserOrderList.add(drugUserOrder);
           }
       }catch (SQLException e){
           LOG.error("Cannot find drugUserOrders buy user order id ",e);
           throw new DaoException("Cannot find drugUserOrders buy user order id ",e);
       }
        return drugUserOrderList;
    }
    @Override
    public Optional<DrugUserOrder> findDrugUserOrderByUserOrderIdAndDrugId(Integer userOrderId, Integer drugId) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_DRUG_USER_ORDER_BY_USER_ORDER_ID_AND_DRUG_ID)){
            preparedStatement.setInt(1,userOrderId);
            preparedStatement.setInt(2,drugId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return Optional.of(extractEntity(resultSet));
            }
        }catch (SQLException e){
            LOG.error("Cannot find drug user order by user order id",e);
            throw new DaoException("Cannot find drug user order by user order id",e);
        }
        LOG.info("Cannot find drug user order by user order id");
        return Optional.empty();
    }

    @Override
    public boolean updateDrugCountAndFinalPriceByDrugUserOrderId(Integer drugCount, Integer drugUserOrderId, Double finalPrice) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_DRUG_USER_ORDER_COUNT_AND_FINAL_PRICE_BY_ID)){
            preparedStatement.setInt(1,drugCount);
            preparedStatement.setDouble(2,finalPrice);
            preparedStatement.setInt(3,drugUserOrderId);
            final int countUpdatedRows = preparedStatement.executeUpdate();
            return countUpdatedRows>0;
        }catch (SQLException e){
            LOG.error("Cannot update drug count by drug user order id",e);
            throw new DaoException("Cannot update drug count by drug user order id",e);
        }
    }

    @Override
    public boolean deleteDrugUserOrderByUserOrderId(Integer userOrderId) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_DRUG_USER_ORDER_BY_USER_ORDER_ID)){
            return deleteBillet(preparedStatement,userOrderId);
        }catch (SQLException e){
            LOG.error("Cannot delete drug user order by user order id",e);
            throw new DaoException("Cannot delete drug user order by user order id",e);
        }
    }

    @Override
    public List<DrugUserOrder> findAll() throws DaoException {
        final List<DrugUserOrder> drugUserOrderList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_DRUG_USER_ORDERS);
            while (resultSet.next()) {
                final DrugUserOrder drugUserOrder = extractEntity(resultSet);
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
                return Optional.of(extractEntity(resultSet));
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
            preparedStatement.setInt(2, entity.getProduct().getId());
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
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_DRUG_USER_ORDER_BY_ID)) {
            return deleteBillet(preparedStatement,id);
        } catch (SQLException e) {
            LOG.error("Cannot delete drugUserOrder by id", e);
            throw new DaoException("Cannot delete drugUserOrder by id", e);
        }
    }

    @Override
    protected DrugUserOrder extractEntity(ResultSet resultSet) throws SQLException {
        return new DrugUserOrder.Builder().
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
                withDrug(new Product.Builder().
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
    }
}
