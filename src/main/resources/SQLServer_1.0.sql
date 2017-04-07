

CREATE TABLE  CTL_EVENT (
    EVENTID nvarchar(20) NOT NULL,
    USERID nvarchar(20) NOT NULL,
    WORKITEMID nvarchar(20) NOT NULL,
    STATUS nvarchar(20),
    TASKID nvarchar(20) NOT NULL,
    WORKITEMDATA nvarchar(2000),
    LASTUPDATETIME datetime,
    PRIMARY KEY (EVENTID)
);


CREATE TABLE CTL_TEMPLATE (
    USERID nvarchar(255) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    ACCESSAUTHORITY decimal(18),
    LASTUPDATETIME datetime,
    PRIMARY KEY (TEMPLATEID,USERID)
);


CREATE TABLE DAT_DOCUMENT (
    WORKITEMID nvarchar(20) NOT NULL,
    FOLDERITEMNO decimal(18) NOT NULL,
    DOCUMENTNO decimal(18) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    PAGECOUNT decimal(18) NOT NULL,
    VIEWINFORMATION nvarchar(2000),
    DOCUMENTTYPE decimal(18),
    LASTUPDATETIME datetime,
    PRIMARY KEY (DOCUMENTNO,FOLDERITEMNO,WORKITEMID)
);


CREATE TABLE DAT_FOLDERITEM (
    WORKITEMID nvarchar(20) NOT NULL,
    FOLDERITEMNO decimal(18) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    LASTUPDATETIME datetime,
    PRIMARY KEY (FOLDERITEMNO,WORKITEMID)
);


CREATE TABLE DAT_LAYER (
    WORKITEMID nvarchar(20) NOT NULL,
    FOLDERITEMNO decimal(18) NOT NULL,
    DOCUMENTNO decimal(18) NOT NULL,
    LAYERNO decimal(18) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    OWNERID nvarchar(20) NOT NULL,
    ANNOTATIONS nvarchar(2000),
    LASTUPDATETIME datetime,
    PRIMARY KEY (DOCUMENTNO,FOLDERITEMNO,LAYERNO,WORKITEMID)
);


CREATE TABLE DAT_PROCESS (
    PROCESSID decimal(18) NOT NULL,
    WORKITEMID nvarchar(20) NOT NULL,
    TEMPLATEID nvarchar(20),
    LASTUPDATETIME datetime,
    PRIMARY KEY (PROCESSID,WORKITEMID)
);


CREATE TABLE DAT_WORKITEM (
    WORKITEMID nvarchar(20) NOT NULL,
    TASKID nvarchar(20) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    LASTHISTORYNO decimal(18) NOT NULL,
    LASTUPDATETIME datetime,
    PRIMARY KEY (WORKITEMID)
);


CREATE TABLE MGR_PROCESS (
    PROCESSID decimal(18) NOT NULL,
    PROCESSNAME nvarchar(255),
    PROCESSVERSION float(53),
    DESCRIPTION nvarchar(255),
    PROCESSDEFINITION nvarchar(2000),
    DOCUMENTDATASAVEPATH nvarchar(260),
    LASTUPDATETIME datetime,
    PRIMARY KEY (PROCESSID)
);


CREATE TABLE MGR_SCHEDULE (
    SCHEDULEID decimal(18) NOT NULL,
    HOSTNAME nvarchar(255),
    PROCESSID decimal(18) NOT NULL,
    TASKID nvarchar(20) NOT NULL,
    PROCESSISERVICEID decimal(18),
    STARTTIME datetime,
    REPEATOPTION nvarchar(2000),
    ENDTIME datetime,
    LASTUPDATETIME datetime,
    PRIMARY KEY (SCHEDULEID)
);


CREATE TABLE MGR_TEMPLATE (
    TEMPLATEID nvarchar(20) NOT NULL,
    TEMPLATENAME nvarchar(255),
    TEMPLATETABLENAME nvarchar(255),
    TEMPLATEFIELD nvarchar(2000),
    TEMPLATETYPE nvarchar(255),
    LASTUPDATETIME datetime,
    PRIMARY KEY (TEMPLATEID)
);


CREATE TABLE MGR_TEMPLATE_P (
    PROFILEID nvarchar(255) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    ACCESSAUTHORITY decimal(18),
    LASTUPDATETIME datetime,
    PRIMARY KEY (PROFILEID,TEMPLATEID)
);


CREATE TABLE MGR_TEMPLATE_U (
    USERID nvarchar(255) NOT NULL,
    TEMPLATEID nvarchar(20) NOT NULL,
    ACCESSAUTHORITY decimal(18),
    LASTUPDATETIME datetime,
    PRIMARY KEY (TEMPLATEID,USERID)
);


CREATE TABLE STR_DATA_FILE (
    WORKITEMID nvarchar(20) NOT NULL,
    FOLDERITEMNO decimal(18) NOT NULL,
    DOCUMENTNO decimal(18) NOT NULL,
    DOCUMENTDATAPATH nvarchar(260),
    LASTUPDATETIME datetime,
    PRIMARY KEY (DOCUMENTNO,FOLDERITEMNO,WORKITEMID)
);


CREATE TABLE STR_LOG_ACCESS (
    EVENTID nvarchar(20) NOT NULL,
    PROCESSTIME datetime NOT NULL,
    USERID nvarchar(255) NOT NULL,
    WORKITEMID nvarchar(20) NOT NULL,
    HISTORYNO decimal(18) NOT NULL,
    PROCESSID decimal(18) NOT NULL,
    PROCESSVERSION float(53),
    TASKID nvarchar(20) NOT NULL,
    LASTUPDATETIME datetime,
    PRIMARY KEY (EVENTID)
)