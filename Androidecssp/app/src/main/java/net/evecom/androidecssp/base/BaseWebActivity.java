/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import net.evecom.androidecssp.R;
import net.mutil.util.HttpUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 描述
 *
 * @author Mars zhang
 * @created 2015-11-25 上午11:41:07
 */
@EActivity(R.layout.message_post_web)
public class BaseWebActivity extends BaseActivity {
    /**
     * MemberVariables
     */
    @ViewById(R.id.wv_oauth_message)
    public WebView webView;
    /**
     * MemberVariables
     */
    protected Context mContext;
    /**
     * 分页
     */
    private String temp = "15";
    /**
     * 进度提示框
     */
    private AlertDialog dialogPress;
    /**
     * 图片
     */
    @ViewById(R.id.image_view_at_web)
    public ImageView imageView;
    /**
     * lineProgressBar
     */
    @ViewById(R.id.webview_progress_id)
    public ProgressBar lineProgressBar;
    @Extra
    public String requestUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @AfterViews
    public void AfterViews(){
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();

        webView.setWebViewClient(new BaseWebViewClient((BaseWebActivity) instance));
        webView.setWebChromeClient(new BaseChromeClient((BaseWebActivity) instance));

        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        // 设置WebView的属性，此时可以去执行JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);// support zoom
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        /** 百度地图 */
        // //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        // 最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
            // }else if(mDensity == DisplayMetrics..DENSITY_XHIGH){
            // webSettings.setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_HIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }


        webView.addJavascriptInterface(new JsInterface(getApplicationContext()), "androidbase");
        String url = HttpUtil.getPCURL() + requestUrl;
        // post访问需要提交的参数
        // 由于webView.postUrl(url, postData)中 postData类型为byte[] ，
        // 通过EncodingUtils.getBytes("&pwd=888", charset)方法进行转换
        webView.postUrl(url, new byte[]{});
    }

    // 下面代码没有添加，在我的手机里也隐藏地址栏了，但是有的设备可能还要加这些
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // if (dialog != null)
            // dialog.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * MemberVariables
     */
    private ProgressDialog mDialog;

    private void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);// 设置进度条是否为不明确
            mDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    mDialog = null;
                }
            });
            mDialog.show();

        }
    }

    private void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


}
