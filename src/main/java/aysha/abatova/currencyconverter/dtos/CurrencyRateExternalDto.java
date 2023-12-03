package aysha.abatova.currencyconverter.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyRateExternalDto {
    private int id;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Ccy")
    private String ccy;
    @JsonProperty("CcyNm_RU")
    private String ccyNm_RU;
    @JsonProperty("CcyNm_UZ")
    private String ccyNm_UZ;
    @JsonProperty("CcyNm_UZC")
    private String ccyNm_UZC;
    @JsonProperty("CcyNm_EN")
    private String ccyNm_EN;
    @JsonProperty("Nominal")
    private String nominal;
    @JsonProperty("Rate")
    private String rate;
    @JsonProperty("Diff")
    private String diff;
    @JsonProperty("Date")
    private String date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getCcyNm_RU() {
        return ccyNm_RU;
    }

    public void setCcyNm_RU(String ccyNm_RU) {
        this.ccyNm_RU = ccyNm_RU;
    }

    public String getCcyNm_UZ() {
        return ccyNm_UZ;
    }

    public void setCcyNm_UZ(String ccyNm_UZ) {
        this.ccyNm_UZ = ccyNm_UZ;
    }

    public String getCcyNm_UZC() {
        return ccyNm_UZC;
    }

    public void setCcyNm_UZC(String ccyNm_UZC) {
        this.ccyNm_UZC = ccyNm_UZC;
    }

    public String getCcyNm_EN() {
        return ccyNm_EN;
    }

    public void setCcyNm_EN(String ccyNm_EN) {
        this.ccyNm_EN = ccyNm_EN;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
