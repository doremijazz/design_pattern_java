package model;

import java.math.BigDecimal;

public class SavingsAccount extends Account {

    private BigDecimal interestRate;

    public SavingsAccount(int id, String accountNumber, BigDecimal balance, Client client, BigDecimal interestRate) {
        super(id, accountNumber, balance, client);
        this.interestRate = interestRate;
    }

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
}
