package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.entity.CtlLogin;
import co.jp.nej.earth.model.entity.MgrUser;

public interface UserService {
    List<Message> login(String userId, String password, HttpSession session) throws EarthException;

    boolean logout(HttpSession session) throws EarthException;

    List<MgrUser> getAll() throws EarthException;

    List<Message> validate(MgrUser mgrUser, boolean insert) throws EarthException;

    boolean insertOne(MgrUser mgrUser) throws EarthException;

    boolean insertList(String workspaceId, List<MgrUser> mgrUser) throws EarthException;

    boolean updateOne(MgrUser mgrUser) throws EarthException;

    boolean deleteList(List<String> userIds) throws EarthException;

    Map<String, Object> getDetail(String userId) throws EarthException;

    // FOR test generic
    List<CtlLogin> getAllMgrLogin(String workspaceId, Long offset, Long limit, OrderSpecifier<String> orderByColumn)
            throws EarthException;

    CtlLogin getCtlLoginDetail(Map<Path<?>, Object> condition) throws EarthException;

    long deleteCtlLogin(Map<Path<?>, Object> condition) throws EarthException;

    long deleteCtlLogins(List<Map<Path<?>, Object>> condition) throws EarthException;

    long deleteAllCtlLogins() throws EarthException;

    long addCtlLogin(CtlLogin login) throws EarthException;

//    boolean deleteListNew(List<String> userIds) throws EarthException;

    long updateCtlLogin(Map<Path<?>, Object> condition, Map<Path<?>, Object> updateMap) throws EarthException;

    List<CtlLogin> searchMgrLogin(String workspaceId, Predicate condition, Long offset, Long limit,
            OrderSpecifier<String> orderByColumn) throws EarthException;
}