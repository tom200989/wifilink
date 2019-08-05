package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.User_LoginState;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.utils.ToastUtil_m;

/**
 * Created by qianli.ma on 2017/11/16 0016.
 */

public abstract class LogoutHelper {

    private Activity context;

    public abstract void logoutFinish();

    public LogoutHelper(Activity context) {
        this.context = context;
        logout();
    }

    private void logout() {
        RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
            @Override
            protected void onSuccess(User_LoginState result) {
                int state = result.getState();
                if (state == Cons.LOGIN) {
                    RX.getInstant().logout(new ResponseObject() {
                        @Override
                        protected void onSuccess(Object result) {
                            ToastUtil_m.show(context, context.getString(R.string.login_logout_successful));
                            logoutFinish();
                        }

                        @Override
                        protected void onResultError(ResponseBody.Error error) {
                            ToastUtil_m.show(context, context.getString(R.string.login_logout_failed));
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil_m.show(context, context.getString(R.string.login_logout_failed));
                        }
                    });
                }
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                ToastUtil_m.show(context, context.getString(R.string.login_logout_failed));
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil_m.show(context, context.getString(R.string.login_logout_failed));
            }
        });
    }

}
