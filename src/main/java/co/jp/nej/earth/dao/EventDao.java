package co.jp.nej.earth.dao;

import java.util.List;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.CtlEvent;

/**
 * Created by minhtv on 3/29/2017.
 */
public interface EventDao {
    public boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    public long insertEvent(CtlEvent event) throws EarthException;
}
