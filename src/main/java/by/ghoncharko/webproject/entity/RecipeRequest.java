package by.ghoncharko.webproject.entity;


import java.sql.Date;
import java.util.Objects;

/**
 * Entity class RecipeRequest
 *
 * @author Dmitry Ghoncharko
 */
public class RecipeRequest implements Entity {
    private Integer id;
    private User user;
    private Drug drug;
    private Date dateStart;
    private Date dateEnd;

    private RecipeRequest() {

    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRequest that = (RecipeRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(drug, that.drug) &&
                Objects.equals(dateStart, that.dateStart) &&
                Objects.equals(dateEnd, that.dateEnd);
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id=" + id +
                ", user=" + user +
                ", drug=" + drug +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, drug, dateStart, dateEnd);
    }

    public static class Builder {
        private final RecipeRequest newRecipeRequest;

        public Builder() {
            newRecipeRequest = new RecipeRequest();
        }

        public Builder withId(Integer id) {
            newRecipeRequest.id = id;
            return this;
        }

        public Builder withUser(User user) {
            newRecipeRequest.user = user;
            return this;
        }

        public Builder withDrug(Drug drug) {
            newRecipeRequest.drug = drug;
            return this;
        }

        public Builder withDateStart(Date dateStart) {
            newRecipeRequest.dateStart = dateStart;
            return this;
        }

        public Builder withDateEnd(Date dateEnd) {
            newRecipeRequest.dateEnd = dateEnd;
            return this;
        }
        public RecipeRequest build() {
            return newRecipeRequest;
        }
    }
}
