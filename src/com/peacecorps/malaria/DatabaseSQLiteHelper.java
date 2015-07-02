package com.peacecorps.malaria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chimdi on 7/18/14.
 */
public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MalariaDatabase";
    private static final String userMedicationChoiceTable = "userSettings";
    private static final String TAGDSH= "DatabaseSQLiteHelper";

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + userMedicationChoiceTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, Drug INTEGER,Choice VARCHAR, Month VARCHAR, Year VARCHAR,Status VARCHAR,Date INTEGER,Percentage DOUBLE, Timestamp VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {


    }

    public static ArrayList<Double> percentage;
    public static ArrayList<Integer> date;

    public int getData(int month, int year, String choice) {
        //Log.d(TAGDSH, "INSIDE GET DATA DATE");
        percentage = new ArrayList<Double>();
        date = new ArrayList<Integer>();
        int count = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String column[] = {"_id", "Date", "Percentage"};
        String args[] = {"" + month, "" + year, "yes", choice};
        Cursor cursor = sqLiteDatabase.query(userMedicationChoiceTable, column, "Month =? AND Year =? AND Status =? AND Choice =?", args, null, null,"Date ASC");
        boolean isDataFound = false;
        while (cursor.moveToNext()) {
            isDataFound = true;
            count += 1;
            date.add(cursor.getInt(1));

            percentage.add(cursor.getDouble(2));
            Log.d(TAGDSH, "INSIDE GET DATA DATE: " + cursor.getInt(1));
            Log.d(TAGDSH,"INSIDE GET DATA PERCENTAGE:"+cursor.getDouble(2));

        }
        if (isDataFound) {
            if (!(date.get(date.size() - 1) == Calendar.getInstance().get(Calendar.DATE))) {
                percentage.add(0.0);
                date.add(Calendar.getInstance().get(Calendar.DATE));
            }
        }
        sqLiteDatabase.close();
        Log.d(TAGDSH, "" + count);
        return count;
    }

    public void getUserMedicationSelection(Context context, String choice, Date date, String status, Double percentage) {
        ContentValues values = new ContentValues(2);
        Calendar cal;
        cal = Calendar.getInstance();
        cal.setTime(date);
        String ts=""+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DATE);


        Log.d(TAGDSH,ts);
        values.put("Drug", SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.drug", 0));
        values.put("Choice", choice);
        values.put("Month", "" + cal.get(Calendar.MONTH));
        values.put("Year", "" + cal.get(Calendar.YEAR));
        values.put("Status", status);
        values.put("Date", cal.get(Calendar.DATE));
        values.put("Percentage", percentage);
        values.put("Timestamp",ts);
        this.getWritableDatabase().insert(userMedicationChoiceTable, "medication", values);
        this.getWritableDatabase().close();

    }

    public String getMedicationData(int date,int month, int year) {
        SQLiteDatabase sqDB = getReadableDatabase();
        String choice ="daily";
        String[] columns = {"_id", "Date", "Percentage", "Status"};
        String[] selArgs = {"" + month, "" + year, choice};
        //Log.d(TAGDSH, "INSIDE GET MEDICATION DATA");
        StringBuffer buffer = new StringBuffer();
        Cursor cursor = sqDB.query(userMedicationChoiceTable, columns, "Month =? AND Year =? AND Choice =?", selArgs, null, null, null, null);
        //Cursor cursor = sqDB.rawQuery("SELECT Date,Choice,Status FROM userSettings WHERE Month =? AND Year =?", selArgs);
        int idx0,idx1,idx2;
        //Log.d(TAGDSH, "MADE QUERY");

        while (cursor.moveToNext()) {
            //Log.d(TAGDSH,"INSIDE CURSOR");
             idx0 = cursor.getColumnIndex("Date");
             idx1 = cursor.getColumnIndex("Percentage");
             idx2 = cursor.getColumnIndex("Status");
            int d= cursor.getInt(idx0);
            String ch = cursor.getString(idx2);
            //String stat = cursor.getString(idx2);
            Log.d(TAGDSH,"Passed Date:"+date+"Found date:"+d);
            //Log.d(TAGDSH,"INSIDE WHILE OF CURSOR GET MEDICATION DATA");
            Log.d(TAGDSH,""+year);
            //Log.d(TAGDSH,"---"+d+ch+stat);
            if(d==date)
            {
                buffer.append(ch);
            }
        }
        sqDB.close();
        return buffer.toString();
    }

    public void updateMedicationEntry(int date, int month, int year, String entry,double percentage){

        SQLiteDatabase sqDB = getReadableDatabase();
        ContentValues values = new ContentValues(2);
        values.put("Status", entry);
        values.put("Percentage", percentage);
        String[] args = new String[]{String.valueOf(date), String.valueOf(month),String.valueOf(year)};
        String[] column = {"Percentage"};
        sqDB.update(userMedicationChoiceTable, values, "Date=? AND Month=? AND YEAR=?", args);
        Cursor cursor=sqDB.query(userMedicationChoiceTable,column,null,null,null,null,null);
        while(cursor.moveToNext())
        {
         Log.d(TAGDSH, "Percentage:" + cursor.getDouble(0));
        }
        sqDB.close();

    }

    /*Insert the database for the no entry */
    public void insertOrUpdateMissedMedicationEntry(int date, int month, int year,double percentage)
    {
        SQLiteDatabase sqDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues(2);
        String Choice="";
        if(SharedPreferenceStore.mPrefsStore.getBoolean("com.peacecorps.malaria.isWeekly",false))
            Choice="weekly";
        else
            Choice="daily";
        String ts=""+year+"-"+month+"-"+date;

        String []columns={"Status"};
        String []selArgs= {""+date,""+month,""+year};
        Cursor cursor = sqDB.query(userMedicationChoiceTable, columns, "Date=? AND Month =? AND Year =?", selArgs, null, null, null, null);
        int idx0; String st=""; int flag=0;
        while(cursor.moveToNext())
        {
            idx0=cursor.getColumnIndex("Status");
            st=cursor.getString(idx0);
            flag=1;
        }

        Log.d(TAGDSH, "" + year);
        if(flag==0 && st.equalsIgnoreCase(""))
        {
            cv.put("Drug", SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.drug", 0));
            cv.put("Choice", Choice);
            cv.put("Month", "" + month);
            cv.put("Year", "" + year);
            cv.put("Date", date);
            cv.put("Percentage", percentage);
            cv.put("Timestamp", ts);
            sqDB.insert(userMedicationChoiceTable, "medicaton", cv);

            String []col ={"Date"};
            String []arg = {""+month,""+year};
            Cursor crsr = sqDB.query(userMedicationChoiceTable,col,"Month =? AND Year =?",arg,null,null,"Date ASC");
            int count=1,p,lim,ft=0;
            while (cursor.moveToNext())
            {
                p = crsr.getInt(0);
                count++;
                if(count==1)
                {
                    ft=p;
                }
                if(count==2)
                {
                   lim=p-ft;
                   for (int i=1;i<lim;i++)
                   {   ts=""+year+"-"+month+"-"+(date+i);
                       cv.put("Drug", SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.drug", 0));
                       cv.put("Choice", Choice);
                       cv.put("Month", "" + month);
                       cv.put("Year", "" + year);
                       cv.put("Date", date+i);
                       cv.put("Percentage", percentage);
                       cv.put("Timestamp", ts);
                       sqDB.insert(userMedicationChoiceTable, "medicaton", cv);
                   }

                }

            }



        }
        sqDB.close();

    }

    /*Is Entered is Used for Getting the Style of Each Calendar Grid Cell According to the Medication*/
    public int isEntered(int date,int month, int year)
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        String column[] = {"Status"};
        String args[] = {""+date,"" + month, ""+year};
        Cursor cursor = sqDB.query(userMedicationChoiceTable, column, "Date=? AND Month =? AND Year =?", args, null, null, null, null);
        int idx=0;
        String status="";
        while(cursor.moveToNext())
        {
            idx = cursor.getColumnIndex("Status");
            status=cursor.getString(idx);
            if(status!=null) {
                if (status.equalsIgnoreCase("yes"))
                    return 0;
                else if (status.equalsIgnoreCase("no"))
                    return 1;
            }

        }
        sqDB.close();
        return 2;


    }

    public long getFirstTime()
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        String column[]={"Timestamp"};
        Cursor cursor = sqDB.query(userMedicationChoiceTable,column,null,null,null,null,"Timestamp ASC LIMIT 1");
        int idx=0; String selected_date=""; long firstRunTime=0;
        while (cursor.moveToNext())
        {
            idx=cursor.getColumnIndex("Timestamp");
            selected_date=cursor.getString(idx);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date comp_date=Calendar.getInstance().getTime();
            try {
                comp_date   = sdf.parse(selected_date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(comp_date);
            firstRunTime=cal.getTimeInMillis();
        }
        sqDB.close();
        return firstRunTime;
    }

    public String getStatus(int date,int month,int year){

        SQLiteDatabase sqDB = getWritableDatabase();
        String []column = {"Status"};
        String []selArgs = {""+date,""+month,""+year};
        Cursor cursor= sqDB.query(userMedicationChoiceTable,column,"Date =? AND Month =? AND Year =?",selArgs,null,null,null,null);

        while(cursor.moveToNext())
        {
            return cursor.getString(0);
        }

        return "miss";
    }

    public int getDosesInaRow()
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        String []column={"Status","Timestamp","Date"};
        Cursor cursor= sqDB.query(userMedicationChoiceTable,column,null,null,null,null,"Timestamp DESC");
        int dosesInaRow=0,prevDate=0,currDate=0;

        //prevDate=cursor.getInt(cursor.getColumnIndex("Date"));
        while(cursor!=null && cursor.moveToNext())
        {


                    if((cursor.getString(0))!=null)
                    {
                        if(cursor.getString(0).compareTo("yes")==0)
                            dosesInaRow++;
                        else
                            break;
                    }
            Log.d(TAGDSH, cursor.getString(1) + "-" + cursor.getString(0));
        }
        sqDB.close();
        return dosesInaRow;
    }

}