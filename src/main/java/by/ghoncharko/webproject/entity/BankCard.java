package by.ghoncharko.webproject.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity class BankCard
 *
 * @author Dmitry Ghoncharko
 */
public final class BankCard implements Entity {
    private final Integer id;
    private final Integer userId;
    private final BigDecimal balance;

    private BankCard(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        balance = builder.balance;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public static class Builder {
        private Integer id;
        private Integer userId;
        private BigDecimal balance;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUserId(Integer userId) {
            this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankCard bankCard = (BankCard) o;

        if (!Objects.equals(id, bankCard.id)) return false;
        if (!Objects.equals(userId, bankCard.userId)) return false;
        return Objects.equals(balance, bankCard.balance);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
