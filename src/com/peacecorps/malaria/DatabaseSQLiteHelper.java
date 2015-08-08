package com.peacecorps.malaria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
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
    private static final String appSettingTable = "appSettings";
    private static final String locationTable = "locationSettings";
    private static final String packingTable = "packingSettings";
    private static final String TAGDSH= "DatabaseSQLiteHelper";
    public static final String LOCATION = "Location";
    public static final String KEY_ROW_ID = "_id";

    private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31 };
    private final int[] daysOfMonthLeap = { 31, 29, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31 };

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + userMedicationChoiceTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, Drug INTEGER,Choice VARCHAR, Month VARCHAR, Year VARCHAR,Status VARCHAR,Date INTEGER,Percentage DOUBLE, Timestamp VARCHAR);");
        database.execSQL("CREATE TABLE " + appSettingTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, Drug VARCHAR, Choice VARCHAR, WeeklyDay INTEGER, FirstTime LONG, FreshInstall VARCHAR);");
        database.execSQL("CREATE TABLE " + locationTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, Location VARCHAR, Times INTEGER);");
        database.execSQL("CREATE TABLE " + packingTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, PackingItem VARCHAR, Quantity INTEGER);");
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
        int d=cal.get(Calendar.DATE);
        String ts="";
        if(d>=10)
        {
            ts=""+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DATE);
        }
        else {
            ts=""+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-0"+cal.get(Calendar.DATE);
        }
        String []args={""+ts};


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

    public void insertAppSettings(String drug, String choice, long date)
    {
        SQLiteDatabase sqDB= getWritableDatabase();
        String [] column={"FirstInstall"};
        ContentValues cv =new ContentValues(2);
        Cursor cursor=sqDB.query(appSettingTable,column,null,null,null,null,"_id ASC LIMIT 1");
        cursor.moveToNext();
        try {

            if (cursor.getString(0).compareTo("true")==0)
            {
                cv.put("Drug", drug);
                cv.put("Choice", choice);
                cv.put("FirstTime", date);
                Calendar c =Calendar.getInstance();
                c.setTimeInMillis(date);
                int w=c.get(Calendar.DAY_OF_WEEK);
                cv.put("WeeklyDay",w);
                cv.put("FreshInstall","true");
                String [] args={"1"};
                sqDB.delete(appSettingTable,"_id = ?",args);
                sqDB.insert(appSettingTable,"settings",cv);
            }

        }
        catch(Exception e)
        {   cv.put("Drug", drug);
            cv.put("Choice", choice);
            cv.put("FirstTime", date);
            Calendar c =Calendar.getInstance();
            c.setTimeInMillis(date);
            int w=c.get(Calendar.DAY_OF_WEEK);
            cv.put("WeeklyDay", w);
            cv.put("FreshInstall", "true");
            String [] args={"1"};
            sqDB.insert(appSettingTable,"settings",cv);
        }
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
        String Choice="",ts="";
        if(SharedPreferenceStore.mPrefsStore.getBoolean("com.peacecorps.malaria.isWeekly",false))
            Choice="weekly";
        else
            Choice="daily";
        if(date>=10)
            ts=""+year+"-"+month+"-"+date;
        else
            ts=""+year+"-"+month+"-0"+date;

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
                   {   if((date+i)>=10)
                       ts=""+year+"-"+month+"-"+(date+i);
                       else
                       ts=""+year+"-"+month+"-0"+(date+i);
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

    public long getFirstTime() {
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



    public int getDosesInaRowDaily()
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        String []column={"Status","Timestamp","Date","Month","Year","Choice"};
        Cursor cursor= sqDB.query(userMedicationChoiceTable,column,null,null,null,null,"Timestamp DESC");
        int dosesInaRow=0,prevDate=0,currDate=0,currDateMonth=0,prevDateMonth=0,prevDateYear=0,currDateYear=0;
        String ts="";
        if(cursor!=null) {
            cursor.moveToNext();
            if (cursor != null) {
                try {
                    ts = cursor.getString(cursor.getColumnIndex("Timestamp"));
                    currDate = cursor.getInt(2);
                    Log.d(TAGDSH, "curr date 1->" + ts);
                } catch (Exception e) {
                    return 0;
                }
                if (cursor.getString(0).compareTo("yes") == 0) {
                    prevDate = cursor.getInt(2);
                    prevDateMonth = cursor.getInt(3);
                    if (Math.abs(currDate - prevDate) <= 1)
                        dosesInaRow++;
                }


                while (cursor != null && cursor.moveToNext()) {
                    currDate = cursor.getInt(2);
                    currDateMonth = cursor.getInt(3);
                    currDateYear = cursor.getInt(4);
                    ts = cursor.getString(cursor.getColumnIndex("Timestamp"));
                    Log.d(TAGDSH, "curr date ->" + ts);
                    int parameter = Math.abs(currDate - prevDate);
                    if ((cursor.getString(0)) != null) {
                        if (currDateMonth == prevDateMonth) {
                            if (cursor.getString(0).compareTo("yes") == 0 && parameter == 1)
                                dosesInaRow++;
                            else
                                break;
                        } else {
                            parameter = Math.abs(currDate - prevDate) % (getNumberofDaysinMonth(currDateMonth, currDateYear) - 1);
                            if (cursor.getString(0).compareTo("yes") == 0 && parameter <= 1)
                                dosesInaRow++;
                            else
                                break;

                        }
                    }
                    Log.d(TAGDSH, "Doses in Row->" + dosesInaRow);
                    prevDate = currDate;
                    prevDateMonth = currDateMonth;
                }
            }
        }
        Log.d(TAGDSH, "Doses in Row->" + dosesInaRow);
        sqDB.close();
        return dosesInaRow;
    }

    private int getNumberofDaysinMonth(int month,int year)
    {
        if(isLeapYear(year))
        {
            return daysOfMonthLeap[month];
        }
        else
            return daysOfMonth[month];
    }

    private static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public int getDosesInaRowWeekly()
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        String []column={"Status","Timestamp","Date","Month","Year"};
        Cursor cursor= sqDB.query(userMedicationChoiceTable,column,null,null,null,null,"Timestamp DESC");
        int dosesInaRow=1,aMonth=0,pMonth=0;
        Date ado,pdo;
        int pPara=0;
        long aPara=0;
        int numDays=0;
        String ats="",pts="";
        if(cursor!=null) {
            cursor.moveToNext();
            if(cursor!=null) {

                try {
                    ats = cursor.getString(1);
                }
                catch (CursorIndexOutOfBoundsException e)
                {
                    return 0;
                }

                aMonth = cursor.getInt(3) + 1;
                ats = getHumanDateFormat(ats, aMonth);
                ado = getDateObject(ats);
                while (cursor.moveToNext()) {
                    pts = cursor.getString(1);
                    pMonth = cursor.getInt(3) + 1;
                    pts = getHumanDateFormat(pts, pMonth);
                    pdo = getDateObject(pts);
                    numDays = getDayofWeek(pdo);
                    pPara = 7 - numDays + 7;
                    aPara = getNumberOfDays(pdo, ado);
                    if (aPara <= pPara)
                        dosesInaRow++;
                    else
                        break;
                    ats = pts;
                    ado = pdo;
                }
            }
        }
        return dosesInaRow;


    }

    private Date getDateObject(String s)
    {
        Date dobj=null;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            dobj= sdf.parse(s);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dobj;
    }

    private int getDayofWeek(Date d)
    {
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        int day=cal.get(Calendar.DAY_OF_WEEK);
        return day;
    }

    private long getNumberOfDays(Date d1,Date d2) {
        long interval = 0;
        Calendar c= Calendar.getInstance();
        c.setTime(d1);
        long ld1 = c.getTimeInMillis();
        c.setTime(d2);
        long ld2=c.getTimeInMillis();
        long oneDay = 1000 * 60 * 60 * 24;
        interval = (ld2-ld1) / oneDay;
        return interval;
    }

    private String getHumanDateFormat(String ats,int aMonth)
    {
        String aYear=ats.substring(0, 4);
        String aDate=ats.substring(Math.max(ats.length() - 2, 0));
        ats=aYear+"-"+aMonth+"-"+aDate;
        return ats;
    }

    public String getMediLastTakenTime()
    {
        SQLiteDatabase sqDB=getWritableDatabase();
        String [] column={"Date","Month","Year"};
        String recentDate="";
        Cursor cursor = sqDB.query(userMedicationChoiceTable, column, null, null, null, null, "Timestamp DESC LIMIT 1");
        if(cursor!=null)
        { cursor.moveToNext();
            try
            {
                recentDate=cursor.getString(0)+"/"+cursor.getString(1);
            }
            catch (Exception e)
            {
                return "";
            }
        }      sqDB.close();
        return recentDate;
    }

    public void resetDatabase()
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        sqDB.delete(userMedicationChoiceTable,null,null);
        sqDB.delete(appSettingTable,null,null);
        sqDB.delete(locationTable,null,null);
        sqDB.delete(packingTable,null,null);
        sqDB.close();
    }

    public void insertLocation(String location)
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        ContentValues cv =new ContentValues(2);
        int a=0;
        cv.put("Location", location);

        String [] columns = {"Location","Times"};
        String [] selArgs = {""+location};

        Cursor cursor = sqDB.query(locationTable,columns,"Location = ?",selArgs,null,null,null);

        while (cursor.moveToNext())
        {
            a= cursor.getInt(1);
            a++;
        }
        cv.put("Times", a);

        if(a==1)
            sqDB.update(locationTable, cv, "Location= ?", selArgs);
        else
            sqDB.insert(locationTable, "location", cv);


    }

    public Cursor getLocation()
    {

        SQLiteDatabase sqDB = getWritableDatabase();
        String []column={"_id","Location"};

        return sqDB.query(locationTable, column,
                null, null, null, null,
                KEY_ROW_ID + " asc ");
    }

    public void insertPackingItem(String pItem,int quantity)
    {
        SQLiteDatabase sqDB = getWritableDatabase();
        ContentValues cv =new ContentValues(2);
        int flag=0,q=0;
        cv.put("PackingItem",pItem);

        String [] columns = {"PackingItem","Quantity"};
        String [] selArgs = {""+pItem};

        Cursor cursor = sqDB.query(packingTable,columns,"PackingItem = ?",selArgs,null,null,null);

        while (cursor.moveToNext())
        {
           q= cursor.getInt(1);
            flag++;
            Log.d(TAGDSH,"Flag: "+flag);
        }


        if(flag==1) {

            cv.put("Quantity", quantity);
            sqDB.update(packingTable, cv, "PackingItem= ?", selArgs);
        }
        else {
            cv.put("Quantity", quantity);
            sqDB.insert(packingTable, "item", cv);
        }


    }

    public Cursor getPackingItem()
    {

        SQLiteDatabase sqDB = getWritableDatabase();
        String []column={"_id","PackingItem","Quantity"};

        return sqDB.query(packingTable, column,
                null, null, null, null,
                KEY_ROW_ID + " asc ");
    }


}