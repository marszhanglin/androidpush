package net.evecom.androidecssp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.evecom.androidecssp.base.BaseActivity;
import net.mutil.util.ShareUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    @ViewById
    public EditText welcome_user_edit;
    @ViewById
    public EditText welcome_password_edit;
    @ViewById
    public CheckBox welcom_checkbox_jzmm;
    @ViewById
    public CheckBox welcom_checkbox_zddr;
    /**
     * SharedPreferences
     */
    private SharedPreferences passnameSp;
    /**
     * 是否需要调整定位设置
     */
    private Boolean isNeedGpsSet = false;
    /**
     * 是否真正登入 防止重复提交
     **/
    private Boolean islogining = false;
    /**
     * login进度条
     */
    private ProgressDialog loginProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void show() {
        toast(this.getLocalClassName(), 1);
        initCheckBoxListener();
        passnameSp = this.getSharedPreferences("PASSNAME", 0);
        askForOpenGPS();
    }

    /**
     * 请求打开gps
     */
    private void askForOpenGPS() {
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(),
                LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            isNeedGpsSet = true;
            toast("请点击定位服务,并打开GPS卫星选项,否则定位无法正常工作!", 1);
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivityForResult(intent, 0);
        }
    }

    private void initCheckBoxListener() {
        welcom_checkbox_jzmm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Click(R.id.main_btn)
    public void main_btn(View view) {
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG).show();
    }

    private void iflogin() {
        SharedPreferences.Editor editor = passnameSp.edit();
        String autologin = ShareUtil.getString(getApplicationContext(), "PASSNAME", "autologin", "0");
        String rembernp = ShareUtil.getString(getApplicationContext(), "PASSNAME", "rembernp", "0");
        String username = ShareUtil.getString(getApplicationContext(), "PASSNAME", "username", "");
        String password = ShareUtil.getString(getApplicationContext(), "PASSNAME", "password", "");
        // 给EditText赋值
        welcome_user_edit.setText(username);

        // 判断是否打钩
        if (autologin.equals("1")) {
            welcom_checkbox_zddr.setChecked(true);
        } else {
            welcom_checkbox_zddr.setChecked(false);
        }
        if (rembernp.equals("1")) {
            welcome_password_edit.setText(password);
            welcom_checkbox_jzmm.setChecked(true);
        } else {
            editor.putString("password", "");
            welcom_checkbox_jzmm.setChecked(false);
        }
        editor.commit();
        // 记住密码且自动登陆时提及登陆请求
        if (autologin.equals("1") && rembernp.equals("1") && !isNeedGpsSet) {
            Log.v("mars", "自动登入");
            loginsubmit(username, password);
        }
    }

    /**
     * 登陆请求
     */
    private void loginsubmit(final String username, final String password) {
        if (islogining) {
            return;
        }

        /** 保存登录名 */
        final SharedPreferences.Editor editor = passnameSp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();

        // 打开进度条
        loginProgressDialog = ProgressDialog.show(this, "提示", "正在登入，请稍等...");
        loginProgressDialog.setCancelable(true);

        submit("abcdef");

    }

    @Background
    public void submit(String aa) {
        System.out.println(aa);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hadlersubmit(9);
    }

    @UiThread
    public void hadlersubmit(int value){
        toast(value+"",1);

        //跳转到首页
        Intent intent =new Intent(instance, MainActivity_.class);
        startActivity(intent);
    }

    @Click
    public void welcome_login_btn(){
        // 打开进度条
        loginProgressDialog = ProgressDialog.show(this, "提示", "正在登入，请稍等...");
        loginProgressDialog.setCancelable(true);
        submit("abcdef");
    }

}
