package by.ghoncharko.webproject.entity;


import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity class Order
 *
 * @author Dmitry Ghoncharko
 */
public class UserOrder implements Entity {
    private final Integer id;
    private final User user;
    private final BigDecimal orderFinalPrice;
    private UserOrder(Builder builder) {
        id = builder.id;
        user = builder.user;
        orderFinalPrice = builder.orderFinalPrice;
    }

    public Integer getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public BigDecimal getOrderFinalPrice() {
        return orderFinalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrder userOrder = (UserOrder) o;

        if (!Objects.equals(id, userOrder.id)) return false;
        if (!Objects.equals(user, userOrder.user)) return false;
        return Objects.equals(orderFinalPrice, userOrder.orderFinalPrice);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (orderFinalPrice != null ? orderFinalPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", user=" + user +
                ", orderFinalPrice=" + orderFinalPrice +
                '}';
    }

    public static class Builder {
        private Integer id;
        private User user;
        private BigDecimal orderFinalPrice;
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }
        public Builder withOrderFinalPrice(BigDecimal orderFinalPrice){
            this.orderFinalPrice = orderFinalPrice;
            return this;
        }
        public UserOrder build() {
            return new UserOrder(this);
        }
    }
}
