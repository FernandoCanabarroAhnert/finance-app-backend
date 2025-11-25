package com.fernandocanabarro.finance_app_backend.transaction.enums;

public enum TransactionType {
    INCOME(1),
    EXPENSE(2);

    private int code;

    private TransactionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TransactionType valueOf(int code) {
        for (TransactionType value : TransactionType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType code: " + code);
    }

}
