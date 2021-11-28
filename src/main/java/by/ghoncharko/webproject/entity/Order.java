package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class Order
 *
 * @author Dmitry Ghoncharko
 */
public class Order implements Entity {
    private Integer id;
    private User user;
    private Drug drug;
    private int count;
    private double finalPrice;
    private OrderStatus status;

    private Order() {

    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Drug getDrug() {
        return drug;
    }


    public int getCount() {
        return count;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return count == order.count &&
                Double.compare(order.finalPrice, finalPrice) == 0 &&
                Objects.equals(id, order.id) &&
                Objects.equals(user, order.user) &&
                Objects.equals(drug, order.drug) &&
                Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, drug, count, finalPrice, status);
    }

    public static class Builder {
        private final Order newOrder;

        public Builder() {
            newOrder = new Order();
        }

        public Builder withId(Integer id) {
            newOrder.id = id;
            return this;
        }

        public Builder withUser(User user) {
            newOrder.user = user;
            return this;
        }

        public Builder withDrug(Drug drug) {
            newOrder.drug = drug;
            return this;
        }

        public Builder withCount(int count) {
            newOrder.count = count;
            return this;
        }

        public Builder withFinalPrice(double finalPrice) {
            newOrder.finalPrice = finalPrice;
            return this;
        }

        public Builder withStatus(OrderStatus status) {
            newOrder.status = status;
            return this;
        }

        public Order build() {
            return newOrder;
        }
    }
}
