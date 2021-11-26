package by.ghoncharko.webproject.entity;


import java.sql.Date;
import java.util.Objects;

/**
 * Entity class Recipe
 *
 * @author Dmitry Ghoncharko
 */
public class Recipe implements Entity {
    private Integer id;
    private Drug drug;
    private Date dateStart;
    private Date dateEnd;
    private Status status;
    private User user;

    private Recipe() {

    }

    public Integer getId() {
        return id;
    }

    public Drug getDrug() {
        return drug;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Status getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) &&
                Objects.equals(drug, recipe.drug) &&
                Objects.equals(dateStart, recipe.dateStart) &&
                Objects.equals(dateEnd, recipe.dateEnd) &&
                Objects.equals(status, recipe.status) &&
                Objects.equals(user, recipe.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, drug, dateStart, dateEnd, status, user);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", drug=" + drug +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", status=" + status +
                ", user=" + user +
                '}';
    }

    public static class Builder {
        private final Recipe newRecipe;

        public Builder() {
            newRecipe = new Recipe();
        }

        public Builder withId(Integer id) {
            newRecipe.id = id;
            return this;
        }

        public Builder withDrug(Drug drug) {
            newRecipe.drug = drug;
            return this;
        }

        public Builder withDateStart(Date dateStart) {
            newRecipe.dateStart = dateStart;
            return this;
        }

        public Builder withDateEnd(Date dateEnd) {
            newRecipe.dateEnd = dateEnd;
            return this;
        }

        public Builder withStatus(Status status) {
            newRecipe.status = status;
            return this;
        }

        public Builder withUser(User id) {
            newRecipe.user = id;
            return this;
        }

        public Recipe build() {
            return newRecipe;
        }
    }
}
