package edu.fjnu.birdie.notepad2.Utils;

import android.provider.BaseColumns;

/**
 * Created by edge0 on 2017/5/16.
 */

public final class NotePad {

    //分类
    public static final String CATEGORY_DELETED = "deleted";         //删除
    public static final String CATEGORY_NORMAL = "normal";           //笔记
    public static final String CATEGORY_IMPORTANT = "important";    // 重要 置顶
    public static final String CATEGORY_MEMO = "memo";              //备忘
    public static final String CATEGORY_PWD= "pwd";                 //加密

    private NotePad() {
    }

    public static final class Notes implements BaseColumns {

        public static final String TABLE_NAME_NOTES = "note";

        public static final String COLUMN_NAME_NOTE_TITLE = "title";
        public static final String COLUMN_NAME_NOTE_CONTENT = "content";
        public static final String COLUMN_NAME_NOTE_CATEGORY= "category";
        public static final String COLUMN_NAME_NOTE_DATE = "date";
        public static final String COLUMN_NAME_NOTE_PASSWORD = "password";
        public static final String COLUMN_NAME_NOTE_MEMOTIME = "memotime";

    }




}
