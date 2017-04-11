package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;

import java.util.List;
import java.util.Map;

public interface TemplateAuthorityDao {

    public Map<TemplateKey, TemplateAccessRight> getMixAuthority(String userId, String workspaceId)
            throws EarthException;

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    public boolean deleteListByProfileIds(List<String> profileIds) throws EarthException;

    public boolean insertMixAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights) throws EarthException;

    public boolean deleteAllMixAuthority(TemplateKey templateKey) throws EarthException;

    public List<TemplateKey> getTemplateKeysByProfile(String profileId) throws EarthException;

    public List<UserAccessRight> getUserAuthority(TemplateKey templateKey) throws EarthException;

    public List<ProfileAccessRight> getProfileAuthority(TemplateKey templateKey) throws EarthException;

    public boolean deleteAllUserAuthority(TemplateKey templateKey) throws EarthException;

    public boolean insertUserAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights) throws EarthException;

    public boolean deleteAllProfileAuthority(TemplateKey templateKey) throws EarthException;

    public boolean insertProfileAuthority(TemplateKey templateKey, List<ProfileAccessRight> profileAccessRights) throws EarthException;
}