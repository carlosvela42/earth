/**
 *
 */
package co.jp.nej.earth.model.constant;

/**
 * @author p-tvo-khanhnv
 */
public class Constant {

    // Salt for encryptOneWay using SHA-512.
    public static final String SALT = "Earth123";

    // SYSTEM WORKSPACE.
    public static final String SYSTEM_WORKSPACE_ID = "-1";

    // EARTH WORKSPACE.
    public static final String EARTH_WORKSPACE_ID = "-2";

    public static final String UTF_8 = "UTF-8";

    public static final String NUM_1 = "1";
    public static final String NUM_2 = "2";
    public static final String DIVISION = "DIVISION";
    public static final String AVAILABLELICENCECOUNT = "AVAILABLELICENCECOUNT";
    public static final String ENTIRE_SYSTEM = "entire_system";

    public static class LocalPart {
        public static final String CREATE_WORKITEM_REQUEST = "CreateWorkItemRequest";
        public static final String SEARCH_WORKITEM_REQUEST = "SearchWorkItemsRequest";
    }

    public static class Regex {
        public static final String DATE_TIME_REGEX = "^$|\\d{4}/([1][0-2]|[0][0-9])/"
                + "([3][0-1]|[1-2][0-9]|[0][1-9]|[1-9]) "
                + "([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]:([0-5][0-9]|[6][0]).\\d{3}";
    }

    /**
     * Message Code and Message Content using in Login function
     *
     * @author p-tvo-thuynd
     */
    public static class MessageCodeLogin {
        public static final String USR_BLANK = "USR_BLANK";
        public static final String PWD_BLANK = "PWD_BLANK";
        public static final String INVALID_LOGIN = "INVALID_LOGIN";
    }

    public static class Directory {
        public static final String IS_EXIT_DIRECTORY = "IS_EXIT_DIRECTORY";
        public static final int CONVERT = 1024 * 1024;
        public static final int DEFAULT_VALUE = 50000;
        public static final int YES = 1;
    }

    public static class MessageUser {
        public static final String USR_BLANK = "USR_BLANK";
        public static final String USR_SPECIAL = "USR_SPECIAL";
        public static final String NAME_BLANK = "NAME_BLANK";
        public static final String NAME_SPECIAL = "NAME_SPECIAL";
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
        public static final String WORKSPACES = "workspaces";
        public static final String LAST_REQUEST_VIEW = "lastRequestView";
        public static final String MESSAGES = "messages";
        public static final String LIST_SEARCH_CONDITION = "listSearchCondition";
        public static final String OPEN_PROCESS_MODE = "openProcessMode";
        public static final String SEARCH_BY_COLUMNS_FORM = "searchByColumnsForm";
        public static final String SESSION_ID = "sessionId";

    }

    public static class ScreenKey {
        public static final String WORKITEM ="WORKITEM";
        public static final String LICENSE="LICENSE";
        public static final String LOG="LOG";
        public static final String LOGIN_STATUS= "LOGIN_STATUS";
        public static final String PROCESS_SCHEDULE ="PROCESS_SCHEDULE";
        public static final String PROCESS_WORKSPACE="PROCESS_WORKSPACE";
        public static final String PROCESS_DIRECTORY ="PROCESS_DIRECTORY";
        public static final String PROCESS_SITE="PROCESS_SITE";
        public static final String PROCESS_PROCESS = "PROCESS_PROCESS";
        public static final String PROCESS_TEMPLATE="PROCESS_TEMPLATE";
        public static final String WORKITEM_TEMPLATE="WORKITEM_TEMPLATE";
        public static final String FOLDERITEM_TEMPLATE="FOLDERITEM_TEMPLATE";
        public static final String DOCUMENT_TEMPLATE= "DOCUMENT_TEMPLATE";
        public static final String LAYER_TEMPLATE= "LAYER_TEMPLATE";
        public static final String AUTHORITY_MENU = "AUTHORITY_MENU";
        public static final String AUTHORITY_TEMPLATE = "AUTHORITY_TEMPLATE";
        public static final String USER_PROFILE = "USER_PROFILE";
        public static final String USER = "USER";
    }

    public static class View {
        public static final String HOME = "home/home";
        public static final String LOGIN = "login/login";
        public static final String IMAGE_VIEWER = "imageViewer/imageViewer";
        public static final String IMAGE_VIEWER_TEMPLATE = "imageViewer/template";
    }

    public static class DatePattern {

        /**
         * yyyy/MM/dd HH:mm:ss 形式
         */
        public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

        /**
         * yyyyMMddHHmmss999 形式
         */
        public static final String DATE_FORMAT_YYYYMMDDHHMMSS999 = "yyyyMMddHHmmss999";

        /**
         * yyyyMMddHHmmss999 形式
         */
        public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

        /**
         *
         */
        public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

        /**
         * yyyy/MM/dd 形式
         */
        public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd";

        public static final String YYYYMMDD_JA_FORMAT = "yyyy年MM月dd日";

