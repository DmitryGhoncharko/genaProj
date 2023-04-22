package by.ghoncharko.webproject.entity;


import java.sql.Date;
import java.util.Objects;


public class Recipe implements Entity {
    private final Integer id;
    private final User user;
    private final Product product;
    private final Date dateStart;
    private final Date dateEnd;
    private final User doctor;

    private Recipe(Builder builder) {
        id = builder.id;
        product = builder.product;
        dateStart = builder.dateStart;
        dateEnd = builder.dateEnd;
        user = builder.user;
        doctor = builder.doctor;
    }

    public Integer getId() {
        return id;
    }

    public Product getDrug() {
        return product;
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

    public User getDoctor() {
        return doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (!Objects.equals(id, recipe.id)) return false;
        if (!Objects.equals(user, recipe.user)) return false;
        if (!Objects.equals(product, recipe.product)) return false;
        if (!Objects.equals(dateStart, recipe.dateStart)) return false;
        if (!Objects.equals(dateEnd, recipe.dateEnd)) return false;
        return Objects.equals(doctor, recipe.doctor);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (dateStart != null ? dateStart.hashCode() : 0);
        result = 31 * result + (dateEnd != null ? dateEnd.hashCode() : 0);
        result = 31 * result + (doctor != null ? doctor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", user=" + user +
                ", drug=" + product +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", doctor=" + doctor +
                '}';
    }

    public static class Builder {
        private Integer id;
        private Product product;
        private Date dateStart;
        private Date dateEnd;
        private User user;
        private User doctor;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withDrug(Product product) {
            this.product = product;
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

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withDoctor(User doctor) {
            this.doctor = doctor;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}
