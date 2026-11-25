package model;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
	private BigDecimal interestRate;

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
}
