package co.jp.nej.earth.model.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import co.jp.nej.earth.model.constant.Constant.DatePattern;

public class UpdateSystemDateForm extends RestToken {

    @NotEmpty(message = ("E0002,dateInput"))
    @Pattern(regexp = DatePattern.DATE_FORMAT_YYYY_MM_DD, message = ("E0017,dateInput"))
    private String dateInput;

    /**
     * @return the dateInput
     */
    public String getDateInput() {
        return dateInput;
    }

    /**
     * @param dateInput
     *            the dateInput to set
     */
    public void setDateInput(String dateInput) {
        this.dateInput = dateInput;
    }

}
