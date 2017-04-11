/**
 *
 */
package co.jp.nej.earth.model.constant;

/**
 * @author p-tvo-khanhnv
 */
public interface Constant {

    // Salt for encryptOneWay using SHA-512.
    public static final String SALT = "Earth123";

    // SYSTEM WORKSPACE.
    public static final String SYSTEM_WORKSPACE_ID = "workspace_system";

    // EARTH WORKSPACE.
    public static final String EARTH_WORKSPACE_ID = "workspace_earth";

    public static final String UTF_8 = "UTF-8";

    public static class LocalPart {
        public static final String CREATE_WORKITEM_REQUEST = "CreateWorkItemRequest";
        public static final String SEARCH_WORKITEM_REQUEST = "SearchWorkItemsRequest";
    }

    /**
     * Message Code and Message Content using in Login function
     * 
     * @author p-tvo-thuynd
     *
     */
    public static class MessageCodeLogin {
        public static final String USR_BLANK = "USR_BLANK";
        public static final String PWD_BLANK = "PWD_BLANK";
        public static final String INVALID_LOGIN = "INVALID_LOGIN";
    }

    public static class MessageUser {
        public static final String USR_BLANK = "USR_BLANK";
        public static final String USR_SPECIAL = "USR_SPECIAL";
        public static final String NAME_BLANK = "NAME_BLANK";
        public static final String CHANGEPWD_BLANK = "CHANGEPWD_BLANK";
        public static final String PWD_BLANK = "PWD_BLANK";
        public static final String PWD_CORRECT = "PWD_CORRECT";
        public static final String USR_EXIST = "USR_EXIST";
    }

    public static class MessageCodeWorkSpace {
        public static final String ID_BLANK = "ID_BLANK";
        public static final String SCHEMA_BLANK = "SCHEMA_BLANK";
        public static final String DBUSER_BLANK = "DBUSER_BLANK";
        public static final String DBPASS_BLANK = "DBPASS_BLANK";
        public static final String DBSERVER_BLANK = "DBSERVER_BLANK";
        public static final String OWNER_BLANK = "OWNER_BLANK";
        public static final String ISEXIT_WORKSPACE = "ISEXIT_WORKSPACE";
    }

    public static class Session {
        public static final String USER_INFO = "userInfo";
        public static final String MENU_STRUCTURE = "menuStructures";
        public static final String TEMPLATE_ACCESS_RIGHT_MAP = "templateAccessRightMap";
        public static final String MENU_ACCESS_RIGHT_MAP = "menuAccessRightMap";
    }

    public static class DatePattern {

        /** yyyy/MM/dd HH:mm:ss 形式 */
        public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

        /** yyyy/MM/dd HH:mm:ss.SSS 形式 */
        public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

        /** yyyy/MM/dd 形式 */
        public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd";

        public static final String YYYYMMDD_JA_FORMAT = "yyyy年MM月dd日";
    }

    public static class OperationDateProcess {
        public static final String OPERATION_DATE = "OperationDate";

        public static final String CURRENT_DATE = "currentDate";

        /** dd/MM/yyyy 形式 */
        public static final String DATE_FORMAT = "dd/MM/yyyy";
    }

    public static class RuleDefilePasswordPolicy {
        public static final String PASS_WHITESPACE = "ILLEGAL_WHITESPACE";
        public static final String PASS_UPPERCASE = "INSUFFICIENT_UPPERCASE";
        public static final String PASS_LOWERCASE = "INSUFFICIENT_LOWERCASE";
        public static final String PASS_DIGIT = "INSUFFICIENT_DIGIT";
        public static final String PASS_SPECIAL = "INSUFFICIENT_SPECIAL";
        public static final String PASS_LONG = "TOO_LONG";
        public static final String PASS_SHORT = "TOO_SHORT";

        // định nghĩa không cho phép nhập khoảng trắng
        public enum WhileSpace {
            WhileSpace("no");

            private String propertyName;

            WhileSpace(String name) {
                propertyName = name;
            }

            @Override
            public String toString() {
                return propertyName;
            }
        }
    }

    public static class Error_Code {
        public static final String E0001 = "E0001";
        public static final String E0002 = "E0002";
        public static final String E0003 = "E0003";
        public static final String E0004 = "E0004";
        public static final String E0005 = "E0005";
        public static final String E0006 = "E0006";
        public static final String E0007 = "E0007";
        public static final String E0008 = "E0008";
        public static final String E0009 = "E0009";
        public static final String E0010 = "E0010";
        public static final String E0011 = "E0011";
        public static final String E0012 = "E0012";
        public static final String E0013 = "E0013";
        public static final String E0014 = "E0014";
        public static final String E0015 = "E0015";
        public static final String E0016 = "E0016";
        public static final String E0017 = "E0017";
        public static final String E0018 = "E0018";
        public static final String E0019 = "E0019";
        public static final String E0020 = "E0020";

