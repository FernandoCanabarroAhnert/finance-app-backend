package com.fernandocanabarro.finance_app_backend.transaction.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private Long id;
    @Column("wallet_id")
    private Long walletId;
    @Column("category_id")
    private Long categoryId;
    @Column("user_id")
    private String userId;
    private String type;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;

}