        /**
         * yyyyMMdd 形式
         */
        public static final String YYYYMMDD = "yyyyMMdd";
    }

    public static class AgentBatch {
        public static final String OPERATION_DATE = "OperationDate";

        public static final String CURRENT_DATE = "currentDate";

        /**
         * dd/MM/yyyy 形式
         */
        public static final String DATE_FORMAT = "dd/MM/yyyy";

        public static final String STATUS_EDIT = "EDIT";

        public static final String STATUS_EDITTING = "EDITTING";

        public static final String STATUS_OPEN = "OPEN";

        // the parameters of job
        public static final String PRE_TRIGER_KEY = "T";
        public static final String PRE_JOB_KEY = "J";
        public static final String P_USER_ID = "user_id";
        public static final String P_WORKSPACE_ID = "workspace_id";
        public static final String P_SCHEDULE_ID = "schedule_id";
        public static final String P_END_TIME = "end_time";
        public static final String P_INTERVAL_SECOND = "interval_second";
        public static final String P_STATUS = "status";
        public static final String P_PROCESS_ID = "process_id";
        public static final String P_HOSTNAME = "host";

        // the status of schedule item
        public static final String SCHEDULE_ENABLE = "enable";
        public static final String SCHEDULE_DISABLE = "disable";
    }

    public static class RuleDefilePasswordPolicy {
        public static final String PASS_WHITESPACE = "ILLEGAL_WHITESPACE";
        public static final String PASS_UPPERCASE = "INSUFFICIENT_UPPERCASE";
        public static final String PASS_LOWERCASE = "INSUFFICIENT_LOWERCASE";
        public static final String PASS_DIGIT = "INSUFFICIENT_DIGIT";
        public static final String PASS_SPECIAL = "INSUFFICIENT_SPECIAL";
        public static final String PASS_LONG = "TOO_LONG";
        public static final String PASS_SHORT = "TOO_SHORT";

        public static final String NO_WHITE_SPACE = "no";
    }

    public static class ErrorCode {
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
        public static final String E0021 = "E0021";
        public static final String E0022 = "E0022";
        public static final String E0023 = "E0023";
        public static final String E0024 = "E0024";
        public static final String E0025 = "E0025";
        public static final String E0026 = "E0026";
        public static final String E0027 = "E0027";
        public static final String E0028 = "E0028";
        public static final String E0029 = "E0029";
        public static final String E0030 = "E0030";
        public static final String E0031 = "E0031";

        public static final String E1008 = "E1008";
        public static final String E1009 = "E1009";
        public static final String E1010 = "E1010";
        public static final String E1011 = "E1011";
        public static final String E1012 = "E1012";
        public static final String E1013 = "E1013";
        public static final String E_TYPE_MISMATCH = "typeMismatch";

        public static final String SESSION_INVALID = "session.invalid";
    }

    public static class ScreenItem {
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
        public static final String PROCESS_NAME = "processName";
        public static final String PROCESS_VERSION = "processVersion";
        public static final String PROCESS_SERVICE = "processService";
        public static final String PROFILE_ID = "profile.id";
        public static final String PROFILE_DESCRIPTION = "profile.description";
        public static final String PROFILE = "Profile";
        public static final String REPEAT_OPTION = "repeatOption";
        public static final String RUNINTERVAL_DAY = "runintervalDay";
        public static final String RUNINTERVAL_HOUR = "runintervalHour";
        public static final String RUNINTERVAL_MINUTE = "runintervalMinute";
        public static final String RUNINTERVAL_SECOND = "runintervalSecond";

        public static final String SCHEDULE_ID = "scheduleId";
        public static final String SCHEMA_NAME = "schemaName";
        public static final String SECTION = "section";
        public static final String SESSION_ID = "sessionId";
        public static final String SITE_ID = "siteId";
        public static final String SITE_MANAGEMENT_TYPE = "siteManagementType";
        public static final String START_TIME = "startTime";
        public static final String STATUS = "status";
        public static final String TASK_NAME = "taskName";
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
        public static final String FROM_DATE = "fromDate";
        public static final String TO_DATE = "toDate";
    }

