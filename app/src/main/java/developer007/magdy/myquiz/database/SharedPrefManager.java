package developer007.magdy.myquiz.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    //authentication
    public static SharedPreferences getAuthPref(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences("Authentication", Context.MODE_PRIVATE);

        return pref;
    }

    //authentication
    public static void setPrefVal(Context mContext, String key, String value) {
        if (key != null) {
            SharedPreferences.Editor authEdit = getAuthPref(mContext).edit();
            authEdit.putString(key, value);
            authEdit.apply();
        }
    }

    /*
       key = "id";
                     SharedPrefManager.setPrefVal(Login.this, key, "3");
                     SharedPrefManager.setPrefVal(Login.this, userName, userName);

     */

    /*
    String saveScoreKey = userName + total;
        sharedPrefManager.setPrefVal(StudentActivity.this, saveScoreKey, "" + percent);
     */
}
