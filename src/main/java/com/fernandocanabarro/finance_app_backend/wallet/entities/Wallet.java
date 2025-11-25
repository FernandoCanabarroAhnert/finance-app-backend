package com.fernandocanabarro.finance_app_backend.wallet.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    private Long id;
    @Column("user_id")
    private String userId;
    private String name;
    private String color;
    private BigDecimal balance;
    @Column("last_income")
    private BigDecimal lastIncome;
    @Column("last_expense")
    private BigDecimal lastExpense;
    @Column("created_at")
    private LocalDate createdAt;

    public void increaseBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

}
