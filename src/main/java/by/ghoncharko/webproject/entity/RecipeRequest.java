package by.ghoncharko.webproject.entity;

import java.sql.Date;
import java.util.Objects;

public class RecipeRequest implements Entity {
    private final Integer id;
    private final Recipe recipe;
    private final Boolean isExtended;
    private final Date dateSolution;

    public RecipeRequest(Builder builder) {
        id = builder.id;
        recipe = builder.recipe;
        isExtended = builder.isExtended;
        dateSolution = builder.dateSolution;
    }

    public Integer getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Boolean getExtended() {
        return isExtended;
    }

    public Date getDateSolution() {
        return dateSolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeRequest that = (RecipeRequest) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(recipe, that.recipe)) return false;
        if (!Objects.equals(isExtended, that.isExtended)) return false;
        return Objects.equals(dateSolution, that.dateSolution);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (recipe != null ? recipe.hashCode() : 0);
        result = 31 * result + (isExtended != null ? isExtended.hashCode() : 0);
        result = 31 * result + (dateSolution != null ? dateSolution.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id=" + id +
                ", recipe=" + recipe +
                ", isExtended=" + isExtended +
                ", dateSolution=" + dateSolution +
                '}';
    }

    public static class Builder {
        private Integer id;
        private Recipe recipe;
        private Boolean isExtended;
        private Date dateSolution;
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public Builder withIsExtended(Boolean isExtended) {
            this.isExtended = isExtended;
            return this;
        }

        public Builder withDateSolution(Date dateSolution) {
            this.dateSolution = dateSolution;
            return this;
        }
        public RecipeRequest build() {
            return new RecipeRequest(this);
        }
    }
}
