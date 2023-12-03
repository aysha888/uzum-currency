package aysha.abatova.currencyconverter.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;


public class ConversionDto {
    @NotEmpty(message = "Поле не может быть пустым")
    private String from;

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @NotEmpty(message = "Поле не может быть пустым")
    private String to;




    public String getFrom() {
        return from;
    }

    public void setFrom(String currencyFromCode) {
        this.from = currencyFromCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String currencyToCode) {
        this.to = currencyToCode;
    }



}
