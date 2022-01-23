package by.ghoncharko.webproject.entity;


import java.sql.Date;
import java.util.Objects;

/**
 * Entity class Recipe
 *
 * @author Dmitry Ghoncharko
 */
public class Recipe implements Entity {
    private final Integer id;
    private final User user;
    private final Drug drug;
    private final Date dateStart;
    private final Date dateEnd;
    private Recipe(Builder builder) {
        id = builder.id;
        drug = builder.drug;
        dateStart = builder.dateStart;
        dateEnd = builder.dateEnd;
        user = builder.user;
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

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (!Objects.equals(id, recipe.id)) return false;
        if (!Objects.equals(user, recipe.user)) return false;
        if (!Objects.equals(drug, recipe.drug)) return false;
        if (!Objects.equals(dateStart, recipe.dateStart)) return false;
        return Objects.equals(dateEnd, recipe.dateEnd);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (drug != null ? drug.hashCode() : 0);
        result = 31 * result + (dateStart != null ? dateStart.hashCode() : 0);
        result = 31 * result + (dateEnd != null ? dateEnd.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", user=" + user +
                ", drug=" + drug +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                '}';
    }

    public static class Builder {
        private Integer id;
        private Drug drug;
        private Date dateStart;
        private Date dateEnd;
        private User user;
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withDrug(Drug drug) {
            this.drug = drug;
            return this;
        }

        public Builder withDateStart(Date dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        public Builder withDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
            return this;
        }

        public Builder withUser(User id) {
            this.user = id;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}
