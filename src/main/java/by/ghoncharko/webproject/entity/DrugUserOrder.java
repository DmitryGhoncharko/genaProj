package by.ghoncharko.webproject.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class DrugUserOrder implements Entity {
    private final Integer id;
    private final UserOrder userOrder;
    private final Product product;
    private final Integer drugCount;
    private final BigDecimal finalPrice;

    private DrugUserOrder(Builder builder) {
        id = builder.id;
        userOrder = builder.userOrder;
        product = builder.product;
        drugCount = builder.drugCount;
        finalPrice = builder.finalPrice;
    }

    public Integer getId() {
        return id;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getDrugCount() {
        return drugCount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugUserOrder that = (DrugUserOrder) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(userOrder, that.userOrder)) return false;
        if (!Objects.equals(product, that.product)) return false;
        if (!Objects.equals(drugCount, that.drugCount)) return false;
        return Objects.equals(finalPrice, that.finalPrice);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userOrder != null ? userOrder.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (drugCount != null ? drugCount.hashCode() : 0);
        result = 31 * result + (finalPrice != null ? finalPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrugUserOrder{" +
                "id=" + id +
                ", userOrder=" + userOrder +
                ", drug=" + product +
                ", drugCount=" + drugCount +
                ", drugFinalPrice=" + finalPrice +
                '}';
    }

    public static class Builder {
        private Integer id;
        private UserOrder userOrder;
        private Product product;
        private Integer drugCount;
        private BigDecimal finalPrice;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUserOrder(UserOrder userOrder) {
            this.userOrder = userOrder;
            return this;
        }

        public Builder withDrug(Product product) {
            this.product = product;
            return this;
        }

        public Builder withDrugCount(Integer drugCount) {
            this.drugCount = drugCount;
            return this;
        }

        public Builder withFinalPrice(BigDecimal finalPrice) {
            this.finalPrice = finalPrice;
            return this;
        }

        public DrugUserOrder build() {
            return new DrugUserOrder(this);
        }
    }
}
