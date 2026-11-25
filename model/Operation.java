package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Operation {
	private int id;
	private BigDecimal amount;
	private LocalDateTime date;
	private OperationType type;
	private Account account;

    public Operation(int id, BigDecimal amount, LocalDateTime date, OperationType type, Account account) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.account = account;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OperationType getType() {
        return this.type;
    }
    public void setType(OperationType type) {
        this.type = type;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Operation [getId()=" + getId() + ", getAmount()=" + getAmount() + ", getDate()=" + getDate()
                + ", getType()=" + getType() + ", getAccount()=" + getAccount() + "]";
    }

}


