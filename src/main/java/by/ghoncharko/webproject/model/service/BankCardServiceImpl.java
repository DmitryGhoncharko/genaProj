package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.DaoHelper;
import by.ghoncharko.webproject.model.dao.DaoHelperFactory;
import by.ghoncharko.webproject.validator.BankCardServiceValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class BankCardServiceImpl implements BankCardService{
    private static final Logger LOG = LogManager.getLogger(BankCardServiceImpl.class);
    private final BankCardServiceValidator bankCardServiceValidator;
    private final DaoHelperFactory daoHelperFactory;

    public BankCardServiceImpl(DaoHelperFactory daoHelperFactory, BankCardServiceValidator bankCardServiceValidator){
       this.daoHelperFactory = daoHelperFactory;
        this.bankCardServiceValidator = bankCardServiceValidator;
    }

    @Override
    public List<BankCard> findAll() throws ServiceException {
        return null;
    }

    @Override
    public boolean addBankCard(Double balance, User user) throws ServiceException {
        if(!bankCardServiceValidator.validateAddBankCard(balance,user)){
            return false;
        }
        final BankCard bankCard = new BankCard.Builder().
                withBalance(BigDecimal.valueOf(balance)).
                withUser(user).
                build();
        try(final DaoHelper daoHelper = daoHelperFactory.create()){
            final BankCardDao bankCardDao = daoHelper.createBankCardDao();
            bankCardDao.create(bankCard);
        }catch (Exception e){
            LOG.error("Cannot add bank card",e);
            throw new ServiceException("Cannot add bank card",e);
        }
        return true;
    }

    @Override
    public List<BankCard> findBankCardsByUserId(User user) throws ServiceException {
        if(!bankCardServiceValidator.validateFindBankCardByUserId(user)){
            return Collections.emptyList();
        }
        final int userId = user.getId();
        try(final DaoHelper daoHelper = daoHelperFactory.create()){
            final BankCardDao bankCardDao = daoHelper.createBankCardDao();
            return  bankCardDao.findUserBankCardsByUserId(userId);
        }catch (Exception e){
            LOG.error("Cannot find bank cards by user id",e);
            throw new ServiceException("Cannot find bank cards by user id",e);
        }
    }

    @Override
    public void deleteBankCard(User user, Integer cardId) throws ServiceException {
        if(!bankCardServiceValidator.validateDeleteBankCard(user,cardId)){
            return;
        }
        final int userId = user.getId();
        try(final DaoHelper daoHelper = daoHelperFactory.create()){
            final BankCardDao bankCardDao = daoHelper.createBankCardDao();
            bankCardDao.deleteCardByCardIdAndUserId(cardId,userId);
        }catch (Exception e){
            LOG.error("Cannot delete bank card by card id and user id ",e);
            throw new ServiceException("Cannot delete bank card by card id and user id ",e);
        }
    }
}
