package co.jp.nej.earth.web.form;

import java.util.List;

public class SearchByColumnForm {

    private String valid;
    private String type;
    private List<ColumnSearch> columnSearchs;

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ColumnSearch> getColumnSearchs() {
        return columnSearchs;
    }

    public void setColumnSearchs(List<ColumnSearch> columnSearchs) {
        this.columnSearchs = columnSearchs;
    }

}
