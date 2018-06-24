package restful.dto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="account")
public class Account {
	
	private String id;
	
	private BigDecimal amount;
	
	public Account() {
	}
	
	public Account(String id, BigDecimal amount) {
		this.id = id;
		this.amount = amount;
	}
	
	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
