package org.techtown.diet;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class History extends AppCompatActivity {
    ListView listView;
    UsrAdapter adapter;
    SQLiteDatabase database;
    // context 추가
    public static Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CONTEXT = this;

        listView = (ListView) findViewById(R.id.history_List);

        openDatabase("Record");
        createTable("userTable");

        adapter = new UsrAdapter();
        selectData("userTable");

        listView.setAdapter(adapter);

        //다이얼로그
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제").setMessage("정말로 삭제하시겠습니까?");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 클릭 시 토스트 이벤트, usrid를 전달시켜야 함
                UsrItem item = (UsrItem) adapter.getItem(position);
                //Toast.makeText(getApplicationContext(), "선택 : " + item.getUsrid(), Toast.LENGTH_LONG).show();

                // 화면 전환
                Intent BankIntent = new Intent(getApplicationContext(), BankActivity.class);
                // 게시물의 번호와 userid를 가지고 DetailActivity 로 이동
                BankIntent.putExtra("tot", item.getTot());
                BankIntent.putExtra("intake", item.getIntake());
                // usrid말고 usrnum을 불러올 방법?
                BankIntent.putExtra("usrnum", item.getNum());
                //finish 추가
                finish();
                startActivity(BankIntent);
            }
        });
        //꾸욱 누르기
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                UsrItem item = (UsrItem) adapter.getItem(position);
                int del_num = item.getNum();
                String str_num = Integer.toString(del_num);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        removeUser(str_num);
                        History.super.recreate();
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



                //int checked = listView.getCheckedItemPosition();
               // adapter.notifyDataSetChanged();
                return true;
            }
        });

        int size = 0;
        String[] record = new String[2];
        List<String[]> records = new ArrayList<>();
        // Linier로 수정(Table )
        //TableLayout historyTable = (TableLayout) findViewById(R.id.historyTable);
        LinearLayout historyTable = (LinearLayout) findViewById(R.id.historyTable);

        try {
            FileInputStream fileIn=openFileInput("history_data");
            Scanner scanner = new Scanner(fileIn);
            scanner.useDelimiter(",");


            while(scanner.hasNext()) {
                record = scanner.nextLine().split(",");
                records.add(record);
                size++;
                }

            } catch (Exception e) {
                e.printStackTrace();
                }
        // 원래 리스트 등록하던 곳

    }//oncreate 완료
    // tot 갱신을 위한 리스트 뷰 갱신
    /*
    public void resumeTot(){
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.history_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help_bank) {

            Intent i = new Intent(this,HelpBank.class);
            startActivity(i);
        }
        // 메인 엑티비티로
        if (id == R.id.action_exit_history){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);

        }
        
        return super.onOptionsItemSelected(item);
    }



    // SQLite 작업 정리
    public void openDatabase (String databaseName){
        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        if (database != null) ;
    }

    public void createTable (String tableName){
        if (database != null) {
            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, date text, intake integer, weight double, tot integer)";
            database.execSQL(sql);
        }
    }

    public void  removeUser(String uuid){
        if(database != null) {
            database.delete("userTable", "_id = ?", new String[]{uuid});
        }
        //초기화 안됨
        //listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    // sqlite 선택
    public  void selectData(String tableName){
        if(database != null){
            String sql = "select _id, date, intake, weight, tot from "+tableName;
            Cursor cursor = database.rawQuery(sql, null);

            for( int i = 0; i< cursor.getCount(); i++){
                cursor.moveToNext();//다음 레코드로 넘어간다.
                int num = cursor.getInt(0);
                String date = cursor.getString(1);
                int intake = cursor.getInt(2);
                double weight = cursor.getDouble(3);
                int tot = cursor.getInt(4);

                adapter.addItem(new UsrItem(num, date, intake, weight, tot));
            }
            cursor.close();
        }
    }


    class UsrAdapter extends BaseAdapter {
        ArrayList<UsrItem> items = new ArrayList<UsrItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(UsrItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            UsrItemView view = new UsrItemView(getApplicationContext());

            UsrItem item = items.get(position);
            view.setDate(item.getDate());
            view.setIntake(item.getIntake());
            view.setWeight(item.getWeight());
            view.setTot(item.getTot());

            return view;
        }
    }


}
