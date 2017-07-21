package co.jp.nej.earth.service;

import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.sql.QBase;
import co.jp.nej.earth.util.ConversionUtil;
import co.jp.nej.earth.util.CryptUtil;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.web.form.ColumnSearch;
import co.jp.nej.earth.web.form.SearchByColumnForm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SearchColumn {

    private static final String TYPE_STRING_PATH = "CHAR";
    private static final Logger LOG = LoggerFactory.getLogger(SearchColumn.class);

    public BooleanBuilder search(QBase<?> qTemplateData, SearchByColumnForm searchByColumnForm) {
        BooleanBuilder condition = new BooleanBuilder();
        List<ColumnSearch> columnSearches = searchByColumnForm.getColumnSearchs();
        String valid = searchByColumnForm.getValid();
        String type = searchByColumnForm.getType();
        if (columnSearches != null) {
            for (ColumnSearch search : columnSearches) {
                if (search.getValue() != null && !EStringUtil.isEmpty(search.getValue())) {
                    Path<?> p = getPathFromName(qTemplateData, search.getName(), type);
                    Predicate pre1 = null;
                    String value=search.getValue().trim();
                    if(search.isEncrypted()){
                        try {
                            value= CryptUtil.encryptData(search.getValue().trim());
                        } catch (Exception ex) {
                            LOG.error(ex.getMessage());
                        }

                    }
                    if (p instanceof StringPath) {
                        StringExpression p1 = (StringExpression) p;
                        switch (search.getOperator().toUpperCase()) {
                            case "EQUAL":
                                pre1 = p1.trim().eq(value);
                                break;
                            case "NOTEQUAL":
                                pre1 = p1.trim().ne(value);
                                break;
                            case "OVER":
                                pre1 = p1.trim().gt(value);
                                break;
                            case "EQUALOVER":
                                pre1 = p1.trim()
                                    .gt(value).or(p1.trim().eq(value));
                                break;
                            case "UNDER":
                                pre1 = p1.trim().lt(value);
                                break;
                            case "EQUALUNDER":
                                pre1 = p1.trim().lt(value).or(p1.trim()
                                    .eq(value));
                                break;
                            case "LIKE":
                                pre1 = p1.upper().like(Expressions.constant(
                                    "%" + search.getValue().toUpperCase() + "%"));
                                break;
                            case "NOTLIKE":
                                pre1 = p1.upper().notLike(Expressions.constant(
                                    "%" + search.getValue().toUpperCase() + "%"));
                                break;
                            case "ISNULL":
                                pre1 = p1.trim().isNull();
                                break;
                            case "ISNOTNULL":
                                pre1 = p1.trim().isNotNull();
                                break;
                            case "ISEMPTY":
                                pre1 = p1.trim().isEmpty();
                                break;
                            case "ISNOTEMPTY":
                                pre1 = p1.trim().isNotEmpty();
                                break;
                            default:
                                break;
                        }
                    } else if (p instanceof NumberPath) {
                        NumberExpression<Integer> p1 = (NumberExpression<Integer>) p;
                        switch (search.getOperator().toUpperCase()) {
                            case "EQUAL":
                                pre1 = p1.eq(Integer.parseInt(search.getValue()));
                                break;
                            case "NOTEQUAL":
                                pre1 = p1.ne(Integer.parseInt(search.getValue()));
                                break;
                            case "OVER":
                                pre1 = p1.gt(Integer.parseInt(search.getValue()));
                                break;
                            case "EQUALOVER":
                                pre1 = p1.gt(Integer.parseInt(search.getValue()))
                                        .or(p1.eq(Integer.parseInt(search.getValue())));
                                break;
                            case "UNDER":
                                pre1 = p1.lt(Integer.parseInt(search.getValue()));
                                break;
                            case "EQUALUNDER":
                                pre1 = p1.lt(Integer.parseInt(search.getValue()))
                                    .or(p1.eq(Integer.parseInt(search.getValue())));
                                break;
                            case "ISNULL":
                                pre1 = p1.isNull();
                                break;
                            case "ISNOTNULL":
                                pre1 = p1.isNotNull();
                                break;
                            default:
                                break;
                        }
                    }
                    if(pre1!=null){
                        switch (valid.toUpperCase()) {
                            case Constant.WorkItemList.WHERE_AND:
                                condition.and(pre1);
                                break;
                            case Constant.WorkItemList.WHERE_OR:
                                condition.or(pre1);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return condition;
    }

    public BooleanBuilder searchColumns(QBase<?> qTemplateData, String valid,
                                        List<SearchByColumnForm> searchByColumnForms) {
        BooleanBuilder condition = new BooleanBuilder();
        if (searchByColumnForms != null) {
            for (SearchByColumnForm searchByColumnForm : searchByColumnForms) {
                if (searchByColumnForm != null) {
                    switch (valid.toUpperCase()) {
                        case Constant.WorkItemList.WHERE_AND:
                            condition.and(search(qTemplateData, searchByColumnForm));
                            break;
                        case Constant.WorkItemList.WHERE_OR:
                            condition.or(search(qTemplateData, searchByColumnForm));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return condition;
    }

    private Path<?> getPathFromName(QBase<?> qTemplateData, String name, String type) {
        if (EStringUtil.contains(type, TYPE_STRING_PATH)) {
            return (Path<?>) ConversionUtil.castObject(qTemplateData.getFieldString(name),Path.class);
        } else {
            return (Path<?>) ConversionUtil.castObject(qTemplateData.getFieldNumber(name),Path.class);
        }
    }

}
