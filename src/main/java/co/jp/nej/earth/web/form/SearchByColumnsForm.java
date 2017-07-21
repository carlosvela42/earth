package co.jp.nej.earth.web.form;

import java.util.List;

public class SearchByColumnsForm {
    private String templateId;
    private String templateType;
    private Long limit;
    private Long skip;
    private String valid;
    private List<SearchByColumnForm> searchByColumnForms;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getSkip() {
        return skip;
    }

    public void setSkip(Long skip) {
        this.skip = skip;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public List<SearchByColumnForm> getSearchByColumnForms() {
        return searchByColumnForms;
    }

    public void setSearchByColumnForms(List<SearchByColumnForm> searchByColumnForms) {
        this.searchByColumnForms = searchByColumnForms;
    }
}
