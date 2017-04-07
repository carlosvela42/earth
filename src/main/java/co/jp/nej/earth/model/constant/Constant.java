/**
 * 
 */
package co.jp.nej.earth.model.constant;

/**
 * @author p-tvo-khanhnv
 *
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

}
