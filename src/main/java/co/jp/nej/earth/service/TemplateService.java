package co.jp.nej.earth.service;

import java.util.List;
import java.util.Map;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.MgrUser;

public interface TemplateService {
    Map<String, Object> getTemplateListInfo(String workspaceId, List<String> templateIds) throws EarthException;

    List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException;

    List<MgrTemplate> getTemplateByProfileId(List<String> profileIds) throws EarthException;

    List<MgrWorkspace> getAllWorkspace() throws EarthException;

    List<MgrTemplate> getTemplateListInfo(String workspaceId) throws EarthException;

    List<UserAccessRight> getUserAuthority(TemplateKey templateKey) throws EarthException;

    List<ProfileAccessRight> getProfileAuthority(TemplateKey templateKey) throws EarthException;

    MgrTemplate getById(TemplateKey templateKey) throws EarthException;

    List<MgrUser> getAllUser() throws EarthException;

    List<MgrProfile> getAllProfile() throws EarthException;

    long saveAuthority(TemplateKey templateKey, List<UserAccessRight> tUser, List<ProfileAccessRight> tProfile)
            throws EarthException;

    List<MgrTemplate> getTemplateByType(String workspaceId, String templateType) throws EarthException;

    boolean deleteTemplates(List<String> templateIds, String workspaceId) throws EarthException;

    boolean insertOne(String workspaceId, MgrTemplate mgrTemplate) throws EarthException;

    boolean updateOne(String workspaceId, MgrTemplate mgrTemplate) throws EarthException;

    List<Message> checkExistsTemplate(MgrTemplate mgrTemplate) throws EarthException;
}