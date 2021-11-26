package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class OrderStatus
 *
 * @author Dmitry ghoncharko
 */
public class OrderStatus implements Entity {
    private Integer id;
    private String name;

    private OrderStatus() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private final OrderStatus newOrderStatus;

        public Builder() {
            newOrderStatus = new OrderStatus();
        }

        public Builder withId(Integer id) {
            newOrderStatus.id = id;
            return this;
        }

        public Builder withName(String name) {
            newOrderStatus.name = name;
            return this;
        }

        public OrderStatus build() {
            return newOrderStatus;
        }
    }
}