    public static class WorkSpace {
        public static final String ID_BLANK = "ID_BLANK";
        public static final String SCHEMA_BLANK = "SCHEMA_BLANK";
        public static final String DBUSER_BLANK = "DBUSER_BLANK";
        public static final String DBPASS_BLANK = "DBPASS_BLANK";
        public static final String DBSERVER_BLANK = "DBSERVER_BLANK";
        public static final String OWNER_BLANK = "OWNER_BLANK";
        public static final String ISEXIT_WORKSPACE = "ISEXIT_WORKSPACE";
        public static final String ISUSE_WORKSPACE = "ISUSE_WORKSPACE";
        public static final String CHARACTER_COMMON = "c##";
        public static final String CHARACTER_REPLACE = "##";
        public static final String ORACLE = "ORACLE";
        public static final String SQL = "SQL";
        public static final String CREATE_USER = "CREATE USER ";
        public static final String IDENTIFIED_BY = " IDENTIFIED BY ";
        public static final String GRANT_SESSION = "Grant Create Session to ";
        public static final String GRANT_DBA = "grant dba to ";
        public static final String DROP_USER = "Drop User ";
        public static final String CASCADE = " Cascade ";
        public static final String ALTER_USER = " ALTER USER ";
        public static final Integer ORACLE_PORT = 1521;
        public static final Integer SQL_PORT = 1433;
        public static final String CREATE_DATABASE = "CREATE DATABASE ";
        public static final String CREATE_LOGIN = "Create Login ";
        public static final String ALTER_LOGIN = "Alter Login ";
        public static final String WITH_PASSWORD = " WITH PASSWORD = '";
        public static final String FOR_LOGIN = " FOR LOGIN = ";
        public static final String WITH_DEFAULT_SCHEMA = " WITH DEFAULT_SCHEMA = ";
        public static final String USE = "USE ";
        public static final String EXEC_SP_ADD_ROLE_MEMBER = " exec sp_addrolemember 'db_owner', '";

        public static final String IP_VALIDATION = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        public static final String SPECIAL_VALIDATION = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~";
        public static final String ID_PATTERN = "[0-9]+";
        public static final String STRING_PATTERN = "[a-zA-Z]+";
        public static final String MOBILE_PATTERN = "[0-9]{10}";
    }

    public static class LicenseHistory {
        public static final String LICENSEHISTORY = "licenseHistory/licenseHistory";
    }

    public static class Template {
        public static final String NOT_DELETE = "NOT_DELETE";
        public static final String ID_BLANK = "ID_BLANK";
        public static final String TEMPLATE_NAME_BLANK = "TEMPLATE_NAME_BLANK";
        public static final String TEMPLATE_TABLE_NAME_BLANK = "TEMPLATE_TABLE_NAME_BLANK";
        public static final String IS_EXIT_TEMPLATE = "ISEXIT_TEMPLATE";
        public static final String IS_EXIT_TABLE = "IS_EXIT_TABLE";
        public static final String TEMPLATE_FIELD_BLANK = "TEMPLATE_FIELD_BLANK";
        public static final int FIELD_TYPTE_1 = 1;
        public static final int FIELD_TYPTE_2 = 2;
        public static final int FIELD_TYPTE_3 = 3;
        public static final int FIELD_TYPTE_4 = 4;
    }

    public static class Site {
        public static final Integer FIRST = 1;
    }

    public static class WorkItemList {
        public static final Integer OR = 3;
        public static final Integer AND = 4;
        public static final String WHERE_AND = "AND";
        public static final String WHERE_OR = "OR";
    }

    public static class EnCryption {
        public static final int _0X45 = 0x45;
        public static final int _0X4F = 0x4f;
        public static final int _0X43 = 0x43;
        public static final int _0X41 = 0x41;
        public static final int _0X44 = 0x44;
        public static final int _0X4C = 0x4c;
        public static final int _0X49 = 0x49;
        public static final int _0X55 = 0x55;
        public static final int _0X42 = 0x42;
        public static final int _0X2A = 0x2a;
        public static final int _0X2D = 0x2d;
        public static final int RADIUS = 16;
        public static final int _0X100 = 0x100;
        public static final int _0XFF = 0xff;
        public static final String SHA_512 = "SHA-512";
        public static final String PKCS5PADDING = "AES/ECB/PKCS5Padding";
        public static final String AES = "AES";
        public static final byte[] KEY = {_0X2D, _0X2A, _0X2D, _0X42, _0X55, _0X49, _0X4C, _0X44, _0X41, _0X43, _0X4F,
            _0X44, _0X45, _0X2D, _0X2A, _0X2D};
    }

    public enum EarthId {

        PROCESS("PROCESS"), // プロセスID、
        TEMPLATE("TEMPLATE"), // テンプレートID、
        WORKSPACE("WORKSPACE"), // ワークスペースID、
        DIRECTORY("DIRECTORY"), // データディレクトリID、
        SITE("SITE"), // サイトID、
        SCHEDULE("SCHEDULE"), //スケジュールID
        TASK("TASK"); // 、タスクID

        private String value;

        EarthId(String value) {
            this.setValue(value);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Regexp {
        public static final String STRING_NUMBER = "[0-9]+";
        public static final String DATETIME_VALIDATION = "(\\d{4})/(\\d{2})/(\\d{2}) " + "(\\d{2}):(\\d{2}):(\\d{2})";
        public static final String ALPHABETS_VALIDATION = "^[A-Za-z0-9]*$";
        public static final String JAPANESE_VALIDATION = "^[a-zA-Z0-9\\s\\.\\-_]+$";
        public static final int MIN_LENGTH = 1;
        public static final int MAX_LENGTH = 255;

    }

    public static class MstCode {
        public static final String TEMPLATE_AUTHORITY_SECTION = "CD0004";
    }
}