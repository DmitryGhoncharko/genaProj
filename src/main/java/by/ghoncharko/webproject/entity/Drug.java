package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class Drug
 *
 * @author Dmitry Ghoncharko
 */
public class Drug implements Entity {
    private Integer id;
    private String name;
    private double price;
    private boolean needReceip;
    private int count;
    private String description;
    private Producer producer;

    private Drug() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isNeedRecipe() {
        return needReceip;
    }

    public int getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public Producer getProducer() {
        return producer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug = (Drug) o;
        return Double.compare(drug.price, price) == 0 &&
                needReceip == drug.needReceip &&
                count == drug.count &&
                Objects.equals(id, drug.id) &&
                Objects.equals(name, drug.name) &&
                Objects.equals(description, drug.description) &&
                Objects.equals(producer, drug.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, needReceip, count, description, producer);
    }

    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", needReceip=" + needReceip +
                ", count=" + count +
                ", description='" + description + '\'' +
                ", producer=" + producer +
                '}';
    }

    public static class Builder {
        private final Drug newDrug;

        public Builder() {
            newDrug = new Drug();
        }

        public Builder withId(Integer id) {
            newDrug.id = id;
            return this;
        }

        public Builder withName(String name) {
            newDrug.name = name;
            return this;
        }

        public Builder withPrice(double price) {
            newDrug.price = price;
            return this;
        }

        public Builder withNeedReceip(boolean needReceip) {
            newDrug.needReceip = needReceip;
            return this;
        }

        public Builder withCount(int count) {
            newDrug.count = count;
            return this;
        }

        public Builder withDescription(String description) {
            newDrug.description = description;
            return this;
        }

        public Builder withProducer(Producer producer) {
            newDrug.producer = producer;
            return this;
        }

        public Drug build() {
            return newDrug;
        }
    }
}
