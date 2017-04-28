package co.jp.nej.earth.util;

import java.util.Date;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.id.EAutoIncrease;
import co.jp.nej.earth.id.EDateCount;
import co.jp.nej.earth.id.EWorkItemNo;
import co.jp.nej.earth.model.constant.Constant.EarthId;

/**
 * Generate Id for following object.
 * <p>
 * <ul>
 * <li>Process ID
 * <li>Work item ID
 * <li>Template ID
 * <li><strike>Profile ID
 * <li>User ID</strike>
 * <li>Workspace ID
 * <li>Data directory ID
 * <li>Site ID
 * <li>Code ID
 * <li>Schedule ID
 * <li>Event ID
 * <li>Session ID
 * <li><strike>Function ID
 * <li>Function classification ID
 * <li>History NO</strike>
 * <li>Latest history NO
 * <li>Folder Item NO
 * <li>Document NO
 * <li>Layer NO
 * </ul>
 * </p>
 * @author landd
 *
 */
public class IdFactory implements EDateCount, EWorkItemNo, EAutoIncrease {

    /**
     * process for auto increment field.
     * <p>
     * <ul>
     * <li>process id
     * <li>template id
     * <li>workspace id
     * <li>datadirectory id
     * <li>site id
     * <li>schedule id
     * </ul>
     */
    @Override
    public int getAutoId(EarthId earthId) throws EarthException {

        // get from MGR_INCREMENT table
        switch (earthId) {
        case PROCESS:
            break;
        case TEMPLATE:
            break;
        case WORKSPACE:
            break;
        case SITE:
            break;
        case DIRECTORY:
            break;
        case SCHEDULE:
            break;
        default:
            throw new EarthException("Not support type " + earthId + " in getAutoId method");
        }
        return 0;
    }

    @Override
    public int getNextHistoryNo(String workItemId) {
        // TODO increase and return LASTHISTORYNO in MGR_WORKITEMINCREMENT table
        // based on input workitem id
        return 0;
    }

    @Override
    public int getNextFolderItemNo(String workItemId) {
        // TODO increase and return FOLDERITEMNO in MGR_WORKITEMINCREMENT table
        // based on input workitem id
        return 0;
    }

    @Override
    public int getNextDocumentNo(String workItemId, int folderItemNo) {
        // TODO increase and return DOCUMENTNO in MGR_WORKITEMINCREMENT table
        // based on input [workitem Id], [folderItem No]
        return 0;
    }

    @Override
    public int getNextLayerNo(String workItemId, int folderItemNo, int documentNo) {
        // TODO increase and return LAYERNO in MGR_WORKITEMINCREMENT table based
        // on input [workitem Id], [folderItem No], [document No]
        return 0;
    }

    @Override
    public String getDateCountId(EarthId earthId) throws EarthException {

        // get current date string with yyyyMMdd format
        String currentShortDate;

        if (earthId == EarthId.WORKITEM) {
            // TODO later
            currentShortDate = DateUtil.getCurrentShortDateString();
        } else if (earthId == EarthId.EVENT) {

            // TODO later
            currentShortDate = DateUtil.getCurrentShortDateString();
        } else {
            throw new EarthException("Only support WorkItem & Event");
        }

        return currentShortDate + (new Date()).getTime();
    }
}
