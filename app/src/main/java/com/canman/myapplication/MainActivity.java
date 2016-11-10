package com.canman.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.canman.myapplication.Parser.AtRichParser;
import com.canman.myapplication.Parser.RichParserManager;
import com.canman.myapplication.View.RichEdittext;


public class MainActivity extends AppCompatActivity implements RichEdittext.DelPos {

    private RichEdittext mEditText;
    private String[] AT = {"暴走萝莉金克丝", "黑暗火女安妮", "琴瑟仙女娑娜", "九尾妖狐阿狸", "暗夜猎手薇恩", "皮城女警凯特琳", "光辉女郎拉克丝"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RichParserManager.getManager().registerRichParser(new AtRichParser());

        mEditText = (RichEdittext) findViewById(R.id.edittext);
        mEditText.setDelPos(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("@皮城女警凯特琳 \n");
        mEditText.setText(stringBuilder);
    }

    public void addAt(View view) {

        int random = (int) (Math.random() * AT.length);
        mEditText.insertRichItem(AT[random], new AtRichParser());
    }

    @Override
    public void delpos(int pos) {
        Toast.makeText(this,pos+"",Toast.LENGTH_LONG).show();
    }
}
