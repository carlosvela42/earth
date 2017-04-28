package co.jp.nej.earth.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLBindings;

public class EModelUtil {

    @SuppressWarnings("rawtypes")
    public static <T> Predicate buildCondition(Map<Path<?>, Object> keyMap, RelationalPathBase<T> qObj,
            boolean withTable) {

        if (keyMap == null || keyMap.size() == 0) {
            return null;
        }

        Class<?> entityClass = qObj.getClass();

        String objName = withTable ? qObj.getTableName() : entityClass.getSimpleName();

        @SuppressWarnings("unchecked")
        PathBuilder<T> entityPath = new PathBuilder(entityClass, objName);

        Iterator<Entry<Path<?>, Object>> it = keyMap.entrySet().iterator();

        BooleanBuilder condition = new BooleanBuilder();
        while (it.hasNext()) {

            Map.Entry<Path<?>, Object> pair = (Map.Entry<Path<?>, Object>) it.next();

            Path<?> colName = pair.getKey();
            Object colValue = pair.getValue();

            Predicate predicate = null;
            if (colValue == null) {
                predicate = entityPath.get(colName).isNull();
            } else if (colValue instanceof String) {
                colValue = ((String) colValue).trim();
                StringExpression express = entityPath.get((StringPath) colName).trim();
                predicate = express.eq(Expressions.asSimple(colValue));
            } else {
                predicate = entityPath.get(colName).eq(Expressions.asSimple(colValue));
            }

            condition.and(predicate);
        }

        return condition.getValue();
    }

    public static String cleanQuery(SQLBindings bindings) {
        String query = bindings.getSQL();
        String tmp = StringUtils.EMPTY;
        for (Object binding : bindings.getBindings()) {

            if (binding instanceof String) {
                tmp = "'" + binding.toString() + "'";
            } else {
                tmp = binding.toString();
            }
            query = query.replaceFirst("\\?", tmp);
        }
        return query;
    }
}
