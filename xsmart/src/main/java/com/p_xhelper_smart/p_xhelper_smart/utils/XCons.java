package com.p_xhelper_smart.p_xhelper_smart.utils;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by qianli.ma on 2019/7/25 0025.
 */
@SuppressLint("UseSparseArrays")
public class XCons {

    public static String TAG = "xsmart";

    public static int ENCRYPT_DEV_UNKNOWN = -1;// 未知设备
    public static int ENCRYPT_DEV_ROUTER = 0;// 路由设备
    public static int ENCRYPT_DEV_HOTSPOT = 1;// 热点设备
    public static int ENCRYPT_DEV_TARGET = 2;// 定制设备
    public static int ENCRYPT_DEV_5G_CPE = 3;// 5G-CPE设备

    public static List<String> ENCRYPT_LIST_DEV_ROUTER = new ArrayList<>();
    public static List<String> ENCRYPT_LIST_DEV_HOTSPOT = new ArrayList<>();
    public static List<String> ENCRYPT_LIST_DEV_TARGET = new ArrayList<>();
    public static List<String> ENCRYPT_LIST_DEV_5G_CPE = new ArrayList<>();

    static {
        // 路由设备(仅针对加密)
        ENCRYPT_LIST_DEV_ROUTER.add("hh70");
        ENCRYPT_LIST_DEV_ROUTER.add("hub70");
        ENCRYPT_LIST_DEV_ROUTER.add("hh40");
        ENCRYPT_LIST_DEV_ROUTER.add("hub40");
        ENCRYPT_LIST_DEV_ROUTER.add("hh41");
        ENCRYPT_LIST_DEV_ROUTER.add("hub41");
        ENCRYPT_LIST_DEV_ROUTER.add("hh42");
        ENCRYPT_LIST_DEV_ROUTER.add("hub42");
        ENCRYPT_LIST_DEV_ROUTER.add("hub71");
        ENCRYPT_LIST_DEV_ROUTER.add("hh71");
        // 热点设备(仅针对加密)
        ENCRYPT_LIST_DEV_HOTSPOT.add("mw12");
        ENCRYPT_LIST_DEV_HOTSPOT.add("mw70");
        ENCRYPT_LIST_DEV_HOTSPOT.add("mw43");
        ENCRYPT_LIST_DEV_HOTSPOT.add("mw45");
        // 定制设备(仅针对加密)
        ENCRYPT_LIST_DEV_TARGET.add("E1");
        // TODO: 2020/6/22  等待FW开发完毕后再填入具体型号
        // 5G CPE的设备
        ENCRYPT_LIST_DEV_5G_CPE.add("5G");
    }

