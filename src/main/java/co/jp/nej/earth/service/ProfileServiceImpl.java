package co.jp.nej.earth.service;

import co.jp.nej.earth.dao.ProfileDao;
import co.jp.nej.earth.dao.UserDao;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.entity.MgrProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private UserDao userDao;

    public List<MgrProfile> getAll() {
        try {
            return profileDao.getAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getDetail(String profileId) throws EarthException {
        try {
            Map<String, Object> detail = new HashMap<String, Object>();
            detail.put("mgrProfile", profileDao.getById(profileId));
            List<String> users=userDao.getUserIdsByProfileId(profileId);
            String userIds="";
            for (String s : users)
            {
                userIds += s + ",";
            }
            if (userIds.length()>0) userIds=userIds.substring(0,userIds.length()-1);
            detail.put("userIds",userIds);
            detail.put("mgrUsers", userDao.getAll());
            return detail;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public List<Message> validate(MgrProfile mgrProfile) {
        List<Message> listMessage = new ArrayList<Message>();
        try {

            return listMessage;
        } catch (Exception ex) {
            Message message = new Message("",
                    messageSource.getMessage("E1009", new String[]{""}, Locale.ENGLISH));
            listMessage.add(message);
            return listMessage;
        }
    }

    public boolean insertAndAssignUsers(MgrProfile mgrProfile, List<String> userIds) throws EarthException {
        try {
           MgrProfile insertProfile= profileDao.insertOne(mgrProfile);
           boolean assignUser= profileDao.assignUsers(mgrProfile.getProfileId(),userIds);
           if (insertProfile != null && assignUser){
               return true;
           }
            return false;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean updateAndAssignUsers(MgrProfile mgrProfile, List<String> userIds) throws EarthException {
        try {
            MgrProfile updateProfile = profileDao.updateOne(mgrProfile);
            boolean unAssignUser= profileDao.unAssignAllUsers(mgrProfile.getProfileId());
            boolean assignUser= profileDao.assignUsers(mgrProfile.getProfileId(),userIds);
            if (updateProfile != null && assignUser && unAssignUser) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    public boolean deleteList(List<String> profileIds) {
        try {

            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
