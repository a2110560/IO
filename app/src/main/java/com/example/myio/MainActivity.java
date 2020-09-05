package com.example.myio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    //沙盒在清除資料或者是解除安裝時會直接消失
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView textView;
    private String name;
    private int counter = 0;
    private boolean issound;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //name:資料庫名稱
        myDBHelper=new MyDBHelper(this,"tcca",null,1);
        db=myDBHelper.getReadableDatabase();//當資料庫滿的時候readable會電電，writeable會拋出例外(兩者皆可增刪修查)

        name = sharedPreferences.getString("name", "nobody");
        counter = sharedPreferences.getInt("counter", 1);
        issound = sharedPreferences.getBoolean("sound", true);

        textView.setText(name + " : " + counter + " : " + issound);
        counter++;
        editor.putInt("counter", counter);
        editor.commit();
    }

    public void test1(View view) {
        name = "jack";
        editor.putString("name", name);
        editor.putBoolean("sound", false);
        editor.commit();
    }

    public void test2(View view) {
        //寫入資料(沙盒)
        try {
            FileOutputStream fout = openFileOutput("jack.txt", MODE_PRIVATE);
            fout.write("Hello".getBytes());
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test3(View view) {
        //讀出資料(沙盒)
        try {
            FileInputStream fin=openFileInput("jack.txt");
            BufferedReader reader=new BufferedReader(new InputStreamReader(fin));
            String line=reader.readLine();
            fin.close();
            textView.setText(line);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void test4(View view) {
        //資料庫查詢
        //SELECT * FROM custom;
        Cursor c=db.query("custom",null,null,null,null,null,null);

        while (c.moveToNext()){
            String id=c.getString(c.getColumnIndex("id"));
            String cname=c.getString(c.getColumnIndex("cname"));
            String tel=c.getString(c.getColumnIndex("tel"));
        }
    }

    public void test5(View view) {
        //新增
        ContentValues values=new ContentValues();
        values.put("cname","jack");
        values.put("tel","123123");
        db.insert("custom",null,values);
    }

    public void test6(View view) {
        //刪除
        //delete from custom where id=2;
        db.delete("custom","id= ?",new String[]{"2"});
    }

    public void test7(View view) {
        ContentValues values=new ContentValues();
        //修改
        values.put("cname","gary");
        values.put("tel","12123");
        db.update("custom",values,"id=?",new String[]{"3"});
    }
}