        public static final String E1008 = "E1008";
        public static final String E1009 = "E1009";
    }

    public static class Screen_Item {
        public static final String CODE_ID = "codeId";
        public static final String CODE_VALUE = "codeValue";
        public static final String CONFIG_VALUE = "configValue";
        public static final String DATA_DIRECTORY_ID = "dataDirectoryId";
        public static final String DB_PASSWORD = "dbPassword";
        public static final String DB_SERVER = "dbServer";
        public static final String DB_USER = "dbUser";
        public static final String DESCRIPTION = "description";
        public static final String DOCUMENT_DATA = "documentData";
        public static final String DOCUMENT_DATA_PATH = "documentDataPath";
        public static final String DOCUMENT_NO = "documentNo";
        public static final String DOCUMENT_TYPE = "documentType";
        public static final String END_TIME = "endTime";
        public static final String EVEN_TID = "eventId";
        public static final String FOLDER_ITEM_NO = "folderItemNo";
        public static final String FOLDER_PATH = "folderPath";
        public static final String FUNCTION_ID = "functionId";
        public static final String FUNCTION_NAME = "functionName";
        public static final String FUNCTION_CATEGORY_ID = "functionCategoryId";
        public static final String FUNCTION_CATEGORY_NAME = "functionCategoryName";
        public static final String FUNCTION_SORT_NO = "functionSortNo";
        public static final String FUNCTION_INFORMATION = "functionInformation";
        public static final String HISTORY_NO = "historyNo";
        public static final String LAST_HISTORY_NO = "lastHistoryNo";
        public static final String LAST_UPDATE_TIME = "lastUpdateTime";
        public static final String LAYER_NO = "layerNo";
        public static final String LDAP_IDENTIFIER = "ldapIdentifier";
        public static final String LOGIN_TIME = "loginTime";
        public static final String LOGOUT_TIME = "logoutTime";
        public static final String NAME = "name";
        public static final String OWNER = "owner";
        public static final String OWNER_ID = "ownerId";
        public static final String PAGE_COUNT = "pageCount";
        public static final String PASSWORD = "password";
        public static final String CONFIRM_PASSWORD = "Confirm Password";
        public static final String NEW_PASSWORD = "New Password";
        public static final String CHANGE_PASSWORD = "Change Password";
        public static final String CREATE_USER = "Create User";
        public static final String PASSWD = "passwd";
        public static final String PROCESS_DEFINITION = "processDefinition";
        public static final String PROCESS_ID = "processId";
        public static final String PROCESS_NAME = "processName";
        public static final String PROCESS_VERSION = "processVersion";
        public static final String PROFILE_ID = "profileId";
        public static final String PROFILE = "Profile";
        public static final String REPEAT_OPTION = "repeatOption";
        public static final String SCHEDULE_ID = "scheduleId";
        public static final String SCHEMA_NAME = "schemaName";
        public static final String SECTION = "section";
        public static final String SESSION_ID = "sessionId";
        public static final String SITE_ID = "siteId";
        public static final String SITE_MANAGEMENT_TYPE = "siteManagementType";
        public static final String START_TIME = "startTime";
        public static final String STATUS = "status";
        public static final String TASK_ID = "taskId";
        public static final String TEMPLATE_FIELD = "templateField";
        public static final String TEMPLATE_TYPE = "templateType";
        public static final String TEMPLATE_ID = "templateId";
        public static final String TEMPLATE_NAME = "templateName";
        public static final String TEMPLATE_TABLE_NAME = "templateTableName";
        public static final String USE_LICENSE_COUNT = "useLicenseCount";
        public static final String USER_ID = "userId";
        public static final String USER = "User";
        public static final String VARIABLE_NAME = "variableName";
        public static final String VIEW_INFORMATION = "viewInformation";
        public static final String WORKITEM_DATA = "workItemData";
        public static final String WORKITEM_ID = "workItemId";
        public static final String WORKSPACE_ID = "workspaceId";
        public static final String WORKSPACE_NAME = "workspaceName";
        public static final String PROCESS_TIME = "processTime";
    }

}
