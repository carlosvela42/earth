package co.jp.nej.earth.util;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.UserAccessRight;
import co.jp.nej.earth.model.entity.MgrUserProfile;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.ColumnNames;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    /**
     * Compare access right of same user from 2 list, then keep the<br>
     * UserAccessRight object which has lower access right.
     *
     * @param userAccessRights first list of UserAccessRight object.
     * @param userAccessRightByProfiles second kist of UserAccessRight object.
     * @return list of UserAccessRight objects which have lowest access right from 2 list above.
     */

    public static List<UserAccessRight> mixAuthority(List<UserAccessRight> userAccessRights,
            List<UserAccessRight> userAccessRightByProfiles) {
        for (UserAccessRight userAccessRightProfile : userAccessRightByProfiles) {
            int countMenu = 0;
            for (UserAccessRight userAccessRight : userAccessRights) {
                if (EStringUtil.contains(userAccessRight.getUserId(), userAccessRightProfile.getUserId())) {
                    if (userAccessRightProfile.getAccessRight().getValue() < userAccessRight.getAccessRight()
                            .getValue()) {
                        userAccessRight.setAccessRight(userAccessRightProfile.getAccessRight());
                    }
                    break;
                } else {
                    countMenu += 1;
                }

            }
            if (countMenu == userAccessRights.size()) {
                userAccessRights.add(userAccessRightProfile);
            }
        }
        return userAccessRights;
    }

    /**
     * get list of UserAccessRight object from resultSet.
     *
     * @param resultSet query result from DB.
     * @return list of UserAccessRight object.
     * @throws EarthException.
     */
    public static List<UserAccessRight> getUserAccessRightsFromResult(ResultSet resultSet) throws EarthException {
        try {
            List<UserAccessRight> userAccessRights = new ArrayList<UserAccessRight>();
            while (resultSet.next()) {
                UserAccessRight userAccessRight = new UserAccessRight();
                userAccessRight.setUserId(resultSet.getString(ColumnNames.USER_ID.toString()));
                userAccessRight.setAccessRight(
                        AccessRight.values()[resultSet.getInt(ColumnNames.ACCESS_AUTHORITY.toString())]);
                userAccessRights.add(userAccessRight);
            }
            return userAccessRights;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }

    /**
     * Assign access right for user based on profile's access right which user <br>
     * belongs to.
     *
     * @param mgrUserProfiles list of MgrUserProfile object.
     * @param mapAccessRightProfile map with key is profile id, and value is access right to template of that profile.
     * @return list of UserAccessRight object with access right of the profile which that user belong to.
     * @throws EarthException
     */
    public static List<UserAccessRight> getUserAccessRightProfiles(List<MgrUserProfile> mgrUserProfiles,
            Map<String, AccessRight> mapAccessRightProfile) throws EarthException {
        try {
            List<UserAccessRight> userAccessRights = new ArrayList<UserAccessRight>();
            for (MgrUserProfile mgrUserProfile : mgrUserProfiles) {
                AccessRight accessRight = mapAccessRightProfile.get(mgrUserProfile.getProfileId());
                if (!StringUtils.isEmpty(accessRight)) {
                    UserAccessRight userAccessRight = new UserAccessRight();
                    userAccessRight.setUserId(mgrUserProfile.getUserId());
                    userAccessRight.setAccessRight(accessRight);
                    userAccessRights.add(userAccessRight);
                }
            }
            return userAccessRights;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}