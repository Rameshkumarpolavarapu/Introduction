package com.example.ramesh.introduction;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by J.yugandhar on 15-06-2017.
 */

public class PrefManager {
    SharedPreferences  pref;
    SharedPreferences.Editor editor;
    Context _context;

    //Shared Preference mode
    int PRIVATE_MODE    =   0;

    //Shared Preference file Name

    private static final String PREF_NAME   =   "introduction_welcome_screen";

    private static final String     IS_FIRST_TIME_LAUNCH    =   "IsFirstTimeLaunch";

    public  PrefManager(Context context){
        this._context    =   context;
        pref        =   _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor      =   pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch(){

        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }

}
