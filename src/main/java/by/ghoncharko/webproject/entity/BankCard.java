package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class BankCard
 *
 * @author Dmitry Ghoncharko
 */
public class BankCard implements Entity {
    private Integer id;
    private Integer userId;
    private double balance;

    private BankCard() {

    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCard bankCard = (BankCard) o;
        return Double.compare(bankCard.balance, balance) == 0 &&
                Objects.equals(id, bankCard.id) &&
                Objects.equals(userId, bankCard.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, balance);
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }

    public static class Builder {
        private final BankCard newBankCard;

        public Builder() {
            newBankCard = new BankCard();
        }

        public Builder withId(Integer id) {
            newBankCard.id = id;
            return this;
        }

        public Builder withUserId(Integer userId) {
            newBankCard.userId = userId;
            return this;
        }

        public Builder withBalance(double balance) {
            newBankCard.balance = balance;
            return this;
        }

        public BankCard build() {
            return newBankCard;
        }
    }
}
