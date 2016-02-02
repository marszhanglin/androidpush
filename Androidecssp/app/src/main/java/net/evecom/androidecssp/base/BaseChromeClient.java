/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;

import net.evecom.androidecssp.R;

/**
 *
 * 描述 解析，渲染网页
 *
 * @author Mars zhang
 * @created 2015-11-5 下午5:54:59
 */
public class BaseChromeClient extends WebChromeClient {
    /** MemberVariables */
    private BaseWebActivity context;

    /**
     *
     * 描述
     *
     * @author Mars zhang
     * @created 2015-11-25 下午2:10:30
     * @param context
     */
    public BaseChromeClient(BaseWebActivity context) {
        super();
        this.context = context;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
        return super.onCreateWindow(view, dialog, userGesture, resultMsg);
    }

    /**
     *
     * 描述
     *
     * @author Mars zhang
     * @created 2015-11-25 下午2:10:36
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     * @see android.webkit.WebChromeClient#onJsAlert(android.webkit.WebView,
     *      java.lang.String, java.lang.String, android.webkit.JsResult)
     */
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("提示").setIcon(R.drawable.qq_dialog_default_icon).setMessage(message)
                .setPositiveButton("确定", null);
        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
        return true;
    }

    /**
     *
     * 描述
     *
     * @author Mars zhang
     * @created 2015-11-25 下午2:10:42
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     * @see android.webkit.WebChromeClient#onJsBeforeUnload(android.webkit.WebView,
     *      java.lang.String, java.lang.String, android.webkit.JsResult)
     */
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    /**
     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
     */
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("提示").setIcon(R.drawable.qq_dialog_default_icon).setMessage(message)
                .setPositiveButton("确定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).setNeutralButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    /**
     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
     * window.prompt('请输入您的域名地址', '618119.com');
     */
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("提示").setMessage(message);
        builder.setIcon(R.drawable.qq_dialog_default_icon);
        final EditText et = new EditText(view.getContext());
        et.setSingleLine();
        et.setText(defaultValue);
        builder.setView(et).setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.confirm(et.getText().toString());
            }
        }).setNeutralButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });

        // 禁止响应按back键的事件
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        System.out.println(newProgress);
        ProgressBar bar = context.lineProgressBar;
        if (newProgress == 100) {
            bar.setVisibility(View.GONE);
        } else {
            if (bar.getVisibility() == View.GONE)
                bar.setVisibility(View.VISIBLE);
            bar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
    }

    /**
     * 百度地图配置权限（同样在WebChromeClient中实现）
     */
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

}
