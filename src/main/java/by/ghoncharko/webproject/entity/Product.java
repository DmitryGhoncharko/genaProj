package by.ghoncharko.webproject.entity;

import java.math.BigDecimal;
import java.util.Objects;


public class Product implements Entity {
    private final Integer id;
    private final String name;
    private final BigDecimal price;
    private final Integer count;
    private final String description;
    private final Producer producer;
    private final Boolean needRecipe;
    private final Boolean isDeleted;

    private Product(Builder builder) {
        id = builder.id;
        name = builder.name;
        price = builder.price;
        count = builder.count;
        description = builder.description;
        producer = builder.producer;
        needRecipe = builder.needReceip;
        isDeleted = builder.isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public Producer getProducer() {
        return producer;
    }

    public Boolean getNeedRecipe() {
        return needRecipe;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!Objects.equals(id, product.id)) return false;
        if (!Objects.equals(name, product.name)) return false;
        if (!Objects.equals(price, product.price)) return false;
        if (!Objects.equals(count, product.count)) return false;
        if (!Objects.equals(description, product.description)) return false;
        if (!Objects.equals(producer, product.producer)) return false;
        if (!Objects.equals(needRecipe, product.needRecipe)) return false;
        return Objects.equals(isDeleted, product.isDeleted);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (producer != null ? producer.hashCode() : 0);
        result = 31 * result + (needRecipe != null ? needRecipe.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", description='" + description + '\'' +
                ", producer=" + producer +
                ", needRecipe=" + needRecipe +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public static class Builder {
        private Integer id;
        private String name;
        private BigDecimal price;
        private Integer count;
        private String description;
        private Producer producer;
        private Boolean needReceip;
        private Boolean isDeleted;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withNeedReceip(boolean needReceip) {
            this.needReceip = needReceip;
            return this;
        }

        public Builder withCount(int count) {
            this.count = count;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withProducer(Producer producer) {
            this.producer = producer;
            return this;
        }

        public Builder withIsDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
