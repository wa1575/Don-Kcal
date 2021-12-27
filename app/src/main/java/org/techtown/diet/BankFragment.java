package org.techtown.diet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.ParcelableSparseArray;

import java.io.Serializable;
import java.util.LinkedList;
// 칼로리 계좌 플래그먼트
public class BankFragment extends Fragment implements AdapterView.OnItemLongClickListener, Serializable {

    private View rootView;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private LinkedList<RecordBean> records = new LinkedList<>();
    private String date = "";
    private String usrid = "";

    public BankFragment() {
    };

    @SuppressLint("ValidFragment")
    public BankFragment(String usrid, String date) {
        this.date = date;
        this.usrid = usrid;
        records = GlobalUtil.getInstance().databaseHelper.readRecords(usrid, date);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bank,container,false);
        initView();
        return rootView;
    }

    public void reload(){

        records = GlobalUtil.getInstance().databaseHelper.readRecords(usrid, date);
        if (listViewAdapter==null){
            listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext());
        }

        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount()>0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }

    private void initView(){
        textView = (TextView) rootView.findViewById(R.id.day_text);
        listView = (ListView) rootView.findViewById(R.id.listView);
        textView.setText(date);
        listViewAdapter = new ListViewAdapter(getContext());
        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount()>0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }

        textView.setText(DateUtil.getDateTitle(date));

        listView.setOnItemLongClickListener(this);
    }

    public int getTotalCost(int intake){
        double totalCost = intake;

        for (RecordBean record: records){
            if (record.getType()==1){
                totalCost -= record.getAmount();
            }
            else{
                totalCost += record.getAmount();
            }
        }
        return (int)totalCost;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(position);
        return false;
    }

    @Nullable
    private void showDialog(int index){
        final RecordBean selectedRecord = records.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("삭제").setMessage("정말로 삭제하시겠습니까?");
        builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                String uuid = selectedRecord.getUuid();
                GlobalUtil.getInstance().databaseHelper.removeRecord(uuid);
                reload();
                GlobalUtil.getInstance().bankActivity.updateHeader();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
}