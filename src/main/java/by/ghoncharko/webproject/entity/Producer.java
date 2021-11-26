package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class Producer
 *
 * @author Dmitry Ghoncharko
 */
public class Producer implements Entity {
    private Integer id;
    private String name;

    private Producer() {

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
        return Objects.equals(id, producer.id) &&
                Objects.equals(name, producer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private final Producer newProducer;

        public Builder() {
            newProducer = new Producer();
        }

        public Builder withId(Integer id) {
            newProducer.id = id;
            return this;
        }

        public Builder withName(String name) {
            newProducer.name = name;
            return this;
        }

        public Producer build() {
            return newProducer;
        }
    }
}
