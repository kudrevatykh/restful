package restful.dto;

import java.math.BigDecimal;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="account")
public class Account {
	
	private String id;
	
	private BigDecimal amount;
	
	private long version;
	
	public Account() {
	}
	
	public Account(String id, BigDecimal amount, long version) {
		this.id = id;
		this.amount = amount;
		this.setVersion(version);
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

	@XmlTransient
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;

        Account a = (Account) obj;

		return Objects.equals(id, a.id) &&
				Objects.equals(version, a.version);
	}

}
