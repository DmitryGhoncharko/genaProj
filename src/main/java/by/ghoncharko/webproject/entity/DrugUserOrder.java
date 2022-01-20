package by.ghoncharko.webproject.entity;

import java.util.Objects;

public  class DrugUserOrder implements Entity {
    private final Integer id;
    private final UserOrder userOrder;
    private final Drug drug;
    private final Integer drugCount;

    private DrugUserOrder(Builder builder) {
        id = builder.id;
        userOrder = builder.userOrder;
        drug = builder.drug;
        drugCount = builder.drugCount;
    }

    public Integer getId() {
        return id;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public Drug getDrug() {
        return drug;
    }

    public Integer getDrugCount() {
        return drugCount;
    }

    public static class Builder {
        private Integer id;
        private UserOrder userOrder;
        private Drug drug;
        private Integer drugCount;
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUserOrder(UserOrder userOrder) {
            this.userOrder = userOrder;
            return this;
        }

        public Builder withDrug(Drug drug) {
            this.drug = drug;
            return this;
        }
        public Builder withDrugCount(Integer drugCount){
            this.drugCount = drugCount;
            return this;
        }
        public DrugUserOrder build() {
            return new DrugUserOrder(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugUserOrder that = (DrugUserOrder) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(userOrder, that.userOrder)) return false;
        if (!Objects.equals(drug, that.drug)) return false;
        return Objects.equals(drugCount, that.drugCount);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userOrder != null ? userOrder.hashCode() : 0);
        result = 31 * result + (drug != null ? drug.hashCode() : 0);
        result = 31 * result + (drugCount != null ? drugCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrugUserOrder{" +
                "id=" + id +
                ", userOrder=" + userOrder +
                ", drug=" + drug +
                ", drugCount=" + drugCount +
                '}';
    }
}
