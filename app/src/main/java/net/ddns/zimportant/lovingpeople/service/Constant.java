package net.ddns.zimportant.lovingpeople.service;

public final class Constant {
	public static final String INSTANCE_ADDRESS = "zimportant.ddns.net:9080";
	public static final String AUTH_URL = "http://" + INSTANCE_ADDRESS + "/auth";
	public static final String DEFAULT_REALM_URL = "realm://" + INSTANCE_ADDRESS + "/default";

	public static final String PARTNER = "Partner";
	public static final String COUNSELOR_ID = "counselorId";
	public static final String STORYTELLER_ID = "storytellerId";

	public static final String ERR_USER_CHAT_OTHER = "This person is chatting with other";
	public static final String ERR_USER_NOT_AVAILABLE = "This person is not available right now";
	public static final String ERR_USER_NOT_REQUEST_MORE = "You cannot chat with two people at one time";
	public static final String ERR_USER_CANCEL = "Cancelled request";
	public static final String ERR_USER_STOP_REQUEST = "Sorry, this person has stopped the request";
	public static final String ERR_USER_CANNOT_CHANGE_STATUS = "You cannot change status while connecting with another";
	public static final String ERR_USER_NOT_BOTH_COUNSELOR = "You cannot both Counselor";
	public static final String ERR_USER_NOT_BOTH_STORYTELLER = "You cannot both Storyteller";

	public static final String ERR_ADD_INFO = "Please add this information";
}
