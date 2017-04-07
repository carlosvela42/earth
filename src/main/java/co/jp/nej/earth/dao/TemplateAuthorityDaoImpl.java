package co.jp.nej.earth.dao;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.manager.connection.ConnectionManager;
import co.jp.nej.earth.manager.connection.EarthQueryFactory;
import co.jp.nej.earth.model.TemplateAccessRight;
import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.model.enums.ColumnNames;
import co.jp.nej.earth.model.sql.QCtlTemplate;
import co.jp.nej.earth.model.sql.QMgrTemplateU;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TemplateAuthorityDaoImpl implements TemplateAuthorityDao{

    public Map<TemplateKey, TemplateAccessRight> getMixAuthority(String userId, String workspaceId) throws EarthException {
        Map<TemplateKey, TemplateAccessRight> templateAccessRightMap = new HashMap<TemplateKey, TemplateAccessRight>();
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            ResultSet resultSet = ConnectionManager.getEarthQueryFactory(workspaceId)
                    .select(qCtlTemplate.templateId, qCtlTemplate.accessAuthority).from(qCtlTemplate)
                    .where(qCtlTemplate.userId.eq(userId)).getResults();
            TemplateAccessRight templateAccessRight = null;
            TemplateKey templateKey = null;
            while (resultSet.next()) {
                templateAccessRight = new TemplateAccessRight();
                templateKey = new TemplateKey();
                templateKey.setWorkspaceId(workspaceId);
                templateKey.setTemplateId(resultSet.getString(ColumnNames.TEMPLATE_ID.toString()));
                templateAccessRight.setTemplateKey(templateKey);
                templateAccessRight.setAccessRight(AccessRight.values()[resultSet.getInt(ColumnNames.ACCESS_AUTHORITY.toString())]);
                templateAccessRightMap.put(templateKey, templateAccessRight);
            }
        } catch (Exception e) {
            throw new EarthException(e.getMessage());
        }
        return templateAccessRightMap;
    }

    public boolean deleteListByUserIds(List<String> userIds) throws EarthException {
        try {
            QCtlTemplate qCtlTemplate = QCtlTemplate.newInstance();
            QMgrTemplateU qMgrTemplateU = QMgrTemplateU.newInstance();
            EarthQueryFactory earthQueryFactory = ConnectionManager.getEarthQueryFactory(Constant.EARTH_WORKSPACE_ID);
            earthQueryFactory.delete(qCtlTemplate).where(qCtlTemplate.userId.in(userIds)).execute();
            earthQueryFactory.delete(qMgrTemplateU).where(qMgrTemplateU.userId.in(userIds)).execute();
            return true;
        } catch (Exception ex) {
            throw new EarthException(ex.getMessage());
        }
    }
}
