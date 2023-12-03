package aysha.abatova.currencyconverter.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ComissionDto {
    @NotEmpty(message = "Поле не может быть пустым")
    private String charCode;
    @NotNull(message = "Поле не может быть пустым")
    @Min(0)
    private int comissionFrom;
    
    @NotNull(message = "Поле не может быть пустым")
    @Min(0)
    private int comissionTo;
    public int getComissionTo() {
        return comissionTo;
    }
    public String getCharCode() {
        return charCode;
    }
    public int getComissionFrom() {
        return comissionFrom;
    }
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }
    public void setComissionFrom(int comissionFrom) {
        this.comissionFrom = comissionFrom;
    }
    public void setComissionTo(int comissionTo) {
        this.comissionTo = comissionTo;
    }

    


}
