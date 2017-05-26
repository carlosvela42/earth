package co.jp.nej.earth.web.form;

import javax.validation.constraints.Pattern;

import co.jp.nej.earth.model.constant.Constant;

public class LicenseHistoryForm {

    @Pattern(regexp = Constant.Regex.DATE_TIME_REGEX, message="E0017,fromTime")
    private String fromDate;

    @Pattern(regexp = Constant.Regex.DATE_TIME_REGEX, message="E0017,toTime")
    private String toDate;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
