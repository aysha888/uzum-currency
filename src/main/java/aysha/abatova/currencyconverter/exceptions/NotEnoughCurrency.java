package aysha.abatova.currencyconverter.exceptions;

public class NotEnoughCurrency extends RuntimeException {
    private String errorCode = "66602";
    private String charCode;

    public NotEnoughCurrency(String charCode) {
        super("Not enough currency");
        this.charCode = charCode;
        
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getCharCode() {
        return charCode;
    }
}
