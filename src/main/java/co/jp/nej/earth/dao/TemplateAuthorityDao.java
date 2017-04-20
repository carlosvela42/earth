package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.ProfileAccessRight;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.UserAccessRight;

import java.util.List;
import java.util.Map;

public interface TemplateAuthorityDao {

    Map<TemplateKey, TemplateAccessRight> getMixAuthority(String userId, String workspaceId)
            throws EarthException;

    boolean deleteListByUserIds(List<String> userIds) throws EarthException;

    boolean deleteListByProfileIds(List<String> profileIds) throws EarthException;

    boolean insertMixAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights) throws EarthException;

    boolean deleteAllMixAuthority(TemplateKey templateKey) throws EarthException;

    List<TemplateKey> getTemplateKeysByProfile(String profileId) throws EarthException;

    List<UserAccessRight> getUserAuthority(TemplateKey templateKey) throws EarthException;

    List<ProfileAccessRight> getProfileAuthority(TemplateKey templateKey) throws EarthException;

    boolean deleteAllUserAuthority(TemplateKey templateKey) throws EarthException;

    boolean insertUserAuthority(TemplateKey templateKey, List<UserAccessRight> userAccessRights) throws EarthException;

    boolean deleteAllProfileAuthority(TemplateKey templateKey) throws EarthException;

    boolean insertProfileAuthority(TemplateKey templateKey, List<ProfileAccessRight> profileAccessRights)
            throws EarthException;
}