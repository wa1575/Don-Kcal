package org.techtown.diet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
// 각종 운동 및 음식 더하는 액티비티
public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener, CategoryRecyclerAdapter.OnCategoryClickListener {

    private static String TAG = "AddRecordActivity";

    private EditText editText;
    private TextView amountText;
    private String userInput = "";

    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter adapter;

    private String category = "General";
    private RecordBean.RecordType type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
    private String remark = category;

    // ****usrid 여기 record에 넣어야 함! *****
    RecordBean record = new RecordBean();

    private boolean inEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        findViewById(R.id.keyboard_one).setOnClickListener(this);
        findViewById(R.id.keyboard_two).setOnClickListener(this);
        findViewById(R.id.keyboard_three).setOnClickListener(this);
        findViewById(R.id.keyboard_four).setOnClickListener(this);
        findViewById(R.id.keyboard_five).setOnClickListener(this);
        findViewById(R.id.keyboard_six).setOnClickListener(this);
        findViewById(R.id.keyboard_seven).setOnClickListener(this);
        findViewById(R.id.keyboard_eight).setOnClickListener(this);
        findViewById(R.id.keyboard_nine).setOnClickListener(this);
        findViewById(R.id.keyboard_zero).setOnClickListener(this);

        getSupportActionBar().setElevation(0);

        amountText = (TextView) findViewById(R.id.textView_amount);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(remark);

        handleBackspace();
        handleDone();
        handleDot();
        handleTypeChange();


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CategoryRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.notifyDataSetChanged();

        adapter.setOnCategoryClickListener(this);

        RecordBean record = (RecordBean) getIntent().getSerializableExtra("record");
        if (record != null) {
            inEdit = true;
            this.record = record;
        }
    }
    private void handleDot() {
        findViewById(R.id.keyboard_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.contains(".")) {
                    userInput += ".";
                }
            }
        });
    }

    private void handleTypeChange() {
        findViewById(R.id.keyboard_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = findViewById(R.id.keyboard_type);
                if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                    type = RecordBean.RecordType.RECORD_TYPE_INCOME;
                    button.setImageResource(R.drawable.baseline_local_fire_department_24);
                } else {
                    type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
                    button.setImageResource(R.drawable.baseline_local_dining_24);
                }
                adapter.changeType(type);
                category = adapter.getSelected();
            }
        });
    }

    private void handleBackspace() {
        findViewById(R.id.keyboard_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.length() > 0) {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                if (userInput.length() > 0 && userInput.charAt(userInput.length() - 1) == '.') {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                updateAmountText();
            }
        });
    }
    // 다른 테이블 갱신 코드 추가하기
    private void handleDone() {
        findViewById(R.id.keyboard_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.equals("")) {
                    double amount = Double.valueOf(userInput);
                    record.setAmount(amount);
                    if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                        record.setType(1);
                    } else {
                        record.setType(2);
                    }
                    record.setCategory(adapter.getSelected());
                    record.setRemark(editText.getText().toString());
                    // usrnum 인식시키기 하기 위한 인텐트
                    int num = getIntent().getIntExtra("usrnum", 0);
                    record.setNum(num);

                    if (inEdit) {
                        GlobalUtil.getInstance().databaseHelper.editRecord(record.getUuid(), record.getNum(),record);
                    } else {
                        GlobalUtil.getInstance().databaseHelper.addRecord(record);
                    }
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "금액을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String input = button.getText().toString();

        if (userInput.contains(".")) {
            if (userInput.split("\\.").length == 1 || userInput.split("\\.")[1].length() < 2) {
                userInput += input;
            }
        } else {
            userInput += input;
        }
        updateAmountText();

    }

    private void updateAmountText() {
        if (userInput.contains(".")) {
            if (userInput.split("\\.").length == 1) {
                amountText.setText(userInput + "00");
            } else if (userInput.split("\\.")[1].length() == 1) {
                amountText.setText(userInput + "0");
            } else if (userInput.split("\\.")[1].length() == 2) {
                amountText.setText(userInput);
            }
        } else {
            if (userInput.equals("")) {
                amountText.setText("0.00");
            } else {
                amountText.setText(userInput + ".00");
            }
        }
    }

    @Override
    public void onClikc(String category) {
        this.category = category;
        editText.setText(category);

        calorieLoad();
    }

    // 아이콘 클릭 시 해당하는 기준 칼로리가 입력됩니다.
    public void calorieLoad(){
        userInput = "";
        if(this.category.equals("Walk")){
            userInput += "180";
        }else if(this.category.equals("Jogging")){
            userInput += "300";
        }else if(this.category.equals("Run")){
            userInput += "630";
        }else if(this.category.equals("Bike")){
            userInput += "264";
        }else if(this.category.equals("Swim")){
            userInput += "720";
        }else if(this.category.equals("Lift")){
            userInput += "500";
        }else if(this.category.equals("Yoga")){
            userInput += "150";
        }else if(this.category.equals("Climb")){
            userInput += "300";
        }else if(this.category.equals("Tennis")){
            userInput += "432";
        }else if(this.category.equals("Golf")){
            userInput += "300";
        }else if(this.category.equals("Sing")){
            userInput += "100";
        }else if(this.category.equals("Salad")){
            userInput += "222";
        }else if(this.category.equals("Breast")){
            userInput += "165";
        }else if(this.category.equals("Fruit")){
            userInput += "52";
        }else if(this.category.equals("Bread")){
            userInput += "265";
        }else if(this.category.equals("Bacon")){
            userInput += "541";
        }else if(this.category.equals("Egg")){
            userInput += "155";
        }else if(this.category.equals("Cheese")){
            userInput += "402";
        }else if(this.category.equals("Fish")){
            userInput += "206";
        }else if(this.category.equals("Rice")){
            userInput += "300";
        }else if(this.category.equals("Boil")){
            userInput += "60";
        }else if(this.category.equals("Pizza")){
            userInput += "266";
        }else if(this.category.equals("Chicken")){
            userInput += "245";
        }else if(this.category.equals("Hamburger")){
            userInput += "490";
        }else if(this.category.equals("FrenchFries")){
            userInput += "379";
        }else if(this.category.equals("IceCream")){
            userInput += "267";
        }else if(this.category.equals("Coffee")){
            userInput += "4";
        }else if(this.category.equals("Juice")){
            userInput += "112";
        }
        updateAmountText();

    }
}