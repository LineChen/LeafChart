package com.beiing.linechartdemo.widget;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class SystemUtil {


    /**
     * 关闭键盘
     */
    public static void KeyBoardCancle(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getWindow().peekDecorView();
        if (view != null) {

            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 开启键盘
     */
    public static void KeyBoardOpen(Activity activity, View view) {

        InputMethodManager inputmanger = (InputMethodManager) activity
                .getSystemService(activity.INPUT_METHOD_SERVICE);
        inputmanger.showSoftInput(view, 0);
    }


    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void KeyBoardHiddent(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager manager = ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE));
        if (manager != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }

    }


    /**
     * 验证包名和信息
     *
     * @param context
     */
    public static void getSigInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            String name = packageInfo.packageName;
            if (!"cn.mama.activity".equals(name)) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            int key = sign.hashCode();
            if (1441633140 != key) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
            parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 验证签名是否正确
     *
     * @param signature
     */
    public static void parseSignature(byte[] signature) {
        try {
            CertificateFactory cFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cFactory.generateCertificate(new ByteArrayInputStream(signature));
            String publickey = cert.getPublicKey().toString();

            if (!"OpenSSLRSAPublicKey{modulus=df7172e600dd923905c1946bf3320bb214ecacba3a5eab354f4c0e2617e9e9c2a3935d1d4290882e96fcf449c22425a6451e63c286b55fcc357b7a89fe5d0b91fc1e834edcecd9469ef13e5cad235505716f79dafe631b16cae5217f511caf97f0b8e138bfafeb8e76ab41ea8fdce0c6a108cc153f36fd09667eda375557757f,publicExponent=10001}".equals(publickey)) {

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }

        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取渠道号
     *
     * @param context
     * @return
     */
    public static String getChannelId(Context context) {
        if (context == null) {
            return "channel";
        }
        PackageManager pm = context.getPackageManager();
        ApplicationInfo appinfo;
        String platform_id = "";
        try {
            appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = appinfo.metaData;
            platform_id = metaData.getString("UMENG_CHANNEL");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return platform_id;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 应用是否前台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
