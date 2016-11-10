package com.canman.myapplication.Parser;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
public class RichParserManager {

    private static RichParserManager mInstance;
    private List<IRichParser> mRichPasers;

    private RichParserManager() {
        mRichPasers = new ArrayList<>();
    }

    public static RichParserManager getManager() {
        if (null == mInstance) {
            synchronized (RichParserManager.class) {
                if (null == mInstance) {
                    mInstance = new RichParserManager();
                }
            }
        }
        return mInstance;
    }

    public void registerRichParser(IRichParser richParser) {
        mRichPasers.add(richParser);
    }

    public boolean containsRichItem(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (IRichParser richItem : mRichPasers) {
            richItem.resetTargetStr(str);
            if (richItem.containsRichItem()) {
                return true;
            }
        }
        return false;
    }
    public String getLastRichItem(String targetStr) {

        final String str = targetStr;
        int index = -1;
        IRichParser iRichParser = null;
        for (IRichParser richItem : mRichPasers) {

            richItem.resetTargetStr(str);

            int temp = richItem.getLastRichIndex();
            if (temp > index) {
                index = temp;
                iRichParser = richItem;
            }
        }
        return iRichParser == null ? "" : iRichParser.getLastRichItem();
    }

    public int getLastRichposition(String targetStr) {

        final String str = targetStr;
        int index = -1;
        IRichParser iRichParser = null;
        for (IRichParser richItem : mRichPasers) {
            richItem.resetTargetStr(str);
            int temp = richItem.getLastRichIndex();
            if (temp > index) {
                index = temp;
                iRichParser = richItem;
            }
        }
        return iRichParser == null ? 0 : iRichParser.getLastRichPosition();
    }

    public String getFirstRichItem(String targetStr) {

        final String str = targetStr;
        int index = Integer.MAX_VALUE;
        IRichParser iRichParser = null;
        for (IRichParser richItem : mRichPasers) {
            richItem.resetTargetStr(str);

            int temp = richItem.getFirstRichIndex();
            if (temp < index && temp != -1) {
                index = temp;
                iRichParser = richItem;
            }
        }
        return iRichParser == null ? "" : iRichParser.getFirstRichItem();
    }
    public boolean isStartWithRichItem(String targetStr) {

        final String str = targetStr;
        if (!RichParserManager.getManager().containsRichItem(str)) {
            return false;
        }
        String firstTopic = RichParserManager.getManager().getFirstRichItem(str);
        return str.startsWith(firstTopic);
    }

    public boolean isEndWithRichItem(String targetStr) {

        final String str = targetStr;
        if (!RichParserManager.getManager().containsRichItem(str)) {
            return false;
        }
        String lastTopic = RichParserManager.getManager().getLastRichItem(str);
        return str.endsWith(lastTopic);
    }

    public SpannableStringBuilder parseRichItems(Context context, String targetStr) {
        final String str = targetStr;
        if (!RichParserManager.getManager().containsRichItem(str)) {
            return new SpannableStringBuilder(str);
        }
        String tempStr = str;
        String richStr = getFirstRichItem(tempStr);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        while (!TextUtils.isEmpty(richStr)) {
            int index = tempStr.indexOf(richStr);
            String startStr = tempStr.substring(0, index);
            ssb.append(startStr);
            ssb.append(formateRichStr(context, richStr));
            tempStr = tempStr.substring(index + richStr.length(), tempStr.length());
            richStr = getFirstRichItem(tempStr);
        }

        ssb.append(tempStr);

        return ssb;
    }

    private SpannableString formateRichStr(Context context, String richStr) {

        final String str = richStr;
        for (IRichParser richItem : mRichPasers) {

            richItem.resetTargetStr(richStr);

            if (richItem.containsRichItem()) {
                return richItem.getRichSpannable(context, richStr);
            }
        }
        return new SpannableString(str);
    }
}
