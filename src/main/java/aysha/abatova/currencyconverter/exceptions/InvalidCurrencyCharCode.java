package aysha.abatova.currencyconverter.exceptions;

public class InvalidCurrencyCharCode extends RuntimeException {
    private String errorCode = "66601";
    private String charCode;

    public InvalidCurrencyCharCode(String charCode) {
        super("Invalid charcode");
        this.charCode = charCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getCharCode() {
        return charCode;
    }
}

