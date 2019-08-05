package com.alcatel.wifilink.root.utils;


public class C_ENUM {
    public enum LOGIN_STATE {
        Login, Logout;

        public static LOGIN_STATE build(int nState) {
            if (nState == 0) {
                return Login;
            } else {
                return Logout;
            }
        }
    }

    public enum OVER_TIME_STATE {
        Disable, Enable;

        public static OVER_TIME_STATE build(int nState) {
            if (nState == 1) {
                return Enable;
            } else {
                return Disable;
            }
        }

        public static int antiBuild(OVER_TIME_STATE storeIn) {
            if (storeIn == Disable) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public enum OVER_DISCONNECT_STATE {
        Disable, Enable;

        public static OVER_DISCONNECT_STATE build(int nState) {
            if (nState == 1) {
                return Enable;
            } else {
                return Disable;
            }
        }

        public static int antiBuild(OVER_DISCONNECT_STATE storeIn) {
            if (storeIn == Enable) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public enum OVER_ROAMING_STATE {
        Disable, Enable;

        public static OVER_ROAMING_STATE build(int nState) {
            if (nState == 1) {
                return Enable;
            } else {
                return Disable;
            }
        }

        public static int antiBuild(OVER_ROAMING_STATE storeIn) {
            if (storeIn == Enable) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /*
     * PDP connection type:
     * PDP_TYPE_IPv4 = 0,
     * PDP_TYPE_PPP = 1,
     * PDP_TYPE_IPv6 = 2,
     * PDP_TYPE_IPv4v6 =3
     */
    public enum PdpType {
        PDP_TYPE_IPv4, PDP_TYPE_PPP, PDP_TYPE_IPv6, PDP_TYPE_IPv4v6, UNKNOWN;

        public static PdpType build(int nState) {
            if (nState == 0) {
                return PDP_TYPE_IPv4;
            } else if (nState == 1) {
                return PDP_TYPE_PPP;
            } else if (nState == 2) {
                return PDP_TYPE_IPv6;
            } else if (nState == 3) {
                return PDP_TYPE_IPv4v6;
            } else {
                return UNKNOWN;
            }
        }

        public static int antiBuild(PdpType storeIn) {
            if (storeIn == PDP_TYPE_IPv4) {
                return 0;
            } else if (storeIn == PDP_TYPE_PPP) {
                return 1;
            } else if (storeIn == PDP_TYPE_IPv6) {
                return 2;
            } else if (storeIn == PDP_TYPE_IPv4v6) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    /*
     * Possible values: 0 - NoSim; 1 - PinRequired; 2 - PukRequired; 3 -
     * Accessable (Network unlocked);//ready 4 - InvalidSim 5 - Unknown
     */
    public enum SIMState {
        NoSim, SimCardDetected, PinRequired, PukRequired, SimLockRequired, PukTimesUsedOut, InvalidSim, Accessable, SimCardIsIniting, Unknown;

        public static SIMState build(int nState) {
            if (nState == 0) {
                return NoSim;
            } else if (nState == 1) {
                return SimCardDetected;
            } else if (nState == 2) {
                return PinRequired;
            } else if (nState == 3) {
                return PukRequired;
            } else if (nState == 4) {
                return SimLockRequired;
            } else if (nState == 5) {
                return PukTimesUsedOut;
            } else if (nState == 6) {
                return InvalidSim;
            } else if (nState == 7) {
                return Accessable;
            } else if (nState == 11) {
                return SimCardIsIniting;
            } else {
                return InvalidSim;
            }
        }
    }

    /*
     * 0: Not available 1: PIN disable 2: PIN enable 3: Require PUK
     */
    public enum PinState {
        NotAvailable, EnableButNotVerified, PinEnableVerified, Disable, RequirePUK, PukTimesUsedOut;

        public static PinState build(int nState) {
            if (nState == 0) {
                return NotAvailable;
            } else if (nState == 1) {
                return EnableButNotVerified;
            } else if (nState == 2) {
                return PinEnableVerified;
            } else if (nState == 3) {
                return Disable;
            } else if (nState == 4) {
                return RequirePUK;
            } else if (nState == 5) {
                return PukTimesUsedOut;
            } else {
                return NotAvailable;
            }
        }
    }

    /*
     * Possible values: 0 : Disable 1 : Enable
     */
    public enum AutoPinState {
        Disable, Enable;

        public static AutoPinState build(int nState) {
            if (nState == 0) {
                return Disable;
            } else {
                return Enable;
            }
        }
    }

	/*
     * Network Type 0: No service 1: GPRS 2: EDGE 3: UMTS 4: HSDPA 5: HSPA 6:
	 * HSUPA 7: DC-HSPA+ 8: LTE 9: GSM 10: HSPA+ 11: UNKNOWN
	 */

    public enum NetworkType {
        No_service, GPRS, EDGE, HSPA, HSUPA, UMTS, HSPA_PLUS, DC_HSPA_PLUS, LTE, UNKNOWN;

        public static NetworkType build(int nState) {
            if (nState == 0) {
                return No_service;
            } else if (nState == 1) {
                return GPRS;
            } else if (nState == 2) {
                return EDGE;
            } else if (nState == 3) {
                return HSPA;
            } else if (nState == 4) {
                return HSUPA;
            } else if (nState == 5) {
                return UMTS;
            } else if (nState == 6) {
                return HSPA_PLUS;
            } else if (nState == 7) {
                return DC_HSPA_PLUS;
            } else if (nState == 8) {
                return LTE;
            } else {
                return UNKNOWN;
            }
        }
    }


    /*
     * RSSI value: 0: level 0 1: level 1 2: level 2 3: level 3 4: level 4 5:
     * level 5
     */
    public enum SignalStrength {
        Level_0, Level_1, Level_2, Level_3, Level_4, Level_5;

        public static SignalStrength build(int nState) {
            if (nState == 0) {
                return Level_0;
            } else if (nState == 1) {
                return Level_1;
            } else if (nState == 2) {
                return Level_2;
            } else if (nState == 3) {
                return Level_3;
            } else if (nState == 4) {
                return Level_4;
            } else {
                return Level_5;
            }
        }
    }

    public enum SMSInit {
        Complete,
        Initing;

        public static SMSInit build(int nState) {
            if (nState == 0) {
                return Complete;
            } else {
                return Initing;
            }
        }

        public static int antiBuild(SMSInit storeIn) {
            if (storeIn == Complete) {
                return 0;
            } else {
                return 2;
            }
        }
    }

    /*
     * 0 : read 1 : unread 2 : sent 3 : sent failed 4 : report 5 : flash 6: draft
     */
    public enum EnumSMSType {
        Read, Unread, Sent, SentFailed, Report, Flash, Draft;

        public static EnumSMSType build(int nState) {
            if (nState == 0) {
                return Read;
            } else if (nState == 1) {
                return Unread;
            } else if (nState == 2) {
                return Sent;
            } else if (nState == 3) {
                return SentFailed;
            } else if (nState == 4) {
                return Report;
            } else if (nState == 5) {
                return Flash;
            } else {
                return Draft;
            }
        }

        public static int antiBuild(EnumSMSType type) {
            if (type == Read) {
                return 0;
            } else if (type == Unread) {
                return 1;
            } else if (type == Sent) {
                return 2;
            } else if (type == SentFailed) {
                return 3;
            } else if (type == Report) {
                return 4;
            } else if (type == Flash) {
                return 5;
            } else {
                return 6;
            }
        }
    }

    /*
     * //This flag means the SMS that want to delete.
     * 0: delete all SMS
     * 1: delete one record in Contact SMS list
     * 2:delete one message in Content  SMS list
     */
    public enum EnumSMSDelFlag {
        Delete_all, Delete_contact_messages, Delete_message;

        public static EnumSMSDelFlag build(int nState) {
            if (nState == 0) {
                return Delete_all;
            } else if (nState == 1) {
                return Delete_contact_messages;
            } else {
                return Delete_message;
            }
        }

        public static int antiBuild(EnumSMSDelFlag type) {
            if (type == Delete_all) {
                return 0;
            } else if (type == Delete_contact_messages) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    /*
     * 0 : none 1 : sending 2 : sendAgainSuccess 3 : fail still sending last message 4 :
     * fail with Memory full 5 : fail
     */
    public enum SendStatus {
        None, Sending, Success, Fail_still_sending, Fail_Memory_Full, Fail;

        public static SendStatus build(int nState) {
            if (nState == 0) {
                return None;
            } else if (nState == 1) {
                return Sending;
            } else if (nState == 2) {
                return Success;
            } else if (nState == 3) {
                return Fail_still_sending;
            } else if (nState == 4) {
                return Fail_Memory_Full;
            } else {
                return Fail;
            }
        }
    }

    /*
     * 0: disconnected 1: connecting 2: connected 3: disconnecting
     */
    public enum ConnectionStatus {
        Disconnected, Connecting, Connected, Disconnecting;

        public static ConnectionStatus build(int nState) {
            if (nState == 0) {
                return Disconnected;
            } else if (nState == 1) {
                return Connecting;
            } else if (nState == 2) {
                return Connected;
            } else if (nState == 3) {
                return Disconnecting;
            } else {
                return Disconnecting;
            }
        }
    }

    /**
     * service type, Samba = 0; DLNA = 1; FTP = 2
     */
    public enum ServiceType {
        Samba, DLNA, FTP;

        public static ServiceType build(int nType) {
            if (nType == 0) {
                return Samba;
            } else if (nType == 1) {
                return DLNA;
            } else if (nType == 2) {
                return FTP;
            } else
                return Samba;
        }
    }

    /**
     * service Status, Disabled = 0; Enabled = 1
     */
    public enum Status {
        Disabled, Enabled;

        public static Status build(int nState) {
            if (nState == 0) {
                return Disabled;
            } else if (nState == 1) {
                return Enabled;
            } else
                return Disabled;
        }
    }

    /**
     * AnonymousState State, Disabled = 0; Enabled = 1
     */
    public enum AnonymousState {
        Disabled, Enabled;

        public static AnonymousState build(int nState) {
            if (nState == 0) {
                return Disabled;
            } else if (nState == 1) {
                return Enabled;
            } else
                return Disabled;
        }
    }

    /**
     * AuthType, ReadOnly = 0; ReadWrite = 1
     */
    public enum AuthType {
        ReadOnly, ReadWrite;

        public static AuthType build(int nType) {
            if (nType == 0) {
                return ReadOnly;
            } else if (nType == 1) {
                return ReadWrite;
            } else
                return ReadOnly;
        }
    }

    /**
     * WlanFrequency, 0:Disable; 1:2.4GHz; 2: 5GHz
     */
    public enum WlanFrequency {
        Frequency_24GHZ, Frequency_5GHZ, Frequency_2point4Gand5GHZ, UNKNOWN;

        public static WlanFrequency build(int nType) {
            if (nType == 1) {
                return Frequency_5GHZ;
            } else if (nType == 2) {
                return Frequency_2point4Gand5GHZ;
            } else if (nType == 0)
                return Frequency_24GHZ;
            else
                return UNKNOWN;
        }

        public static int antiBuild(WlanFrequency frequency) {
            if (Frequency_24GHZ == frequency) {
                return 0;
            } else if (Frequency_5GHZ == frequency) {
                return 1;
            } else
                return 2;
        }
    }

    /**
     * SecurityMode //0: disable; 1: WEP; 2: WPA; 3: WPA2; 4: WPA/WPA2
     */
    public enum SecurityMode {
        Disable, WEP, WPA, WPA2, WPA_WPA2;

        public static SecurityMode build(int nType) {
            if (nType == 1) {
                return WEP;
            } else if (nType == 2) {
                return WPA;
            } else if (nType == 3) {
                return WPA2;
            } else if (nType == 4) {
                return WPA_WPA2;
            } else
                return Disable;
        }

        public static int antiBuild(SecurityMode mode) {
            if (mode == WEP) {
                return 1;
            } else if (mode == WPA) {
                return 2;
            } else if (mode == WPA2) {
                return 3;
            } else if (mode == WPA_WPA2) {
                return 4;
            } else
                return 0;
        }
    }

    /**
     * WPAEncryption 0: TKIP 1: AES 2: AUTO
     */
    public enum WPAEncryption {
        TKIP, AES, AUTO;

        public static WPAEncryption build(int nType) {
            if (nType == 0) {
                return TKIP;
            } else if (nType == 1) {
                return AES;
            } else if (nType == 2) {
                return AUTO;
            } else {
                return AUTO;
            }
        }

        public static int antiBuild(WPAEncryption mode) {
            if (mode == TKIP) {
                return 0;
            } else if (mode == AES) {
                return 1;
            } else if (mode == AUTO) {
                return 2;
            } else
                return 2;
        }
    }

    /**
     * WEPEncryption //0: OPEN 1: share
     */
    public enum WEPEncryption {
        Open, Share;

        public static WEPEncryption build(int nType) {
            if (nType == 0) {
                return Open;
            } else if (nType == 1) {
                return Share;
            } else {
                return Open;
            }
        }

        public static int antiBuild(WEPEncryption mode) {
            if (mode == Open) {
                return 0;
            } else if (mode == Share) {
                return 1;
            } else
                return 1;
        }
    }

    /**
     * WMode 0: auto 1: 802.11a 2: 802.11b 3: 802.11g 4: 802.11a+n 5: 802.11g+n
     */
    public enum WModeEnum {
        WMode_auto,
        WMode_802_11a,
        WMode_802_11b,
        WMode_802_11g,
        WMode_802_11a_n,
        WMode_802_11g_n;

        public static WModeEnum build(int nType) {
            if (nType == 0) {
                return WMode_auto;
            } else if (nType == 1) {
                return WMode_802_11a;
            } else if (nType == 2) {
                return WMode_802_11b;
            } else if (nType == 3) {
                return WMode_802_11g;
            } else if (nType == 4) {
                return WMode_802_11a_n;
            } else if (nType == 5) {
                return WMode_802_11g_n;
            } else {
                return WMode_auto;
            }
        }
    }

    public enum SsidHiddenEnum {

        SsidHidden_Unknown,
        SsidHidden_Disable,
        SsidHidden_Enable;

        public static SsidHiddenEnum build(int nType) {
            if (nType == 1) {
                return SsidHidden_Disable;
            } else if (nType == 2) {
                return SsidHidden_Enable;
            } else {
                return SsidHidden_Unknown;
            }
        }

        public static int antiBuild(SsidHiddenEnum type) {
            switch (type) {
                case SsidHidden_Disable:
                    return 0;
                case SsidHidden_Enable:
                    return 1;
                default:
                    return -1;
            }
        }
    }

    public enum WIFIConnectStatus {
        Connected, Disconnected;

        public static WIFIConnectStatus build(int nState) {
            if (nState == 0) {
                return Connected;
            } else {
                return Disconnected;
            }
        }
    }

    /*
     * Login status 0: logout 1: logined 2:the LOGIN times used out
     */
    public enum UserLoginStatus {
        Logout, LOGIN, LoginTimeOut;

        public static UserLoginStatus build(int status) {
            if (status == 1) {
                return LOGIN;
            } else if (status == 2) {
                return LoginTimeOut;
            } else {
                return Logout;
            }
        }

        public static int antiBuild(UserLoginStatus status) {
            if (status == LoginTimeOut) {
                return 2;
            } else if (status == LOGIN) {
                return 1;
            } else
                return 0;
        }
    }

    public enum WlanSupportMode {
        Mode2Point4G, Mode5G, Mode2Point4GAnd5G;

        public static WlanSupportMode build(int nMode) {
            switch (nMode) {
                case 0:
                    return Mode2Point4G;
                case 1:
                    return Mode5G;
                case 2:
                    return Mode2Point4GAnd5G;

                default:
                    return Mode2Point4G;
            }

        }
    }

    /*
     * 0: use the web UI and LOGIN device;
       1: just connected to device but not LOGIN.

     */
    public enum EnumDeviceType {
        USE_WEB_LOGIN, CONNECTED_DEVICE_NOT_LOGIN;

        public static EnumDeviceType build(int nMode) {
            switch (nMode) {
                case 0:
                    return USE_WEB_LOGIN;
                default:
                    return CONNECTED_DEVICE_NOT_LOGIN;
            }

        }

        public static int antiBuild(EnumDeviceType type) {
            switch (type) {
                case USE_WEB_LOGIN:
                    return 0;
                default:
                    return 1;
            }
        }
    }

    /*
     * 0:USB connect
        1:WIFI connect

     */
    public enum EnumConnectMode {
        USB_CONNECT, WIFI_CONNECT;

        public static EnumConnectMode build(int nMode) {
            switch (nMode) {
                case 0:
                    return USB_CONNECT;
                default:
                    return WIFI_CONNECT;
            }

        }
    }

    public enum EnumDeviceCheckingStatus {
        DEVICE_CHECKING, DEVICE_NEW_VERSION, DEVICE_NO_NEW_VERSION, DEVICE_NO_CONNECT,
        SERVICE_NOT_AVAILABLE, DEVICE_CHECK_ERROR;

        public static EnumDeviceCheckingStatus build(int nStatus) {
            switch (nStatus) {
                case 0:
                    return DEVICE_CHECKING;

                case 1:
                    return DEVICE_NEW_VERSION;

                case 2:
                    return DEVICE_NO_NEW_VERSION;
                case 3:
                    return DEVICE_NO_CONNECT;
                case 4:
                    return SERVICE_NOT_AVAILABLE;
                case 5:
                    return DEVICE_CHECK_ERROR;

                default:
                    return DEVICE_NO_NEW_VERSION;
            }

        }

        public static int antiBuild(EnumDeviceCheckingStatus status) {
            int nStatus = 2;
            if (DEVICE_CHECKING == status) {
                nStatus = 0;
            } else if (DEVICE_NEW_VERSION == status) {
                nStatus = 1;
            } else if (DEVICE_NO_NEW_VERSION == status) {
                nStatus = 2;
            } else if (DEVICE_NO_CONNECT == status) {
                nStatus = 3;
            } else if (SERVICE_NOT_AVAILABLE == status) {
                nStatus = 4;
            } else if (DEVICE_CHECK_ERROR == status) {
                nStatus = 5;
            }

            return nStatus;
        }
    }

    public enum EnumDeviceUpgradeStatus {
        DEVICE_UPGRADE_NOT_START, DEVICE_UPGRADE_UPDATING, DEVICE_UPGRADE_COMPLETE;

        public static EnumDeviceUpgradeStatus build(int nStatus) {
            switch (nStatus) {
                case 0:
                    return DEVICE_UPGRADE_NOT_START;

                case 1:
                    return DEVICE_UPGRADE_UPDATING;

                case 2:
                    return DEVICE_UPGRADE_COMPLETE;

                default:
                    return DEVICE_UPGRADE_NOT_START;
            }

        }

        public static int antiBuild(EnumDeviceUpgradeStatus status) {
            int nStatus = 0;
            if (DEVICE_UPGRADE_NOT_START == status) {
                nStatus = 0;
            } else if (DEVICE_UPGRADE_UPDATING == status) {
                nStatus = 1;
            } else if (DEVICE_UPGRADE_COMPLETE == status) {
                nStatus = 2;
            }

            return nStatus;
        }
    }

    public enum EnumRestoreErrorStatus {
        RESTORE_ERROR_SUCCESSFUL, RESTORE_ERROR_NO_BACKUP_FILE, RESTORE_ERROR_OTHER_ERROR;

        public static EnumRestoreErrorStatus build(int nStatus) {
            switch (nStatus) {
                case 0:
                    return RESTORE_ERROR_SUCCESSFUL;

                case 1:
                    return RESTORE_ERROR_NO_BACKUP_FILE;

                case 2:
                    return RESTORE_ERROR_OTHER_ERROR;

                default:
                    return RESTORE_ERROR_OTHER_ERROR;
            }

        }

        public static int antiBuild(EnumRestoreErrorStatus status) {
            int nStatus = 0;
            if (RESTORE_ERROR_SUCCESSFUL == status) {
                nStatus = 0;
            } else if (RESTORE_ERROR_NO_BACKUP_FILE == status) {
                nStatus = 1;
            } else if (RESTORE_ERROR_OTHER_ERROR == status) {
                nStatus = 2;
            }

            return nStatus;
        }
    }
}
