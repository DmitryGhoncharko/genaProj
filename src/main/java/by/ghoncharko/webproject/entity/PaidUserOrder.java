package by.ghoncharko.webproject.entity;

import java.sql.Date;
import java.util.Objects;

public class PaidUserOrder implements Entity {
    private final Integer id;
    private final UserOrder userOrder;
    private final Date datePayed;

    public PaidUserOrder(Builder builder) {
        id = builder.id;
        userOrder = builder.userOrder;
        datePayed = builder.datePayed;
    }

    public Integer getId() {
        return id;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaidUserOrder that = (PaidUserOrder) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(userOrder, that.userOrder)) return false;
        return Objects.equals(datePayed, that.datePayed);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userOrder != null ? userOrder.hashCode() : 0);
        result = 31 * result + (datePayed != null ? datePayed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PaidUserOrder{" +
                "id=" + id +
                ", userOrder=" + userOrder +
                ", datePayed=" + datePayed +
                '}';
    }

    public static class Builder {
        private Integer id;
        private UserOrder userOrder;
        private Date datePayed;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUserOrder(UserOrder userOrder) {
            this.userOrder = userOrder;
            return this;
        }

        public Builder withDatePayed(Date datePayed) {
            this.datePayed = datePayed;
            return this;
        }

        public PaidUserOrder build() {
            return new PaidUserOrder(this);
        }
    }
}
