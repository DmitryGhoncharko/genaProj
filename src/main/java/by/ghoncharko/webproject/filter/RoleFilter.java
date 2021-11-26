package by.ghoncharko.webproject.filter;


import by.ghoncharko.webproject.command.CommandRegistry;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebFilter(filterName = "roleFilter")
public class RoleFilter implements Filter {
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String REDIRECT_ON_CONTROLLER_COMMAND = "controller?command=error";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final String commandName = req.getParameter("command");
        if (currentUserHasPermissionForCommand(commandName, req)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(REDIRECT_ON_CONTROLLER_COMMAND); //todo create error page
        }
    }

    private boolean currentUserHasPermissionForCommand(String commandName, HttpServletRequest request) {
        List<Role> allowedRoles = CommandRegistry.getAllowedRoles(commandName);
        Optional<Role> currentUserRole = retrieveCurrentUserRole(request);
        return definePermissionForCommand(currentUserRole, allowedRoles);
    }

    private Optional<Role> retrieveCurrentUserRole(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
                .map(s -> (User) s.getAttribute(USER_SESSION_ATTRIBUTE_NAME))
                .map(User::getRole);
    }

    private boolean definePermissionForCommand(Optional<Role> currentUserRole, List<Role> allowedRoles) {

        return checkUserUnauthorizedAndAccesNotRestricted(currentUserRole, allowedRoles)
                || (checkUserAuthorizedAndAccesNotRestricted(currentUserRole, allowedRoles)
                || (checkUserAuthorizedAndAccesRestricted(currentUserRole, allowedRoles) ? true : false));
    }

    private boolean checkUserUnauthorizedAndAccesNotRestricted(Optional<Role> currentUserRole, List<Role> allowedRoles) {

        return !currentUserRole.isPresent() && allowedRoles.size() == 0 ? true : false;
    }

    private boolean checkUserAuthorizedAndAccesNotRestricted(Optional<Role> currentUserRole, List<Role> allowedRoles) {
        return currentUserRole.isPresent() && allowedRoles.size() == 0 ? true : false;
    }

    private boolean checkUserAuthorizedAndAccesRestricted(Optional<Role> currentUserRole, List<Role> allowedRoles) {
        if (currentUserRole.isPresent() && allowedRoles.size() > 0) {
            for (Role role : allowedRoles) {
                if (role.equals(currentUserRole.get())) {
                    return true;
                }
            }
        }
        return false;
    }
}