package com.canman.myapplication.View;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.canman.myapplication.Parser.IRichParser;
import com.canman.myapplication.Parser.RichParserManager;


public class RichEdittext extends EditText implements View.OnKeyListener {
    private int mOldSelStart, mOldSelEnd;
    private int mNewSelStart, mNewSelEnd;
    private CharSequence mContentStr = "";

    public DelPos getDelPos() {
        return delPos;
    }

    public void setDelPos(DelPos delPos) {
        this.delPos = delPos;
    }

    private DelPos delPos;

    public RichEdittext(Context context) {
        this(context, null);
    }

    public RichEdittext(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(Color.WHITE);
        setOnKeyListener(this);
    }

    public interface DelPos{
        void delpos(int pos);
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (startStrEndWithRichItem() && getSelectionStart() == getSelectionEnd()) {

                int startPos = getSelectionStart();
                final String startStr = toString().substring(0, startPos);

                String richItem = RichParserManager.getManager().getLastRichItem(startStr);

                int position = RichParserManager.getManager().getLastRichposition(startStr);
                if (delPos!=null){
                    delPos.delpos(position -1);
                }

                int lenth = richItem.length();
                String temp = startStr.substring(0, startStr.length() - lenth);
                setText(temp + toString().substring(startPos, toString().length()));
                setSelection(temp.length());

                return true;
            }
        }
        return false;
    }

    private boolean endStrStartWithRichItem() {

        int endPos = getSelectionEnd();
        String endStr = toString().substring(endPos, toString().length());
        if (!RichParserManager.getManager().containsRichItem(endStr)) {
            return false;
        }

        String firstTopic = RichParserManager.getManager().getFirstRichItem(endStr);
        return endStr.startsWith(firstTopic);
    }

    public boolean startStrEndWithRichItem() {

        int startPos = getSelectionStart();
        final String startStr = toString().substring(0, startPos);
        if (!RichParserManager.getManager().containsRichItem(startStr)) {
            return false;
        }

        String lastTopic = RichParserManager.getManager().getLastRichItem(startStr);
        return startStr.endsWith(lastTopic);
    }

    @Override
    public void setSelection(int start, int stop) {
        if (0 <= start && stop <= getText().toString().length()) {

            mNewSelStart = start;
            mNewSelEnd = stop;
            super.setSelection(start, stop);
        }
    }

    @Override
    protected void onSelectionChanged(final int selStart, final int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (0 == selStart && 0 == selEnd) {
            mOldSelStart = selStart;
            mOldSelEnd = selEnd;
            return;
        }
        if (selStart == mNewSelStart && selEnd == mNewSelEnd) {
            mOldSelStart = selStart;
            mOldSelEnd = selEnd;
            return;
        }

        int targetStart = selStart, targetEnd = selEnd;
        String text = toString();
        if (selStart == selEnd && Math.abs(selStart - mOldSelStart) > 1) {
            int pos = getRecommendSelection(selStart);
            if (-1 != pos) {
                setSelection(pos, pos);
                return;
            }
        } else {
            if (mOldSelStart < selStart) {
                int startPos = selStart - 1;
                String endStr = text.substring(startPos, text.length());
                if (RichParserManager.getManager().isStartWithRichItem(endStr)) {

                    String richStr = RichParserManager.getManager().getFirstRichItem(endStr);
                    targetStart = startPos + richStr.length();
                }
            }
            else if (mOldSelStart > selStart) {

                int startPos = selStart + 1;
                startPos = startPos < text.length() ? startPos : text.length();
                String startStr = text.substring(0, startPos);
                if (RichParserManager.getManager().isEndWithRichItem(startStr)) {

                    String richStr = RichParserManager.getManager().getLastRichItem(startStr);
                    targetStart = startPos - richStr.length();
                }
            }
            if (mOldSelEnd < selEnd) {

                int endPos = selEnd - 1;
                String endStr = text.substring(endPos, text.length());
                if (RichParserManager.getManager().isStartWithRichItem(endStr)) {

                    String richStr = RichParserManager.getManager().getFirstRichItem(endStr);
                    targetEnd = endPos + richStr.length();
                }
            }
            else if (mOldSelEnd > selEnd) {

                int endPos = selEnd + 1;
                String startStr = text.substring(0, endPos);
                if (RichParserManager.getManager().isEndWithRichItem(startStr)) {

                    String richStr = RichParserManager.getManager().getLastRichItem(startStr);
                    targetEnd = endPos - richStr.length();
                }
            }
        }
        mOldSelStart = selStart;
        mOldSelEnd = selEnd;
        mNewSelStart = targetStart;
        mNewSelEnd = targetEnd;
        setSelection(targetStart, targetEnd);
    }

    private int getRecommendSelection(int pos) {

        String text = toString();
        if (TextUtils.isEmpty(text)) {
            return -1;
        }
        String startStr = text.substring(0, pos);
        String richStr = RichParserManager.getManager().getLastRichItem(startStr);
        int start = 0;
        if (!TextUtils.isEmpty(richStr)) {

            start = startStr.lastIndexOf(richStr) + richStr.length();
        }
        String endStr = text.substring(pos, text.length());
        richStr = RichParserManager.getManager().getFirstRichItem(endStr);
        int end = text.length();
        if (!TextUtils.isEmpty(richStr)) {

            end = startStr.length() + endStr.indexOf(richStr);
        }
        String middleStr = text.substring(start, end);
        richStr = RichParserManager.getManager().getFirstRichItem(middleStr);
        if (TextUtils.isEmpty(richStr)) {
            return -1;
        }
        start = start + middleStr.indexOf(richStr);
        end = start + richStr.length();
        return (pos - start < end - pos) ? start : end;
    }
     public int insertRichItem(String richKeyword, IRichParser richItem) {

        final IRichParser item = richItem;
        if (null == item) {
            return -1;
        }
        int currentPos = getSelectionStart();

        String text = toString();
        String startStr;
        if (0 == currentPos) {

            startStr = "";
        } else {

            startStr = text.substring(0, currentPos);
        }
        String endStr;
        if (currentPos == text.length()) {

            endStr = "";
        } else {

            endStr = text.substring(currentPos, text.length());
        }
        String richText = item.getRichText(richKeyword);
        setText(startStr + richText + endStr);
        setSelection(currentPos + richText.length());


        final String temp = toString().substring(0, currentPos);
        return  RichParserManager.getManager().getLastRichposition(temp);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (!TextUtils.equals(mContentStr, text)) {
            mContentStr = text;
            SpannableStringBuilder spannableStr = RichParserManager.getManager().parseRichItems(getContext(), mContentStr.toString());
            setText(spannableStr);
        }
    }

    @Override
    public String toString() {
        return mContentStr == null ? "" : mContentStr.toString();
    }

}
