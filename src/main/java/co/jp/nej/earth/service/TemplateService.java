package co.jp.nej.earth.service;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.entity.MgrProfile;
import co.jp.nej.earth.model.entity.MgrTemplate;
import co.jp.nej.earth.model.entity.MgrUser;

import java.util.List;
import java.util.Map;

public interface TemplateService {
    public Map<String,Object> getTemplateListInfo(String workspaceId, List<String> templateIds) throws EarthException;
    public List<MgrTemplate> getAllByWorkspace(String workspaceId) throws EarthException;
    public List<MgrTemplate> getTemplateByProfileId(List<String> profileIds) throws EarthException;
    public List<MgrWorkspace> getAllWorkspace() throws EarthException;
    public List<MgrTemplate> getTemplateListInfo(String workspaceId) throws EarthException;
    public List<UserAccessRight> getUserAuthority(TemplateKey templateKey) throws EarthException;
    public List<ProfileAccessRight> getProfileAuthority(TemplateKey templateKey) throws EarthException;
    public MgrTemplate getById(TemplateKey templateKey) throws EarthException;
    public List<MgrUser> getAllUser() throws EarthException;
    public List<MgrProfile> getAllProfile() throws EarthException;
    public boolean saveAuthority(TemplateKey templateKey, List<UserAccessRight> tUser, List<ProfileAccessRight> tProfile) throws EarthException;
}