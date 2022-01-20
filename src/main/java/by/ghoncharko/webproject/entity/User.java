package by.ghoncharko.webproject.entity;


import java.util.Objects;

/**
 * Entity class User
 *
 * @author Dmitry Ghoncharko
 */
public class User implements Entity {
    private final Integer id;
    private final String login;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final Boolean isBanned;

    private User(Builder builder) {
        id = builder.id;
        login = builder.login;
        password = builder.password;
        firstName = builder.firstName;
        lastName = builder.lastName;
        role = builder.role;
        isBanned = builder.isBanned;
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

    public Boolean getBanned() {
        return isBanned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(id, user.id)) return false;
        if (!Objects.equals(login, user.login)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(firstName, user.firstName)) return false;
        if (!Objects.equals(lastName, user.lastName)) return false;
        if (!Objects.equals(role, user.role)) return false;
        return Objects.equals(isBanned, user.isBanned);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (isBanned != null ? isBanned.hashCode() : 0);
        return result;
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
                ", isBanned=" + isBanned +
                '}';
    }

    public static class Builder {
        private Integer id;
        private String login;
        private String password;
        private String firstName;
        private String lastName;
        private Role role;
        private Boolean isBanned;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder withBannedStatus(Boolean bannedStatus) {
            this.isBanned = bannedStatus;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