    // 方法
    public static String METHOD_LOGIN = "Login";
    public static String METHOD_LOGOUT = "Logout";
    public static String METHOD_GET_LOGIN_STATE = "GetLoginState";
    public static String METHOD_CHANGE_PASSWORD = "ChangePassword";
    public static String METHOD_HEART_BEAT = "HeartBeat";
    public static String METHOD_GET_SMS_STORAGE_STATE = "GetSMSStorageState";
    public static String METHOD_GET_NETWORK_REGISTER_STATE = "GetNetworkRegisterState";
    public static String METHOD_GET_SIM_STATUS = "GetSimStatus";
    public static String METHOD_UNLOCK_PIN = "UnlockPin";
    public static String METHOD_UNLOCK_PUK = "UnlockPuk";
    public static String METHOD_CHANGE_PIN_CODE = "ChangePinCode";
    public static String METHOD_CHANGE_PIN_STATE = "ChangePinState";
    public static String METHOD_GET_CONNECTION_STATE = "GetConnectionState";
    public static String METHOD_CONNECT = "Connect";
    public static String METHOD_DISCONNECT = "DisConnect";
    public static String METHOD_GET_CONNECTION_SETTINGS = "GetConnectionSettings";
    public static String METHOD_SET_CONNECTION_SETTINGS = "SetConnectionSettings";
    public static String METHOD_GET_NETWORK_SETTINGS = "GetNetworkSettings";
    public static String METHOD_SET_NETWORK_SETTINGS = "SetNetworkSettings";
    public static String METHOD_GET_NETWORK_INFO = "GetNetworkInfo";
    public static String METHOD_GET_WLAN_SETTINGS = "GetWlanSettings";
    public static String METHOD_SET_WLAN_SETTINGS = "SetWlanSettings";
    public static String METHOD_GET_WLAN_SUPPORT_MODE = "GetWlanSupportMode";
    public static String METHOD_GET_SYSTEM_STATUS = "GetSystemStatus";
    public static String METHOD_GET_SYSTEM_INFO = "GetSystemInfo";
    public static String METHOD_SET_DEVICE_REBOOT = "SetDeviceReboot";
    public static String METHOD_SET_DEVICE_RESET = "SetDeviceReset";
    public static String METHOD_SET_DEVICE_BACKUP = "SetDeviceBackup";
    public static String METHOD_GET_WAN_SETTINGS = "GetWanSettings";
    public static String METHOD_SET_WAN_SETTINGS = "SetWanSettings";
    public static String METHOD_GET_LAN_SETTINGS = "GetLanSettings";
    public static String METHOD_GET_DEVICE_NEW_VERSION = "GetDeviceNewVersion";
    public static String METHOD_SET_CHECK_NEW_VERSION = "SetCheckNewVersion";
    public static String METHOD_SET_DEVICE_START_UPDATE = "SetDeviceStartUpdate";
    public static String METHOD_GET_DEVICE_UPGRADE_STATE = "GetDeviceUpgradeState";
    public static String METHOD_SET_FOTA_START_DOWNLOAD = "SetFOTAStartDownload";
    public static String METHOD_SET_DEVICE_UPDATE_STOP = "SetDeviceUpdateStop";
    public static String METHOD_GET_FTP_SETTINGS = "GetFtpSettings";
    public static String METHOD_GET_SAMBA_SETTINGS = "GetSambaSettings";
    public static String METHOD_GET_DLNA_SETTINGS = "GetDLNASettings";
    public static String METHOD_SET_FTP_SETTINGS = "SetFtpSettings";
    public static String METHOD_SET_SAMBA_SETTINGS = "SetSambaSettings";
    public static String METHOD_SET_DLNA_SETTINGS = "SetDLNASettings";
    public static String METHOD_SET_USAGE_SETTING = "SetUsageSettings";
    public static String METHOD_GET_USAGE_SETTING = "GetUsageSettings";
    public static String METHOD_GET_USAGE_RECORD = "GetUsageRecord";
    public static String METHOD_SET_USAGE_RECORD_CLEAR = "SetUsageRecordClear";
    public static String METHOD_GET_CONNECTED_DEVICE_LIST = "GetConnectedDeviceList";
    public static String METHOD_GET_BLOCK_DEVICE_LIST = "GetBlockDeviceList";
    public static String METHOD_SET_CONNECTED_DEVICE_BLOCK = "SetConnectedDeviceBlock";
    public static String METHOD_SET_DEVICE_UNBLOCK = "SetDeviceUnblock";
    public static String METHOD_SET_DEVICE_NAME = "SetDeviceName";
    public static String METHOD_GET_PROFILE_LIST = "GetProfileList";
    public static String METHOD_GET_SMS_CONTACT_LIST = "GetSMSContactList";
    public static String METHOD_GET_SMS_INIT_STATE = "getSmsInitState";
    public static String METHOD_GET_SMS_CONTENT_LIST = "GetSMSContentList";
    public static String METHOD_SAVE_SMS = "SaveSMS";
    public static String METHOD_DELETE_SMS = "DeleteSMS";
    public static String METHOD_SEND_SMS = "SendSMS";
    public static String METHOD_GET_SEND_SMS_RESULT = "GetSendSMSResult";
    public static String METHOD_GET_SINGLE_SMS = "GetSingleSMS";
    public static String METHOD_GET_WIFI_EXTENDER_SETTINGS = "GetWIFIExtenderSettings";
    public static String METHOD_SET_WIFI_EXTENDER_SETTINGS = "SetWIFIExtenderSettings";
    public static String METHOD_GET_WIFI_EXTENDER_CURRENT_STATUS = "GetWIFIExtenderCurrentStatus";
    public static String METHOD_GET_HOTSPOT_LIST = "GetHotspotList";
    public static String METHOD_SEARCH_HOTSPOT = "SearchHotspot";
    public static String METHOD_CONNECT_HOTSPOT = "ConnectHotspot";
    public static String METHOD_GET_CONNECT_HOTSPOT_STATE = "GetConnectHotspotState";
    public static String METHOD_DISCONNECT_HOTSPOT = "DisConnectHotspot";

    // 固定的用户名
    public static String ACCOUNT = "admin";
    // 备份文件2级目录
    public static String PATH_SMARTLINK = "/smartlink";
    // 备份文件3级文件
    public static String PATH_CONFIGURE_BIN = "/configure.bin";

}
