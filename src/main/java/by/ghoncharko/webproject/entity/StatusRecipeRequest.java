package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class StatusRecipeRequest
 *
 * @author
 */
public class StatusRecipeRequest implements Entity {
    private Integer id;
    private String name;

    private StatusRecipeRequest() {

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
        StatusRecipeRequest that = (StatusRecipeRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StatusRecipeRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private final StatusRecipeRequest newsStatusRecipeRequest;

        public Builder() {
            newsStatusRecipeRequest = new StatusRecipeRequest();
        }

        public Builder withId(Integer id) {
            newsStatusRecipeRequest.id = id;
            return this;
        }

        public Builder withName(String name) {
            newsStatusRecipeRequest.name = name;
            return this;
        }

        public StatusRecipeRequest build() {
            return newsStatusRecipeRequest;
        }
    }
}
