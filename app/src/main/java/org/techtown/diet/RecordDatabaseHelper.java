package org.techtown.diet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;

public class RecordDatabaseHelper extends SQLiteOpenHelper {
    private String TAG ="RecordDatabaseHelper";

    public static final String DB_NAME = "Record";

    private static final String CREATE_RECORD_DB = "create table Record ("
            + "id integer primary key autoincrement, "
            + "uuid text, "
            + "type integer, "
            + "usrnum integer REFERENCES userTable(_id) ON DELETE CASCADE, "
            + "category text, "
            + "remark text, "
            + "amount double, "
            + "time integer, "
            + "date date )";

    public RecordDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addRecord(RecordBean bean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid",bean.getUuid());
        values.put("type",bean.getType());
        values.put("usrnum", bean.getNum());
        values.put("category",bean.getCategory());
        values.put("remark",bean.getRemark());
        values.put("amount",bean.getAmount());
        values.put("date",bean.getDate());
        values.put("time",bean.getTimeStamp());
        db.insert(DB_NAME,null,values);
        values.clear();
        Log.d(TAG,bean.getUuid()+"added");
    }

    public void  removeRecord(String uuid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"uuid = ?",new String[]{uuid});
    }


    public void editRecord(String uuid, int usrnum, RecordBean record){
        removeRecord(uuid);
        record.setUuid(uuid);
        record.setNum(usrnum);
        addRecord(record);
    }
    // String sql = "update userTable set tot = (select amount from Record where userTable._id = Record.usrnum)";
    public void updateTot(int num, String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            String sql = "update userTable set tot = " + amount + " where _id = "+ num ;
            db.execSQL(sql);
        }
    }
    // 리스트는 아이디가 일치하는 것만 출력하게 만들 예정
    public LinkedList<RecordBean> readRecords(String usridStr, String dateStr){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where usrnum = ? order by time asc",new String[]{usridStr});
        if (cursor.moveToFirst()){
            do{
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                int usrnum = cursor.getInt(cursor.getColumnIndex("usrnum"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));

                RecordBean record = new RecordBean();
                record.setUuid(uuid);
                record.setType(type);
                record.setNum(usrnum);
                record.setCategory(category);
                record.setRemark(remark);
                record.setAmount(amount);
                record.setDate(date);
                record.setTimeStamp(timeStamp);

                records.add(record);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public LinkedList<String> getAvaliableDate( String usrid ){

        LinkedList<String> dates = new LinkedList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where usrnum = ? order by date asc",new String[]{ usrid });
        if (cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                if (!dates.contains(date)){
                    dates.add(date);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }


}
