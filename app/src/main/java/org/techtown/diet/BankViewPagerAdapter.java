package org.techtown.diet;

import java.util.LinkedList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

// 칼로리 계좌 어댑터
public class BankViewPagerAdapter extends FragmentPagerAdapter {

    LinkedList<BankFragment> fragments = new LinkedList<>();

    LinkedList<String> dates = new LinkedList<>();
    String usrid = "";

    public BankViewPagerAdapter(FragmentManager fm, String usrid) {
        super(fm);
        initFragments(usrid);
    }
    // id 플래그먼트 추가
    private void initFragments(String usrid){
        this.usrid = usrid;
        dates = GlobalUtil.getInstance().databaseHelper.getAvaliableDate(this.usrid);

        if (!dates.contains(DateUtil.getFormattedDate())){
            dates.addLast(DateUtil.getFormattedDate());
        }

       //for(String usrid: usrids) {
           for (String date : dates) {
               BankFragment fragment = new BankFragment(this.usrid, date);
               fragments.add(fragment);
           }
       //}
    }

    public void reload(){
        for (BankFragment fragment :
                fragments) {
            fragment.reload();
        }
    }

    public int getLatsIndex(){
        return fragments.size()-1;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public String getDateStr(int index){
        return dates.get(index);
    }

    public int getTotalCost(int index, int intake){
        return fragments.get(index).getTotalCost(intake);
    }
}
