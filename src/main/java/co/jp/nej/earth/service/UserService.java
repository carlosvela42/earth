package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.entity.MgrUser;

public interface UserService {
    public List<Message> login(String userId, String password, HttpSession session) throws EarthException;
    public boolean logout(HttpSession session) throws EarthException;
    public List<MgrUser> getAll() ;
    public List<Message> validate(MgrUser mgrUser, boolean insert ) ;
    public boolean insertOne(MgrUser mgrUser) throws EarthException ;
    public boolean updateOne(MgrUser mgrUser) throws EarthException  ;
    public boolean deleteList(List<String> userIds) ;
    public Map<String,Object> getDetail(String userId) throws EarthException;
}