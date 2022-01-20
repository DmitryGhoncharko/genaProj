package by.ghoncharko.webproject.entity;

import java.sql.Date;
import java.util.Objects;

/**
 * Entity class Order
 *
 * @author Dmitry Ghoncharko
 */
public class UserOrder implements Entity {
    private final Integer id;
    private final User user;
    private final Boolean isPayed;
    private final Date datePayed;

    private UserOrder(Builder builder) {
        id = builder.id;
        user = builder.user;
        isPayed = builder.isPayed;
        datePayed = builder.datePayed;
    }

    public Integer getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public Boolean getPayed() {
        return isPayed;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrder userOrder = (UserOrder) o;

        if (!Objects.equals(id, userOrder.id)) return false;
        if (!Objects.equals(user, userOrder.user)) return false;
        if (!Objects.equals(isPayed, userOrder.isPayed)) return false;
        return Objects.equals(datePayed, userOrder.datePayed);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (isPayed != null ? isPayed.hashCode() : 0);
        result = 31 * result + (datePayed != null ? datePayed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", user=" + user +
                ", isPayed=" + isPayed +
                ", datePayed=" + datePayed +
                '}';
    }

    public static class Builder {
        private Integer id;
        private User user;
        private Boolean isPayed;
        private Date datePayed;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }


        public Builder withIsPayed(Boolean isPayed) {
            this.isPayed = isPayed;
            return this;
        }

        public Builder withDatePayed(Date datePayed) {
            this.datePayed = datePayed;
            return this;
        }

        public UserOrder build() {
            return new UserOrder(this);
        }
    }
}
