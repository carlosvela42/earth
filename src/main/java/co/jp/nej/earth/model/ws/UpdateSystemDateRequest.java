package co.jp.nej.earth.model.ws;

import co.jp.nej.earth.model.constant.Constant.DatePattern;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class UpdateSystemDateRequest extends Request {

    @NotEmpty(message = ("E001,dateInput"))
    @Pattern(regexp = DatePattern.DATE_FORMAT_YYYY_MM_DD, message = ("E0011,dateInput"))
    private String dateInput;

    /**
     * @return the dateInput
     */
    public String getDateInput() {
        return dateInput;
    }

    /**
     * @param dateInput the dateInput to set
     */
    public void setDateInput(String dateInput) {
        this.dateInput = dateInput;
    }

}
