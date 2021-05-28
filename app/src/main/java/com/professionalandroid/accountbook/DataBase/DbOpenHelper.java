package com.professionalandroid.accountbook.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
 *   데이터 베이스의 전체적인 정보를 담고있는 객체
 */
public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";     // 데이터 베이스의 이름
    private static final int DATABASE_VERSION = 1;                              // 데이터 베이스의 버전
    public static SQLiteDatabase mDB;                                           // SQLITE 객체
    private DatabaseHelper mDBHelper;                                           // DbOpenHelper 객체
    private Context mCtx;                                                       // DbOpenHelper를 오픈할 Context

     /*
     *   데이터 베이스의 실질적인
     *   쿼리를 실행하는 객체이다.
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        // 생성자
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }
    
    // DbOpenHelper 생성자
    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    // DB를 여는 메소드
    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();      // 쓰기 가능한 Sqlite객체를 가져와 삽입
        return this;
    }

    // DB를 생성
    public void create(){
        mDBHelper.onCreate(mDB);
    }

    // DB를 닫음
    public void close(){
        mDB.close();
    }

    // Record 값을 Insert
    public long insertColumn(String content,String income, String expend, String date){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.CONTENT, content);
        values.put(DataBases.CreateDB.INCOME, income);
        values.put(DataBases.CreateDB.EXPEND, expend);
        values.put(DataBases.CreateDB.DATE, date);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }
 
    // Select 쿼리 실행
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    // 정렬 select 쿼리 실행
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM account ORDER BY " + sort + ";", null);
        return c;
    }

    // Update 쿼리 실행
    public boolean updateColumn(long id,String content, String income, String expend, String date){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.CONTENT, content);
        values.put(DataBases.CreateDB.INCOME, income);
        values.put(DataBases.CreateDB.EXPEND, expend);
        values.put(DataBases.CreateDB.DATE, date);

        return mDB.update(DataBases.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // 한 Record에 대하여 Delete 쿼리실행
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }

    // 모든 Record delete 쿼리 실행
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }
}

