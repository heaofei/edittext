package com.canman.myapplication.Parser;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

public class AtRichParser extends AbstractRichParser {


    public String getRichPattern() {
        return "@[^@]\\S+ ";
    }


    public String getRichText(String richStr) {
        return String.format(" @%s ", richStr);
    }


    public SpannableString getRichSpannable(Context context, String richStr) {

        if (TextUtils.isEmpty(richStr)) {
            return new SpannableString("");
        }
        String str = richStr;
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan highLightSpan = new ForegroundColorSpan(Color.parseColor("#009933"));
        spannableString.setSpan(highLightSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
