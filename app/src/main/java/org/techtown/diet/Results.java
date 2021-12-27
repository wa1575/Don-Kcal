package org.techtown.diet;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
// 칼로리 계산 결과 출력하는 액티비티
public class Results extends AppCompatActivity {
    SQLiteDatabase database;

    // 결과를 받아서 표시하기 위한 변수
    TextView bmrValue,tdeeValue,bmiValue,bmiDetail,tryingToView;
    TextView totValue;
    Button historyButton,logButton;
    double bmi=0.0,uWeight=0.0;
    int bmr=0,tdee=0,inCalories=0;
    int tot=0;
    int totCal=0;
    String ttmsg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bmrValue = (TextView) findViewById(R.id.bmrValue);
        tdeeValue = (TextView) findViewById(R.id.tdeeValue);
        bmiValue = (TextView) findViewById(R.id.bmiValue);
        bmiDetail = (TextView) findViewById(R.id.bmiDetail);
        historyButton = (Button) findViewById(R.id.historyButton2);
        logButton = (Button) findViewById(R.id.logButton);
        tryingToView = (TextView) findViewById(R.id.tryingToView);

        // SQLite 도 적용
        openDatabase("Record");
        createTable("userTable");


        // get data from intent
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null){

            bmr = (int)extrasBundle.getDouble("bmr");
            tdee = (int)extrasBundle.getDouble("tdee");
            bmi = extrasBundle.getDouble("bmi");
            uWeight = Math.round(extrasBundle.getDouble("weight")*100.0)/100.0;
            ttmsg = extrasBundle.getString("tt");

        }

        String msg;
        if (bmi < 18.5) {
            msg = "Under Weight";
        } else if (bmi < 24.9) {
            msg =  "Normal Weight";
        } else if (bmi < 29.9) {
            msg = "Over Weight";
        } else if (bmi < 40) {
            msg = "Obese";
        } else {
            msg = "an inhuman";
            bmi=0.0;
        }

        String ttmsg_new;

        if (ttmsg.equals("Loose weight")){
            inCalories = tdee - 500;
            if(inCalories<bmr) inCalories=bmr;
            // To loose weight you should limit your intake to ~  calories
            ttmsg_new  = "체중을 줄이고 싶다면 " + inCalories + " 이하로 섭취해야 합니다.";
        }else if (ttmsg.equals("Gain weight")){
            inCalories = tdee + 500;
            // To gain weight you should increase your intake to ~ calories
            ttmsg_new = "체중을 늘리고 싶다면 " + inCalories + " 이상으로 섭취해야 합니다.";
        }else if (ttmsg.equals("Maintain weight")){
            inCalories=tdee;
            // To maintain weight your intake should be ~ calories
            ttmsg_new = "체중을 유지하고 싶다면 " + inCalories + " 정도로 유지하면 됩니다.";
        } else
            ttmsg_new = "이런 무언가 잘못됐어...";

        // populate results
        bmrValue.setText(""+bmr);
        tdeeValue.setText(""+tdee);
        bmiValue.setText(""+bmi);
        bmiDetail.setText(msg);
        tryingToView.setText(ttmsg_new);

        totCal = -inCalories;

        // save history 클릭 시
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                long millis=System.currentTimeMillis();
                java.sql.Date date=new java.sql.Date(millis);
                try {
                    FileOutputStream fileout=openFileOutput("history_data", MODE_APPEND);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                    outputWriter.write(date+",");
                    outputWriter.write(inCalories+",");
                    outputWriter.write(uWeight+",");
                    outputWriter.write(totCal+"\n");
                    outputWriter.close();


                    // SQLite 적용
                    String sDate = date.toString();
                    insertData(sDate, inCalories, uWeight, totCal);
                    
                    /*Toast.makeText(getBaseContext(), "성공적으로 저장되었습니다.",
                            Toast.LENGTH_SHORT).show();*/
                    logButton.setEnabled(false);
                    logButton.setAlpha(0.5f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
            );

        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Results.this,History.class);
                startActivity(i);

            }
        });
    }

    // DB 실습 관련 코드
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

    public void insertData(String date, int intake, double weight, int tot){
        if(database != null){
            String sql = "insert into userTable" + "(date, intake, weight, tot) values(?, ?, ?, ?)";
            Object[] params = {date, intake, weight, tot};
            database.execSQL(sql, params);//이런식으로 두번쨰 파라미터로 이런식으로 객체를 전달하면 sql문의 ?를 이 params에 있는 데이터를 물음표를 대체해준다.
            Toast.makeText(getApplicationContext(), "칼로리 계좌 생성!", Toast.LENGTH_LONG).show();
        }
    }
}
