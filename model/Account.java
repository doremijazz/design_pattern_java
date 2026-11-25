package model;

import java.math.BigDecimal;

public abstract class Account {
	private int id;
	private String accountNumber;
	private BigDecimal balance;
	private Client client;

	protected Account(int id, String accountNumber, BigDecimal balance, Client client) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.client = client;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

    @Override
    public String toString() {
        return "Account [getId()=" + getId() + ", getAccountNumber()=" + getAccountNumber() + ", getBalance()="
                + getBalance() + ", getClient()=" + getClient() + "]";
    }

    
}
