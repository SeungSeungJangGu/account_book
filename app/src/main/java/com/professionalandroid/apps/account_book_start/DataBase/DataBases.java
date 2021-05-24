package com.professionalandroid.apps.account_book_start.DataBase;

import android.provider.BaseColumns;

/*
*   데이터 베이스의 테이블의 속성 값 및 쿼리를 가지고있는 객체이다.
 */
public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String CONTENT = "content";
        public static final String INCOME = "income";
        public static final String EXPEND = "expend";
        public static final String DATE = "date";
        public static final String _TABLENAME0 = "account";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +CONTENT+" text, "
                +INCOME+" text, "
                +EXPEND+" text, "
                +DATE+" text not null );";
    }
}