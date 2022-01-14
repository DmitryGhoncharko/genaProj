package by.ghoncharko.webproject.entity;

import java.util.Objects;

public final class UserOrderStatus {
    private final Integer id;
    private final String userOrderStatusName;

    private UserOrderStatus(Builder builder) {
        id = builder.id;
        userOrderStatusName = builder.userOrderStatusName;
    }

    public Integer getId() {
        return id;
    }

    public String getUserOrderStatusName() {
        return userOrderStatusName;
    }

    public static class Builder {
        private Integer id;
        private String userOrderStatusName;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUserOrderStatusName(String userOrderStatusName) {
            this.userOrderStatusName = userOrderStatusName;
            return this;
        }

        public UserOrderStatus build() {
            return new UserOrderStatus(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrderStatus that = (UserOrderStatus) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(userOrderStatusName, that.userOrderStatusName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userOrderStatusName != null ? userOrderStatusName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserOrderStatus{" +
                "id=" + id +
                ", userOrderStatusName='" + userOrderStatusName + '\'' +
                '}';
    }
}
