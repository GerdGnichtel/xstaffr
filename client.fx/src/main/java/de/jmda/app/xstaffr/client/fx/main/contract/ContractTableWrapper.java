package de.jmda.app.xstaffr.client.fx.main.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

import de.jmda.app.xstaffr.common.domain.Contract;

public final class ContractTableWrapper
{
	private Contract contract;

	public ContractTableWrapper(Contract contract) { this.contract = contract; }

	public Contract getContract() { return contract; }
	public LocalDate getInception() { return contract.getInception(); }
	public LocalDate getExpiration() { return contract.getExpiration(); }
	public BigDecimal getHourlyRate() { return contract.getHourlyRate(); }
	public String getText() { return contract.getText(); }
}