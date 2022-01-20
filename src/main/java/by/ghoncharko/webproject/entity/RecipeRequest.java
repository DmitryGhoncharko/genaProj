package by.ghoncharko.webproject.entity;

import java.util.Objects;

public class RecipeRequest {
    private final Integer id;
    private final Recipe recipe;
    private final Boolean recipeExtended;

    private RecipeRequest(Builder builder) {
        this.id = builder.id;
        this.recipe = builder.recipe;
        this.recipeExtended = builder.recipeExtended;
    }

    public Integer getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Boolean getRecipeExtended() {
        return recipeExtended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeRequest that = (RecipeRequest) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(recipe, that.recipe)) return false;
        return Objects.equals(recipeExtended, that.recipeExtended);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (recipe != null ? recipe.hashCode() : 0);
        result = 31 * result + (recipeExtended != null ? recipeExtended.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id=" + id +
                ", recipe=" + recipe +
                ", recipeExtended=" + recipeExtended +
                '}';
    }

    public static class Builder {
        private Integer id;
        private Recipe recipe;
        private Boolean recipeExtended;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public Builder withRecipeIsExtended(Boolean recipeExtended) {
            this.recipeExtended = recipeExtended;
            return this;
        }

        public RecipeRequest build() {
            return new RecipeRequest(this);
        }
    }
}
