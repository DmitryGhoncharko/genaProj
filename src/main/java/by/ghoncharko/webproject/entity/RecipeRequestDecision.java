package by.ghoncharko.webproject.entity;

import java.sql.Date;
import java.util.Objects;

public class RecipeRequestDecision implements Entity {
    private final Integer id;
    private final RecipeRequest recipeRequest;
    private final Boolean isExtended;
    private final Date dateDecision;

    private RecipeRequestDecision(Builder builder) {
        id = builder.id;
        isExtended = builder.isExtended;
        dateDecision = builder.dateDecision;
        recipeRequest = builder.recipeRequest;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getExtended() {
        return isExtended;
    }

    public Date getDateDecision() {
        return dateDecision;
    }

    public RecipeRequest getRecipeRequest() {
        return recipeRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeRequestDecision that = (RecipeRequestDecision) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(recipeRequest, that.recipeRequest))
            return false;
        if (!Objects.equals(isExtended, that.isExtended)) return false;
        return Objects.equals(dateDecision, that.dateDecision);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (recipeRequest != null ? recipeRequest.hashCode() : 0);
        result = 31 * result + (isExtended != null ? isExtended.hashCode() : 0);
        result = 31 * result + (dateDecision != null ? dateDecision.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecipeRequestDecision{" +
                "id=" + id +
                ", recipeRequest=" + recipeRequest +
                ", isExtended=" + isExtended +
                ", dateDecision=" + dateDecision +
                '}';
    }

    public static class Builder {
        private Integer id;
        private Boolean isExtended;
        private Date dateDecision;
        private RecipeRequest recipeRequest;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withIsExtended(Boolean isExtended) {
            this.isExtended = isExtended;
            return this;
        }

        public Builder withDateDecision(Date dateDecision) {
            this.dateDecision = dateDecision;
            return this;
        }

        public Builder withRecipeRequest(RecipeRequest recipeRequest) {
            this.recipeRequest = recipeRequest;
            return this;
        }

        public RecipeRequestDecision build() {
            return new RecipeRequestDecision(this);
        }
    }
}
