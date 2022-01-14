package by.ghoncharko.webproject.entity;

import java.util.Objects;

public final class RecipeStatus {
    private final Integer id;
    private final String statusName;

    public RecipeStatus(Builder builder) {
        id = builder.id;
        statusName = builder.statusName;
    }

    public Integer getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public static class Builder {
        private Integer id;
        private String statusName;

        Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        Builder withStatusName(String statusName) {
            this.statusName = statusName;
            return this;
        }

        public RecipeStatus build() {
            return new RecipeStatus(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeStatus that = (RecipeStatus) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(statusName, that.statusName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (statusName != null ? statusName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecipeStatus{" +
                "id=" + id +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}
