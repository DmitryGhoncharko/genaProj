package by.ghoncharko.webproject.entity;


import java.util.Objects;

/**
 * Entity class User
 *
 * @author Dmitry Ghoncharko
 */
public class User implements Entity {
    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

    private User() {

    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, firstName, lastName, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Builder {
        private final User newUser;

        public Builder() {
            newUser = new User();
        }

        public Builder withId(Integer id) {
            newUser.id = id;
            return this;
        }

        public Builder withLogin(String login) {
            newUser.login = login;
            return this;
        }

        public Builder withPassword(String password) {
            newUser.password = password;
            return this;
        }

        public Builder withFirstName(String firstName) {
            newUser.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            newUser.lastName = lastName;
            return this;
        }

        public Builder withRole(Role role) {
            newUser.role = role;
            return this;
        }

        public User build() {
            return newUser;
        }
    }
}
