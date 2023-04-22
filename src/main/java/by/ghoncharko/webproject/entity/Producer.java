package by.ghoncharko.webproject.entity;

import java.util.Objects;


public class Producer implements Entity {
    private final Integer id;
    private final String name;

    private Producer(Builder builder) {
        id = builder.id;
        name = builder.name;
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

        Producer producer = (Producer) o;

        if (!Objects.equals(id, producer.id)) return false;
        return Objects.equals(name, producer.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private Integer id;
        private String name;


        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Producer build() {
            return new Producer(this);
        }
    }
}
