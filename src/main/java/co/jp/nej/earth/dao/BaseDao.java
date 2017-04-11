package co.jp.nej.earth.dao;

import java.util.List;
import java.util.Map;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.BaseModel;

/**
 * Base DAO Model.
 * @author landd
 *
 * @param <T>
 */
public interface BaseDao<T extends BaseModel<T>> {

    /**
     * Get all record of type T.
     * @param workspaceId the workspace where item reside
     * @return List of T record
     * @throws EarthException General Exception
     */
    List<T> findAll(String workspaceId, Long offset, Long limit, OrderSpecifier<String> orderByColumn) throws EarthException;

    T findOne(String workspaceId, Map<Path<?>, Object> keyMap) throws EarthException;

    boolean delete(String workspaceId, Map<Path<?>, Object> keyMap) throws EarthException;

    boolean deleteList(String workspaceId, List<Map<Path<?>, Object>> keyMap) throws EarthException;
    
    boolean add(String workspaceId, T t) throws EarthException;
    
    boolean update(String workspaceId, Map<Path<?>, Object> keyMap, Map<Path<?>, Object> valueMap) throws EarthException;
}
