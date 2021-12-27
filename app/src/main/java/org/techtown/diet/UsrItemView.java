package org.techtown.diet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UsrItemView extends RelativeLayout {
    TextView dateView;
    TextView intakeView;
    TextView weightView;
    TextView totView;

    public UsrItemView(Context context) {
        super(context);
        init(context);
    }

    public UsrItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.usr_item, this, true);

        dateView = (TextView) findViewById(R.id.date_heading);
        intakeView = (TextView) findViewById(R.id.intake_heading);
        weightView = (TextView) findViewById(R.id.weight_heading);
        totView = (TextView) findViewById(R.id.tot_heading);
    }

    public void setDate(String date) {
        dateView.setText(date);
    }

    public void setIntake(int intake) {
        intakeView.setText(String.valueOf(intake));
    }

    public void setWeight(double weight) {
        weightView.setText(String.valueOf(weight));
    }

    public void setTot(int tot) {
        totView.setText(String.valueOf(tot));
    }
}
