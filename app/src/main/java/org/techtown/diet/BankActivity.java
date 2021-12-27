package org.techtown.diet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
// 칼로리 계좌 액티비티
public class BankActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG ="BankActivity";

    private ViewPager viewPager;
    private BankViewPagerAdapter pagerAdapter;
    private TickerView amountText;
    private TextView dateText;
    private int currentPagerPosition = 0;

    History.UsrAdapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().bankActivity = this;
        getSupportActionBar().setElevation(0);

        int num;
        int tot;
        // history 에서 인텐트 변수 받아오기
        num = getIntent().getIntExtra("usrnum", 0);
        // 우선 String으로 변환
        String usrnum = String.valueOf(num);

        amountText = (TickerView)findViewById(R.id.amount_text);
        amountText.setCharacterLists(TickerUtils.provideNumberList());
        dateText=(TextView) findViewById(R.id.date_text);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new BankViewPagerAdapter(getSupportFragmentManager(), usrnum);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(pagerAdapter.getLatsIndex());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이너클래스라 추가함
                int num;

                num = getIntent().getIntExtra("usrnum", 0);
                Intent intent = new Intent(BankActivity.this,AddRecordActivity.class);
                // 화면 전환
                // 게시물의 번호와 userid를 가지고 AddActivity 로 이동
                intent.putExtra("usrnum", num);
                startActivityForResult(intent,1);
            }
        });
        updateHeader();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.reload();
        updateHeader();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int tot = getIntent().getIntExtra("tot", 0);
        Log.d(TAG,"cost: "+pagerAdapter.getTotalCost(position, tot));
        currentPagerPosition = position;
        updateHeader();
    }



    // 뒤로가기 버튼 누를 때, finish 작업 후 start해줘야 해서
    @Override
    public void onBackPressed() {
        //((History)History.CONTEXT).resumeTot();
        //((History)History.CONTEXT).onResume();
        Intent i = new Intent(BankActivity.this,History.class);
        startActivity(i);
        super.onBackPressed();
    }


    public void updateHeader(){
        int intake = getIntent().getIntExtra("intake", 0);
        String amount = String.valueOf(pagerAdapter.getTotalCost(currentPagerPosition, -intake));
        // 추가 -> 전체가 똑같이 업데이트되버림, 해당하는 id만 변경되게 수정시켜야 함
        // 완료! 이제 페이지 이동할 때마다 history view 자동으로 갱신시켜 야 함
        int num = getIntent().getIntExtra("usrnum", 0);
        GlobalUtil.getInstance().databaseHelper.updateTot(num, amount);
        // 작동안함...
        //((History)History.CONTEXT).resumeTot();
        //History.resumeTot();
        //listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        amountText.setText(amount);
        String date = pagerAdapter.getDateStr(currentPagerPosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}