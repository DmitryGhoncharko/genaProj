package by.ghoncharko.webproject.entity;

import java.sql.Date;
import java.util.Objects;

/**
 * Entity class Order
 *
 * @author Dmitry Ghoncharko
 */
public final class UserOrder implements Entity {
    private final Integer id;
    private final UserOrderStatus userOrderStatus;
    private final User user;
    private final Integer count;
    private final Date datePayed;

    private UserOrder(Builder builder) {
        id = builder.id;
        userOrderStatus = builder.userOrderStatus;
        user = builder.user;
        count = builder.count;
        datePayed = builder.datePayed;
    }

    public Integer getId() {
        return id;
    }

    public UserOrderStatus getUserOrderStatus() {
        return userOrderStatus;
    }

    public User getUser() {
        return user;
    }

    public Integer getCount() {
        return count;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    public static class Builder {
        private Integer id;
        private UserOrderStatus userOrderStatus;
        private User user;
        private Integer count;
        private Date datePayed;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withCount(Integer count) {
            this.count = count;
            return this;
        }

        public Builder withDatePayed(Date datePayed) {
            this.datePayed = datePayed;
            return this;
        }

        public Builder withStatus(UserOrderStatus userOrderStatus) {
            this.userOrderStatus = userOrderStatus;
            return this;
        }

        public UserOrder build() {
            return new UserOrder(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrder userOrder = (UserOrder) o;

        if (!Objects.equals(id, userOrder.id)) return false;
        if (!Objects.equals(userOrderStatus, userOrder.userOrderStatus)) return false;
        if (!Objects.equals(user, userOrder.user)) return false;
        if (!Objects.equals(count, userOrder.count)) return false;
        return Objects.equals(datePayed, userOrder.datePayed);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userOrderStatus != null ? userOrderStatus.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (datePayed != null ? datePayed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", userOrderStatus=" + userOrderStatus +
                ", user=" + user +
                ", count=" + count +
                ", datePayed=" + datePayed +
                '}';
    }
}
