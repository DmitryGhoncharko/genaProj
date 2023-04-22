package by.ghoncharko.webproject.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class BankCard implements Entity {
    private final Integer id;
    private final User user;
    private final BigDecimal balance;

    private BankCard(Builder builder) {
        id = builder.id;
        user = builder.user;
        balance = builder.balance;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankCard bankCard = (BankCard) o;

        if (!Objects.equals(id, bankCard.id)) return false;
        if (!Objects.equals(user, bankCard.user)) return false;
        return Objects.equals(balance, bankCard.balance);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "id=" + id +
                ", userId=" + user +
                ", balance=" + balance +
                '}';
    }

    public static class Builder {
        private Integer id;
        private User user;
        private BigDecimal balance;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public BankCard build() {
            return new BankCard(this);
        }
    }
}
