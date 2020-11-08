package com.example.listview.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.os.IResultReceiver;

import com.example.listview.Model.LanguageInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    public  static String DB_Name="Language_Db";
    public static Integer DB_Ver=1;
    SQLiteDatabase db;
    private String tbl_language="tblLanguage";
    private String colName="Name";
    private String colDescription="Description";
    private String colreleaseDate="releaseDate";
    private String colID="ID";

    public DbHelper(Context context)
    {
        //Code to create database
        super(context,DB_Name,null,DB_Ver);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Code to create table
        String query="CREATE TABLE "+
                tbl_language +
                " ("+colID+" INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                colName+" TEXT," +
            colDescription + " TEXT," +
            colreleaseDate + " DATE)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //code to create table

        //Drop table
        String query="DROP TABLE IF EXISTS "+
                tbl_language;

        sqLiteDatabase.execSQL(query);

        //Call onCreate Again
        onCreate(sqLiteDatabase);
    }
//Create for Language
    public int Insert(LanguageInfo info)
    {
        db=getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(colName,info.getName());
        values.put(colDescription,info.getDescription());
        values.put(colreleaseDate,info.getReleaseDate().toString());

        db.insert(tbl_language,null,values);
        db=getReadableDatabase();
        Cursor cur= db.rawQuery("select max("+colID+") ID from " + tbl_language,null);
        cur.moveToNext();
        int result = Integer.parseInt(cur.getString(0));
        return  result;
    }

    public  int Update(LanguageInfo info)
    {
        db=getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(colName,info.getName());
        values.put(colDescription,info.getDescription());
        values.put(colreleaseDate,info.getReleaseDate().toString());

        int result= db.update(tbl_language,values, colID + " =?",new String[]{String.valueOf(info.getId())});
        return  result;
    }

    public ArrayList<LanguageInfo> GetAll() throws ParseException {
        ArrayList<LanguageInfo> infoList=new ArrayList<>();

        SQLiteDatabase db=getWritableDatabase();

        Cursor cur=db.rawQuery("SELECT * FROM "+tbl_language,null);

        if(cur.moveToFirst())
        {
            while(cur.moveToNext()){
                LanguageInfo info=new LanguageInfo();
                info.setId(Integer.parseInt(cur.getString(cur.getColumnIndex(colID))));
                info.setName(cur.getString(cur.getColumnIndex(colName)));
                info.setDescription(cur.getString(cur.getColumnIndex(colDescription)));

                info.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(cur.getString(cur.getColumnIndex(colreleaseDate))));

                infoList.add(info);
            }


        }
        return infoList;
    }

    public void Delete(LanguageInfo info)
    {
        db=getWritableDatabase();

        db.execSQL("delete from " +tbl_language+ " where " +colID+" ="+info.getId());

    }
}
