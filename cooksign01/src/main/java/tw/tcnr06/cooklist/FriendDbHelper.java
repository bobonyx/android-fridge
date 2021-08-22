package tw.tcnr06.cooklist;
//---------------------------舊程式


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

////----------------------------------------------------------
//建構式參數說明：
//context   可以操作資料庫的內容本文，一般可直接傳入Activity物件。
//name  要操作資料庫名稱，如果資料庫不存在，會自動被建立出來並呼叫onCreate()方法。
//factory  用來做深入查詢用，入門時用不到。
//version  版本號碼。
////-----------------------
public class FriendDbHelper extends SQLiteOpenHelper {
    String TAG = "tcnr06=>";
    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "friends.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_TABLE = "recipe";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql = "CREATE TABLE " + DB_TABLE + " ( "
            + "id INTEGER PRIMARY KEY," + "title TEXT NOT NULL," + "recipe_text TEXT);";
    private static SQLiteDatabase database;

    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new FriendDbHelper(context, DB_FILE, null, VERSION).getWritableDatabase();
        }
        return database;
    }

    public FriendDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
        super(context, "friends.db", null, 1);
        sCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    public long insertRec(String b_title, String b_recipe_text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("title", b_title);
        rec.put("recipe_text", b_recipe_text);

        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }

    //    ContentValues values
    public long insertRec_m(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }

    public int RecCount() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
//-------------------------

    public int clearRec() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }


    public int updateRec(String b_id, String b_title, String b_recipe_text) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
//			rec.put("id", b_id);
            rec.put("title", b_title);
            rec.put("recipe_text", b_recipe_text);


            String whereClause = "id='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE, rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int deleteRec(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause = "id='" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public ArrayList<String> getRecSet_query() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE +
                " WHERE title LIKE ? AND recipe_text LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%%",
                "%%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
}//-----END
//---------------------------新程式
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import java.util.ArrayList;
//
//////----------------------------------------------------------
////建構式參數說明：
////context   可以操作資料庫的內容本文，一般可直接傳入Activity物件。
////name  要操作資料庫名稱，如果資料庫不存在，會自動被建立出來並呼叫onCreate()方法。
////factory  用來做深入查詢用，入門時用不到。
////version  版本號碼。
//////-----------------------
//public class FriendDbHelper extends SQLiteOpenHelper {
//    String TAG = "oldpa=>";
//    public String sCreateTableCommand;    // 資料庫名稱
//    private static final String DB_FILE = "friends.db";
//    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
//    public static final int VERSION = 1;    // 資料表名稱
//    private static final String DB_TABLE = "recipe";    // 資料庫物件，固定的欄位變數
//    private static final String crTBsql = "CREATE TABLE " + DB_TABLE + " ( "
//            + "id INTEGER PRIMARY KEY," + "title TEXT NOT NULL," + "recipe_text TEXT);";
//    private static SQLiteDatabase database;
//
//    //----------------------------------------------
//    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
//    public static SQLiteDatabase getDatabase(Context context) {
//        if (database == null || !database.isOpen()) {
//            database = new FriendDbHelper(context, DB_FILE, null, VERSION).getWritableDatabase();
//        }
//        return database;
//    }
//
//    public FriendDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
////        super(context, name, factory, version);
//        super(context, "friends.db", null, 1);
//        sCreateTableCommand = "";
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(crTBsql);
//    }
//
//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.d(TAG, "onUpgrade()");
//        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
//        onCreate(db);
//    }
//
//    public long insertRec(String b_title, String b_recipe_text) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues rec = new ContentValues();
//        rec.put("title", b_title);
//        rec.put("recipe_text", b_recipe_text);
//
//        long rowID = db.insert(DB_TABLE, null, rec);
//        db.close();
//        return rowID;
//    }
//
//    //    ContentValues values
//    public long insertRec_m(ContentValues rec) {
//        SQLiteDatabase db = getWritableDatabase();
//        long rowID = db.insert(DB_TABLE, null, rec);
//        db.close();
//        return rowID;
//    }
//
//    public int RecCount() {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE;
//        Cursor recSet = db.rawQuery(sql, null);
//        int count = recSet.getCount();
//        recSet.close();
//        return count;
//    }
//
//    public ArrayList<String> getRecSet() {
//        SQLiteDatabase db = getReadableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE;
//        Cursor recSet = db.rawQuery(sql, null);
//        ArrayList<String> recAry = new ArrayList<String>();
//        //----------------------------
//        int columnCount = recSet.getColumnCount();
//        while (recSet.moveToNext()) {
//            String fldSet = "";
//            for (int i = 0; i < columnCount; i++)
//                fldSet += recSet.getString(i) + "#";
//            recAry.add(fldSet);
//        }
//        //------------------------
//        recSet.close();
//        db.close();
//        return recAry;
//    }
////-------------------------
//
//    public int clearRec() {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE;
//        Cursor recSet = db.rawQuery(sql, null);
//        if (recSet.getCount() != 0) {
////			String whereClause = "id < 0";
//            int rowsAffected = db.delete(DB_TABLE, "1", null);
//            // From the documentation of SQLiteDatabase delete method:
//            // To remove all rows and get a count pass "1" as the whereClause.
//            recSet.close();
//            db.close();
//            return rowsAffected;
//        } else {
//            recSet.close();
//            db.close();
//            return -1;
//        }
//    }
//
//
//    public int updateRec(String b_id, String b_title, String b_recipe_text) {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE;
//        Cursor recSet = db.rawQuery(sql, null);
//
//        if (recSet.getCount() != 0) {
//            ContentValues rec = new ContentValues();
////			rec.put("id", b_id);
//            rec.put("title", b_title);
//            rec.put("recipe_text", b_recipe_text);
//
//
//            String whereClause = "id='" + b_id + "'";
//
//            int rowsAffected = db.update(DB_TABLE, rec, whereClause, null);
//            recSet.close();
//            db.close();
//            return rowsAffected;
//        } else {
//            recSet.close();
//            db.close();
//            return -1;
//        }
//    }
//
//    public int deleteRec(String b_id) {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE;
//        Cursor recSet = db.rawQuery(sql, null);
//        if (recSet.getCount() != 0) {
//            String whereClause = "id='" + b_id + "'";
//            int rowsAffected = db.delete(DB_TABLE, whereClause, null);
//            recSet.close();
//            db.close();
//            return rowsAffected;
//        } else {
//            recSet.close();
//            db.close();
//            return -1;
//        }
//    }
//
//    public ArrayList<String> getRecSet_query(String t_title, String t_recipe_text) {
//        SQLiteDatabase db = getReadableDatabase();
//
//        String sql = "SELECT * FROM " + DB_TABLE +
//                " WHERE t_title LIKE ? AND t_recipe_text LIKE ? ORDER BY id ASC";
//        String[] args = new String[]{"%" + t_title.toString() + "%",
//                "%" + t_recipe_text.toString() + "%"};
//
//        Cursor recSet = db.rawQuery(sql, args);
//        ArrayList<String> recAry = new ArrayList<String>();
//        //----------------------------
//        int columnCount = recSet.getColumnCount();
//        while (recSet.moveToNext()) {
//            String fldSet = "";
//            for (int i = 0; i < columnCount; i++)
//                fldSet += recSet.getString(i) + "#";
//            recAry.add(fldSet);
//        }
//        //------------------------
//        recSet.close();
//        db.close();
//        return recAry;
//    }
//}//-----END