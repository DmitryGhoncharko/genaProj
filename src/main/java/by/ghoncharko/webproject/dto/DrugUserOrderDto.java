package by.ghoncharko.webproject.dto;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.DrugUserOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class DrugUserOrderDto {
   private final List<DrugUserOrder> drugUserOrderList;
   private final List<BankCard> bankCardList;
   private final BigDecimal finalPrice;

    public DrugUserOrderDto(Builder builder) {
        drugUserOrderList =builder.drugUserOrderList;
        bankCardList = builder.bankCardList;
        finalPrice = builder.finalPrice;
    }

    public List<DrugUserOrder> getDrugUserOrderList() {
        return drugUserOrderList;
    }

    public List<BankCard> getBankCardList() {
        return bankCardList;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugUserOrderDto that = (DrugUserOrderDto) o;

        if (!Objects.equals(drugUserOrderList, that.drugUserOrderList))
            return false;
        if (!Objects.equals(bankCardList, that.bankCardList)) return false;
        return Objects.equals(finalPrice, that.finalPrice);
    }

    @Override
    public int hashCode() {
        int result = drugUserOrderList != null ? drugUserOrderList.hashCode() : 0;
        result = 31 * result + (bankCardList != null ? bankCardList.hashCode() : 0);
        result = 31 * result + (finalPrice != null ? finalPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrugUserOrderDto{" +
                "drugUserOrderList=" + drugUserOrderList +
                ", bankCardList=" + bankCardList +
                ", finalPrice=" + finalPrice +
                '}';
    }

    public static class Builder{
        private List<DrugUserOrder> drugUserOrderList;
        private List<BankCard> bankCardList;
        private BigDecimal finalPrice;

        public Builder withDrugUserOrderList(List<DrugUserOrder> drugUserOrderList){
            this.drugUserOrderList = drugUserOrderList;
            return this;
        }
        public Builder withBankCardList(List<BankCard> bankCardList){
            this.bankCardList = bankCardList;
            return this;
        }
        public Builder withFinalPrice(BigDecimal finalPrice){
            this.finalPrice = finalPrice;
            return this;
        }
        public DrugUserOrderDto build(){
            return new DrugUserOrderDto(this);
        }
    }
}
