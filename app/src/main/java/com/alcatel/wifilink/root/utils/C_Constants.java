package com.alcatel.wifilink.root.utils;

/**
 * Created by yaodong.zhang on 2017/6/26.
 */

public class C_Constants {
    public class Language {
        public static final String LANGUAGE = "language";
        public static final String ENGLISH = "en";
        public static final String ARABIC = "ar";
        public static final String ESPANYOL = "es";
        public static final String GERMENIC = "de";
        public static final String ITALIAN = "it";
        public static final String FRENCH = "fr";
        public static final String SERBIAN = "sr";
        public static final String CROATIAN = "hr";
        public static final String SLOVENIAN = "sl";
        public static final String POLAND = "pl";
        public static final String RUSSIAN = "ru";
        public static final String CHINA = "zh";
    }

    public class SIMState {
        public static final int NOWN = 0;
        public static final int SIM_CARD_DETECTED = 1;
        public static final int PIN_REQUIRED = 2;
        public static final int PUK_REQUIRED = 3;
        public static final int SIM_LOCK_REQUIRED = 4;
        public static final int PUK_TIMES_USED_OUT = 5;
        public static final int SIM_CARD_ILLEGAL = 6;
        public static final int SIM_CARD_READY = 7;
        public static final int SIM_CARD_IS_INITING = 11;
    }

    public class PinState {
        public static final int UNKNOWN = 0;
        public static final int ENABLE_BUT_NOT_VERIFIED = 1;
        public static final int PIN_enable_verified = 2;
        public static final int PIN_DISABLE = 3;
        public static final int PUK_REQUIRED = 4;
        public static final int PUK_TIMES_USED_OUT = 5;
    }

    public class SIMLockState {
        public static final int NO_SIMLOCK_OR_SIM_LOCK_UNLOCK_ = -1;
        public static final int NCK = 0;
        public static final int NSCK = 1;
        public static final int SPCK = 2;
        public static final int CCK = 3;
        public static final int PCK = 4;
        public static final int RCK_15 = 15;
        public static final int RCK_16 = 16;
        public static final int RCK_17 = 17;
        public static final int RCK_18 = 18;
        public static final int RCK_19 = 19;
        public static final int RCK_FORBID = 30;
    }

    public class ConnectionStatus {
        public static final int DISCONNECTED = 0;
        public static final int CONNECTING = 1;
        public static final int CONNECTED = 2;
        public static final int DISCONNECTING = 3;
    }

    public class Conprofileerror {
        public static final int NO_ERROR = 0;
        public static final int APN_ERROR = 1;
        public static final int pdp_error = 2;
    }

    public class Loginstate {
        public static final int LOGOUT = 0;
        public static final int LOGIN = 1;
        public static final int THE_LOGIN_TIMES_USED_OUT = 2;
    }

    public class SetNetWorkSeting {
        public static final int NET_WORK_MODE_AUTO = 0;
        public static final int NET_WORK_MODE_4G = 3;
        public static final int NET_WORK_MODE_3G = 2;
        public static final int NET_WORK_MODE_2G = 1;
    }

    public class GetNetworkInfo {
        public static final int ROAMING = 0;
        public static final int NO_ROAMING = 1;
    }

    public class ConnectionSettings {
        public static final int CONNECTION_MODE_AUTO = 1;
        public static final int CONNECTION_MODE_MANUAL = 0;
        public static final int ROAMING_DISABLE = 0;
        public static final int ROAMING_ENABLE = 1;
    }

    public class WanSettingsStatus {
        public static final int DISCONNECTED = 0;
        public static final int CONNECTING = 1;
        public static final int CONNECTED = 2;
        public static final int DISCONNECTING = 3;
    }

    public class DeviceUpgradeStatus {
        public static final int DEVICE_UPGRADE_NOT_START = 0;
        public static final int DEVICE_UPGRADE_UPDATING = 1;
        public static final int DEVICE_UPGRADE_COMPLETE = 2;
    }

    public class DeviceVersionCheckState {
        public static final int DEVICE_CHECKING = 0;
        public static final int DEVICE_NEW_VERSION = 1;
        public static final int DEVICE_NO_NEW_VERSION = 2;
        public static final int DEVICE_NO_CONNECT = 3;
        public static final int SERVICE_NOT_AVAILABLE = 4;
        public static final int DEVICE_CHECK_ERROR = 5;
    }

    public class DeviceUSBStatus {
        public static final int NOT_INSERT = 0;
        public static final int USB_STORAGE = 1;
        public static final int USB_PRINT = 2;
    }

    public class UsageSetting {
        public static final int UNIT_MB = 0;
        public static final int UNIT_GB = 1;
        public static final int UNIT_KB = 2;
    }
}
