package co.jp.nej.earth.model.enums;

public enum ColumnNames {
    ACCESS_AUTHORITY("accessAuthority")
    ,ANNOTATIONS("annotations")
    ,AVAILABLE_LICENSE_COUNT("availableLicenseCount")
    ,BUSINESS_DAY("businessDay")
    ,BUSINESS_DAY_FLAG("businessDayFlag")
    ,CODE_ID("codeId")
    ,CODE_VALUE("codeValue")
    ,CONFIG_VALUE("configValue")
    ,DATA_DIRECTORY_ID("dataDirectoryId")
    ,DB_PASSWORD("dbPassword")
    ,DB_SERVER("dbServer")
    ,DB_USER("dbUser")
    ,PORT("port")
    ,DESCRIPTION("description")
    ,DOCUMENT_DATA("documentData")
    ,DOCUMENT_DATA_PATH("documentDataPath")
    ,DOCUMENT_DATASAVE_PATH("documentDataSavePath")
    ,DOCUMENT_NO("documentNo")
    ,DOCUMENT_TYPE("documentType")
    ,END_TIME("endTime")
    ,EVEN_TID("eventId")
    ,FOLDER_ITEM_NO("folderItemNo")
    ,FOLDER_PATH("folderPath")
    ,FUNCTION_ID("functionId")
    ,FUNCTION_NAME("functionName")
    ,FUNCTION_CATEGORY_ID("functionCategoryId")
    ,FUNCTION_CATEGORY_NAME("functionCategoryName")
    ,FUNCTION_SORT_NO("functionSortNo")
    ,FUNCTION_INFORMATION("functionInformation")
    ,HISTORY_NO("historyNo")
    ,LAST_HISTORY_NO("lastHistoryNo")
    ,LAST_UPDATE_TIME("lastUpdateTime")
    ,LAYER_NO("layerNo")
    ,LDAP_IDENTIFIER("ldapIdentifier")
    ,LOGIN_TIME("loginTime")
    ,LOGOUT_TIME("logoutTime")
    ,NAME("name")
    ,OWNER("owner")
    ,OWNER_ID("ownerId")
    ,PAGE_COUNT("pageCount")
    ,PASSWORD("password")
    ,PASSWD("passwd")
    ,PROCESS_DEFINITION("processDefinition")
    ,PROCESS_ID("processId")
    ,PROCESS_NAME("processName")
    ,HOST_NAME("hostName")
    ,PROCESS_ISERVICEID("processIServiceId")
    ,PROCESS_VERSION("processVersion")
    ,PROFILE_ID("profileId")
    ,REPEAT_OPTION("repeatOption")
    ,SCHEDULE_ID("scheduleId")
    ,SCHEMA_NAME("schemaName")
    ,SECTION("section")
    ,SESSION_ID("sessionId")
    ,SITE_ID("siteId")
    ,SITE_MANAGEMENT_TYPE("siteManagementType")
    ,START_TIME("startTime")
    ,STATUS("status")
    ,TASK_ID("taskId")
    ,TEMPLATE_FIELD("templateField")
    ,TEMPLATE_TYPE("templateType")
    ,TEMPLATE_ID("templateId")
    ,TEMPLATE_NAME("templateName")
    ,TEMPLATE_TABLE_NAME("templateTableName")
    ,USE_LICENSE_COUNT("useLicenseCount")
    ,USER_ID("userId")
    ,VARIABLE_NAME("variableName")
    ,VIEW_INFORMATION("viewInformation")
    ,WORKITEM_DATA("workitemData")
    ,WORKITEM_ID("workitemId")
    ,WORKSPACE_ID("workspaceId")
    ,WORKSPACE_NAME("workspaceName")
    ,PROCESS_TIME("processTime")
    ,TASK_NAME("taskName")
    ,CLASS_NAME("className")
    ,NEW_CREATE_FILE("newCreateFile")
    ,RESERVED_DISK_VOL_SIZE("reservedDiskVolSize")
    ,DISK_VOL_SIZE("diskVolSize")
    ,DIVISION("division")
    ,DB_TYPE("dbType")
    ,DB_PORT("dbPort")
    ,ISSUE_DATE("issueDate")
    ,ENABLE_DISABLE("enable_Disable")
    ,NEXT_RUN_DATE("nextRunDate")
    ,RUN_INTERVAL_DAY("runIntervalDay")
    ,RUN_INTERVAL_HOUR("runIntervalHour")
    ,RUN_INTERVAL_MINUTE("runIntervalMinute")
    ,RUN_INTERVAL_SECOND("runIntervalSecond")
    ,COUNT("count");

    private String propertyName;

    ColumnNames(String name) {
        propertyName = name;
    }

    @Override
    public String toString() {
        return propertyName;
    }
}
