package com.canman.myapplication.Parser;

import android.content.Context;
import android.text.SpannableString;

public interface IRichParser {
    void resetTargetStr(String text);
    String getRichPattern();
    boolean containsRichItem();
    String getFirstRichItem();
    int getFirstRichIndex();
    String getLastRichItem();
    int getLastRichIndex();
    String getRichText(String richStr);
    SpannableString getRichSpannable(Context context, String richStr);
    int  getLastRichPosition();
}
