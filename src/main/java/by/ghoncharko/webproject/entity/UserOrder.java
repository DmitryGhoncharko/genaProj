package by.ghoncharko.webproject.entity;


import java.util.Objects;


public class UserOrder implements Entity {
    private final Integer id;
    private final User user;

    private UserOrder(Builder builder) {
        id = builder.id;
        user = builder.user;
    }

    public Integer getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrder userOrder = (UserOrder) o;

        if (!Objects.equals(id, userOrder.id)) return false;
        return Objects.equals(user, userOrder.user);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }

    public static class Builder {
        private Integer id;
        private User user;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public UserOrder build() {
            return new UserOrder(this);
        }
    }
}
