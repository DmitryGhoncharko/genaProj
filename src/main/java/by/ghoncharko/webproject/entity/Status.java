package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class Status
 *
 * @author Dmitry Ghoncharko
 */
public class Status implements Entity {
    private Integer id;
    private String name;

    private Status() {

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
        Status status = (Status) o;
        return Objects.equals(id, status.id) &&
                Objects.equals(name, status.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private final Status newStatus;

        public Builder() {
            newStatus = new Status();
        }

        public Builder withId(Integer id) {
            newStatus.id = id;
            return this;
        }

        public Builder withName(String name) {
            newStatus.name = name;
            return this;
        }

        public Status build() {
            return newStatus;
        }
    }
}
