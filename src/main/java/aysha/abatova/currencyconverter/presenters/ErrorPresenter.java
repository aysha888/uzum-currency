package aysha.abatova.currencyconverter.presenters;

public class ErrorPresenter {
    private String errorCode;
    private String errorMessage;
    private String charCode;

    public ErrorPresenter(String errorCode, String errorMessage, String charCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.charCode = charCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCharCode() {
        return charCode;
    }
}
