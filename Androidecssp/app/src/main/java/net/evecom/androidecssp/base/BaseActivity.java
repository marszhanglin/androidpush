/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import net.mutil.util.HttpUtil;
import net.tsz.afinal.FinalDb;

import java.io.IOException;
import java.util.HashMap;

/**
 * BaseActivity
 * 
 * @author Mars zhang
 * 
 */
public class BaseActivity extends Activity {
    /** 数据状态 */
    protected static final int MESSAGETYPE_01 = 0x0001;
    /** 数据状态 */
    protected static final int MESSAGETYPE_02 = 0x0002;
    /** 数据状态 */
    protected static final int MESSAGETYPE_03 = 0x0003;
    /** 数据状态 */
    protected static final int MESSAGETYPE_04 = 0x0004;
    /** 数据状态 */
    protected static final int MESSAGETYPE_05 = 0x0005;
    /** 数据状态 */
    protected static final int MESSAGETYPE_06 = 0x0006;
    /** FinalDb */
    private FinalDb db;
    /** 实体对象 */
    public static BaseActivity instance = null;
    /** view */
    public StringBuilder requestCode = new StringBuilder();

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static boolean DEBUG = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        db=BaseApplication.instence.db;
    }

    /**
     *
     * 描述
     *
     * @author Mars zhang
     * @created 2016-1-7 上午9:51:01
     * @param strUrl
     * @param entityMap
     * @return String
     * @throws IOException
     */
    protected String connServerForResultPost(String strUrl, HashMap<String, String> entityMap)
            throws  IOException {
        String strResult = "";
        if (null == entityMap) {
            entityMap = new HashMap<String, String>();
        }
        strResult = HttpUtil.connServerForResultPost(strUrl, entityMap);
        if (DEBUG) {
            Log.d(TAG, strResult);
        }
        return strResult;
    }
    /** 土司 */
    protected void toast(String strMsg, int L1S0) {
        Toast.makeText(getApplicationContext(), strMsg, L1S0).show();
    }
    /**
     *
     * 描述 展示图片
     *
     * @author Mars zhang
     * @created 2015-11-9 下午3:05:16
     */
    public void displayImage(ImageView imageView, String uriStr, HashMap<String, String> params) {
        uriStr += "?" + requestCode.toString();
        if (null != params) {
            Object[] mKeys = params.keySet().toArray();
            for (int i = 0; i < mKeys.length; i++) {
                uriStr += "&" + mKeys[i] + "=" + params.get(mKeys[i]);
            }
        }
        BaseApplication.instence.finalbitmap.display(imageView, uriStr);
    }

    /**
     *
     * 描述 展示图片
     *
     * @author Mars zhang
     * @created 2015-11-9 下午3:05:16
     */
    public void displayImageWithWidthHeight(ImageView imageView, String uriStr, HashMap<String, String> params,
                                            int width, int height) {
        uriStr += "?" + requestCode.toString();
        if (null != params) {
            Object[] mKeys = params.keySet().toArray();
            for (int i = 0; i < mKeys.length; i++) {
                uriStr += "&" + mKeys[i] + "=" + params.get(mKeys[i]);
            }
        }
        BaseApplication.instence.finalbitmap.display(imageView, uriStr, width, height);
    }

    /**
     *
     * 描述 展示图片
     *
     * @author Mars zhang
     * @created 2015-11-9 下午3:05:16
     */
    public void displayImage(ImageView imageView, String uriStr) {
        BaseApplication.instence.finalbitmap.display(imageView, uriStr);
    }

    /**
     *
     * 描述 getDb
     *
     * @author Mars zhang
     * @created 2015-11-30 下午3:01:55
     * @return
     */
    public FinalDb getDb() {
        if (null == db)
            db = FinalDb.create(instance);
        return db;
    };

    /**
     * 要加动画过度动画 所以要重写finish方法
     */
    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 要加动画过度动画 所以要重写startActivity方法
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

    }

    /**
     *
     * 描述 非空判断
     *
     * @author Mars zhang
     * @created 2015-11-25 下午2:09:28
     * @param valueobj
     * @param defaultValue
     * @return
     */
    public String ifnull(Object valueobj, String defaultValue) {
        String value = valueobj + "";
        if (null == value || value.equals("null")) {
            return defaultValue;
        } else {
            return value;
        }

    }


}
