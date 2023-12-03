package aysha.abatova.currencyconverter.entities;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String numCode;
    @Column(unique = true)
    private String charCode;
    private int nominal;
    private String currency_name;

    private BigDecimal rate;

    private String date;

    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getComissionFrom() {
        return comissionFrom;
    }

    public void setComissionFrom(int comission_from) {
        this.comissionFrom = comission_from;
    }

    public int getComissionTo() {
        return comissionTo;
    }

    public void setComissionTo(int comission_to) {
        this.comissionTo = comission_to;
    }

    private int comissionFrom;

    private int comissionTo;

    @OneToOne(mappedBy = "currency", fetch = FetchType.LAZY)
    private Account account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String name) {
        this.currency_name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
