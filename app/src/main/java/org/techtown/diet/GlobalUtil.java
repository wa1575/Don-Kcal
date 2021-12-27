package org.techtown.diet;

import android.content.Context;

import java.util.LinkedList;
// 아이콘에 대한 정보
public class GlobalUtil {
    private static final String TAG = "GlobalUtil";

    private static GlobalUtil instance;

    public RecordDatabaseHelper databaseHelper;
    private Context context;
    public BankActivity bankActivity;

    public LinkedList<CategoryResBean> costRes = new LinkedList<>();
    public LinkedList<CategoryResBean> earnRes = new LinkedList<>();

    private static int [] costIconRes = {
            R.drawable.icon_general_white,
            R.drawable.icon_walk_white,
            R.drawable.icon_jogging_white,
            R.drawable.icon_run_white,
            R.drawable.icon_bike_white,
            R.drawable.icon_swim_white,
            R.drawable.icon_lift_white,
            R.drawable.icon_yoga_white,
            R.drawable.icon_climb_white,
            R.drawable.icon_tennis_white,
            R.drawable.icon_golf_white,
            R.drawable.icon_sing_white
    };
    private static int [] costIconResBlack = {
            R.drawable.icon_general,
            R.drawable.icon_walk,
            R.drawable.icon_jogging,
            R.drawable.icon_run,
            R.drawable.icon_bike,
            R.drawable.icon_swim,
            R.drawable.icon_lift,
            R.drawable.icon_yoga,
            R.drawable.icon_climb,
            R.drawable.icon_tennis,
            R.drawable.icon_golf,
            R.drawable.icon_sing
    };
    private static String[] costTitle = {"General","Walk","Jogging", "Run", "Bike", "Swim", "Lift", "Yoga",
            "Climb", "Tennis", "Golf", "Sing"};

    private static int[] earnIconRes = {
            R.drawable.icon_general_white,
            R.drawable.icon_salad_white,
            R.drawable.icon_breast_white,
            R.drawable.icon_fruit_white,
            R.drawable.icon_bread_white,
            R.drawable.icon_bacon_white,
            R.drawable.icon_egg_white,
            R.drawable.icon_cheese_white,
            R.drawable.icon_fish_white,
            R.drawable.icon_rice_white,
            R.drawable.icon_boil_white,
            R.drawable.icon_pizza_white,
            R.drawable.icon_chicken_white,
            R.drawable.icon_hambuger_white,
            R.drawable.icon_frenchfries_white,
            R.drawable.icon_ice_white,
            R.drawable.icon_coffee_white,
            R.drawable.icon_drinking_white
            };


    private static int[] earnIconResBlack = {
            R.drawable.icon_general,
            R.drawable.icon_salad,
            R.drawable.icon_breast,
            R.drawable.icon_fruit,
            R.drawable.icon_bread,
            R.drawable.icon_bacon,
            R.drawable.icon_egg,
            R.drawable.icon_cheese,
            R.drawable.icon_fish,
            R.drawable.icon_rice,
            R.drawable.icon_boil,
            R.drawable.icon_pizza,
            R.drawable.icon_chicken,
            R.drawable.icon_hambuger,
            R.drawable.icon_frenchfries,
            R.drawable.icon_ice,
            R.drawable.icon_coffee,
            R.drawable.icon_drinking
    };

    private static String[] earnTitle = {"General", "Salad", "Breast", "Fruit",
            "Bread","Bacon","Egg","Cheese","Fish",
            "Rice", "Boil","Pizza"
            ,"Chicken","Hamburger","FrenchFries","IceCream","Coffee","Juice"
    };

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        databaseHelper = new RecordDatabaseHelper(context,RecordDatabaseHelper.DB_NAME,null,1);

        for (int i=0;i<costTitle.length;i++){
            CategoryResBean res = new CategoryResBean();
            res.title = costTitle[i];
            res.resBlack = costIconResBlack[i];
            res.resWhite = costIconRes[i];
            costRes.add(res);
        }

        for (int i=0;i<earnTitle.length;i++){
            CategoryResBean res = new CategoryResBean();
            res.title = earnTitle[i];
            res.resBlack = earnIconResBlack[i];
            res.resWhite = earnIconRes[i];
            earnRes.add(res);
        }

    }

    public static GlobalUtil getInstance(){
        if (instance==null){
            instance = new GlobalUtil();
        }
        return instance;
    }

    public int getResourceIcon(String category){
        for (CategoryResBean res :
                costRes) {
            if (res.title.equals(category)){
                return res.resWhite;
            }
        }

        for (CategoryResBean res :
                earnRes) {
            if (res.title.equals(category)){
                return res.resWhite;
            }
        }

        return costRes.get(0).resWhite;
    }
}
