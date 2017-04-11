package co.jp.nej.earth.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
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

            // use getString to get a String path

            Predicate predicate = colValue != null ? entityPath.get(colName).eq(Expressions.asSimple(colValue))
                    : entityPath.get(colName).isNull();
            condition.and(predicate);
        }

        return condition.getValue();
    }

    public static String cleanQuery(SQLBindings bindings) {
        String query = bindings.getSQL();
        for (Object binding : bindings.getBindings()) {
            query = query.replaceFirst("\\?", binding.toString());
        }
        return query;
    }

}
