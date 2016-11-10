package com.canman.myapplication.Parser;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractRichParser implements IRichParser {

    private String mTargetStr;

    public void resetTargetStr(String text) {
        mTargetStr = text;
    }

    public boolean containsRichItem() {

        if (TextUtils.isEmpty(mTargetStr)) {
            return false;
        }
        Pattern p = Pattern.compile(getRichPattern());
        Matcher matcher = p.matcher(mTargetStr);
        return matcher.find();
    }


    public String getFirstRichItem() {

        if (TextUtils.isEmpty(mTargetStr)) {
            return "";
        }
        Pattern p = Pattern.compile(getRichPattern());
        Matcher matcher = p.matcher(mTargetStr);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    public int getFirstRichIndex() {

        if (TextUtils.isEmpty(mTargetStr)) {
            return -1;
        }
        String item = getFirstRichItem();
        if (TextUtils.isEmpty(item)) {
            return -1;
        }
        return mTargetStr.indexOf(item);
    }

    public String getLastRichItem() {

        if (TextUtils.isEmpty(mTargetStr)) {
            return "";
        }
        Pattern p = Pattern.compile(getRichPattern());
        Matcher matcher = p.matcher(mTargetStr);

        String result = "";
        while (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    public int getLastRichPosition() {
        int i=0;
        if (TextUtils.isEmpty(mTargetStr)) {
            return 0;
        }
        Pattern p = Pattern.compile(getRichPattern());
        Matcher matcher = p.matcher(mTargetStr);

        while (matcher.find()) {
            i++;
        }
        return i;
    }

    public int getLastRichIndex() {

        if (TextUtils.isEmpty(mTargetStr)) {
            return -1;
        }
        String item = getLastRichItem();
        if (TextUtils.isEmpty(item)) {
            return -1;
        }
        return mTargetStr.lastIndexOf(item);
    }
}
