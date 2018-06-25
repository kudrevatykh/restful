package restful.dto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="account")
public class Account {
	
	private String id;
	
	private BigDecimal amount;
	
	private long version;
	
	@SuppressWarnings("unused")//for jaxb
	private Account() {
	}
	
	public Account(String id, BigDecimal amount, long version) {
		this.id = id;
		this.amount = amount;
		this.version = version;
	}
	
	@XmlElement
	public String getId() {
		return id;
	}

	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}

	@XmlTransient
	public long getVersion() {
		return version;
	}

}
