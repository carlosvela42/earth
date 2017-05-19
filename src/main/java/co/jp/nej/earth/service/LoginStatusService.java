package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.entity.CtlLogin;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface LoginStatusService {

    List<CtlLogin> getAll(Long offset, Long limit, OrderSpecifier<String> orderByColumn) throws EarthException;

    CtlLogin insertOne(CtlLogin ctlLogin) throws EarthException;

    boolean deleteList(List<String> sessionIds) throws EarthException;
}
