package by.ghoncharko.webproject.model.service;



import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService extends Service<User> {
    Optional<User> authenticate(String login, String password) throws ServiceException;

    Optional<User> createClient(String login, String password, String firstName, String lastName) throws ServiceException;

    List<User> findAllClientsLimitOffsetPagination(Integer currentPageNumber) throws ServiceException;

    int findMaxPagesForUsersAsClients() throws ServiceException;
    static UserService getInstance() {
        return UserServiceImpl.getInstance();
    }


    List<User> findAllLimitOffsetPagination(Integer currentPage) throws ServiceException;

    int findMaxPagesCountForAllUsers() throws ServiceException;
}
