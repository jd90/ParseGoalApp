package com.parse.starter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Borris on 22/04/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public DatabaseHelper(Context context) {
        super(context, "goalSharkDB", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating tables methods
        db.execSQL("CREATE TABLE IF NOT EXISTS profilesTbl (profileName VARCHAR, refreshDay INT(3))");
        db.execSQL("CREATE TABLE IF NOT EXISTS goalsTbl (profileName VARCHAR, goalName VARCHAR, total INT(3), done INT(3)," +
                "b0 INT(1),b1 INT(1),b2 INT(1),b3 INT(1),b4 INT(1),b5 INT(1),b6 INT(1)," +
                "bt0 INT(1),bt1 INT(1),bt2 INT(1),bt3 INT(1),bt4 INT(1),bt5 INT(1),bt6 INT(1)," +
                "percent INT(3), type INT(1))");
        db.execSQL("CREATE TABLE IF NOT EXISTS futureGoalsTbl (profileName, goalName, total, type)");
        db.execSQL("CREATE TABLE IF NOT EXISTS pastTotalsTbl (profileName VARCHAR, percent INT(3), date VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS profilesTbl");
        db.execSQL("DROP TABLE IF EXISTS goalsTbl");
        db.execSQL("DROP TABLE IF EXISTS pastTotalsTbl");
        db.execSQL("DROP TABLE IF EXISTS futureGoalsTbl");
        // create new tables
        onCreate(db);
    }

    /*
    * ---- insert row methods -----
    */


    public void insertProfile(ClassProfile profile) {

        Log.i("44331", "insert prof"+profile.getName());

        SQLiteDatabase db = this.getWritableDatabase();//not just have a db that i access and hold at a class level??

        //ContentValues values = new ContentValues();
        //values.put("profileName", profile.name);
        //values.put("refreshDay", 366);
        //db.insert("profilesTbl", null, values);

        db.execSQL("INSERT INTO profilesTbl (profileName, refreshDay) VALUES ('" + profile.getName() + "', " + profile.getRefreshDay() + ")");
    }

    public void insertGoal(ClassGoal goal) {


        Log.i("44331", "insert goal"+goal.getName());

        Log.i("44331 dbh ", ""+goal.profileName+"','"+goal.getName()+"',"+goal.getTotal()+","+goal.getDone()+"" +
                ","+goal.getButton(0)+","+goal.getButton(1)+","+goal.getButton(2)+","+goal.getButton(3)+","+goal.getButton(4)+","+goal.getButton(5)+","+goal.getButton(6)+
                ","+goal.buttonsThrough[0]+","+goal.buttonsThrough[1]+","+goal.buttonsThrough[2]+","+goal.buttonsThrough[3]+","+goal.buttonsThrough[4]+","+goal.buttonsThrough[5]+","+goal.buttonsThrough[6]+
                ","+goal.percent+
                ")");
        SQLiteDatabase db = this.getWritableDatabase();
        int type;
        if(goal.type){type=1;}else{type=0;}
        db.execSQL("INSERT INTO goalsTbl (profileName,goalName,total,done,b0,b1,b2,b3,b4,b5,b6,bt0,bt1,bt2,bt3,bt4,bt5,bt6,percent,type) " +
                "VALUES ('"+goal.profileName+"','"+goal.getName()+"',"+goal.getTotal()+","+goal.getDone()+"" +
            ","+goal.getButton(0)+","+goal.getButton(1)+","+goal.getButton(2)+","+goal.getButton(3)+","+goal.getButton(4)+
                ","+goal.getButton(5)+","+goal.getButton(6)+","+goal.buttonsThrough[0]+","+goal.buttonsThrough[1]+","
                +goal.buttonsThrough[2]+","+goal.buttonsThrough[3]+","+goal.buttonsThrough[4]+","+goal.buttonsThrough[5]+
                ","+goal.buttonsThrough[6]+ ","+goal.percent+","+type+
                ")");



        Log.i("44331", "ZZZZZ" +this.getAllGoals().size() );
    }

    public void insertFutureGoal(String profileName, String goalName, int total, int type){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO futureGoalsTbl (profileName, goalName, total, type) " +
                "VALUES (" +
                "'" + profileName + "'" +
                ",'" + goalName + "'" +
                "," + total +
                "," + type +
                ")");

    }

    public void insertPastTotal(String profileName, ClassArchiveItem archiveItem){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO pastTotalsTbl (profileName,percent,date) " +
                    "VALUES (" +
                "'" +profileName+
                "'," +archiveItem.percent+
                ",'" +archiveItem.date+
                "')");

    }

    public void updatePastTotals(String profileName, ArrayList<ClassArchiveItem> list){

        SQLiteDatabase db = this.getWritableDatabase();
        clearPastTotalsTbl(profileName);
        for(int i=0; i<list.size(); i++) {
            ClassArchiveItem archiveItem = list.get(i);
            db.execSQL("INSERT INTO pastTotalsTbl (profileName,percent,date) " +
                    "VALUES (" +
                    "'" + profileName +
                    "'," + archiveItem.percent +
                    ",'" + archiveItem.date +
                    "')");
        }
    }


    /*
    * ---- get list methods -----
    */

    public ArrayList<ClassProfile> getProfiles() {
        ArrayList<ClassProfile> profilesList = new ArrayList<ClassProfile>();
        String selectQuery = "SELECT  * FROM profilesTbl";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ClassProfile profile = new ClassProfile("", 777);
                profile.renameProfile(c.getString(c.getColumnIndex("profileName")));
                profile.setRefreshDay(c.getInt(c.getColumnIndex("refreshDay")));

                // adding to list
                profilesList.add(profile);
            } while (c.moveToNext());
        }

        return profilesList;
    }

    public ArrayList<ClassGoal> getGoals(String profileName) {
        ArrayList<ClassGoal> goalsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM goalsTbl WHERE profileName LIKE '"+profileName+"'";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ClassGoal goal = new ClassGoal("",999);
                goal.profileName = profileName;
                goal.setName((c.getString(c.getColumnIndex("goalName"))));
                goal.setTotal((c.getInt(c.getColumnIndex("total"))));
                goal.setDone((c.getInt(c.getColumnIndex("done"))));
                int type =(c.getInt(c.getColumnIndex("type")));
                if(type == 1){goal.type = true;}else{goal.type=false;}
                goal.setButton(0, (c.getInt(c.getColumnIndex("b0"))));
                goal.setButton(1, (c.getInt(c.getColumnIndex("b1"))));
                goal.setButton(2, (c.getInt(c.getColumnIndex("b2"))));
                goal.setButton(3, (c.getInt(c.getColumnIndex("b3"))));
                goal.setButton(4, (c.getInt(c.getColumnIndex("b4"))));
                goal.setButton(5, (c.getInt(c.getColumnIndex("b5"))));
                goal.setButton(6, (c.getInt(c.getColumnIndex("b6"))));
                goal.buttonsThrough[0] = (c.getInt(c.getColumnIndex("bt0")));
                goal.buttonsThrough[1] = (c.getInt(c.getColumnIndex("bt1")));
                goal.buttonsThrough[2] = (c.getInt(c.getColumnIndex("bt2")));
                goal.buttonsThrough[3] = (c.getInt(c.getColumnIndex("bt3")));
                goal.buttonsThrough[4] = (c.getInt(c.getColumnIndex("bt4")));
                goal.buttonsThrough[5] = (c.getInt(c.getColumnIndex("bt5")));
                goal.buttonsThrough[6] = (c.getInt(c.getColumnIndex("bt6")));



                // adding to list
                goalsList.add(goal);
            } while (c.moveToNext());
        }

        return goalsList;
    }

    public ArrayList<ClassGoal> getFutureGoals(String profileName) {
        Log.i("44331 nm1", profileName);
        ArrayList<ClassGoal> futureGoalsList = new ArrayList<ClassGoal>();
        String selectQuery = "SELECT  * FROM futureGoalsTbl WHERE profileName LIKE '"+profileName+"'";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ClassGoal futureGoal = new ClassGoal("",999);
                Log.i("44331 nm13", profileName);
                futureGoal.profileName = profileName;
                futureGoal.setName((c.getString(c.getColumnIndex("goalName"))));
                futureGoal.setTotal((c.getInt(c.getColumnIndex("total"))));
                int type =(c.getInt(c.getColumnIndex("type")));
                if(type == 1){futureGoal.type = true;}else{futureGoal.type=false;}

                // adding to list
                futureGoalsList.add(futureGoal);
            } while (c.moveToNext());
        }

        return futureGoalsList;
    }

    public ArrayList<ClassArchiveItem> getPastTotals(String profileName) {
        ArrayList<ClassArchiveItem> pastTotals = new ArrayList<ClassArchiveItem>();
        String selectQuery = "SELECT  * FROM pastTotalsTbl WHERE profileName LIKE '"+profileName+"'";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                pastTotals.add(new ClassArchiveItem(c.getInt(c.getColumnIndex("percent")), c.getString(c.getColumnIndex("date"))));
                //needs to take date too - model past totals as an object?


            } while (c.moveToNext());
        }

        return pastTotals;
    }

    public ArrayList<ClassProfile> getAllProfiles() {
        ArrayList<ClassProfile> profilesList = new ArrayList<ClassProfile>();
        String selectQuery = "SELECT  * FROM profilesTbl";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ClassProfile profile = new ClassProfile("", 777);
                profile.renameProfile(c.getString(c.getColumnIndex("profileName")));
                profile.setRefreshDay(c.getInt(c.getColumnIndex("refreshDay")));

                // adding to list
                profilesList.add(profile);
            } while (c.moveToNext());
        }

        return profilesList;
    }

    public ArrayList<ClassGoal> getAllGoals() {
        ArrayList<ClassGoal> goalsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM goalsTbl";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ClassGoal goal = new ClassGoal("",999);
                goal.profileName = (c.getString(c.getColumnIndex("profileName")));
                goal.setName((c.getString(c.getColumnIndex("goalName"))));
                goal.setTotal((c.getInt(c.getColumnIndex("total"))));
                goal.setDone((c.getInt(c.getColumnIndex("done"))));
                int type =(c.getInt(c.getColumnIndex("type")));
                if(type == 1){goal.type = true;}else{goal.type=false;}
                goal.setButton(0, (c.getInt(c.getColumnIndex("b0"))));
                goal.setButton(1, (c.getInt(c.getColumnIndex("b1"))));
                goal.setButton(2, (c.getInt(c.getColumnIndex("b2"))));
                goal.setButton(3, (c.getInt(c.getColumnIndex("b3"))));
                goal.setButton(4, (c.getInt(c.getColumnIndex("b4"))));
                goal.setButton(5, (c.getInt(c.getColumnIndex("b5"))));
                goal.setButton(6, (c.getInt(c.getColumnIndex("b6"))));
                goal.buttonsThrough[0] = (c.getInt(c.getColumnIndex("bt0")));
                goal.buttonsThrough[1] = (c.getInt(c.getColumnIndex("bt1")));
                goal.buttonsThrough[2] = (c.getInt(c.getColumnIndex("bt2")));
                goal.buttonsThrough[3] = (c.getInt(c.getColumnIndex("bt3")));
                goal.buttonsThrough[4] = (c.getInt(c.getColumnIndex("bt4")));
                goal.buttonsThrough[5] = (c.getInt(c.getColumnIndex("bt5")));
                goal.buttonsThrough[6] = (c.getInt(c.getColumnIndex("bt6")));



                // adding to list
                goalsList.add(goal);
            } while (c.moveToNext());
        }

        return goalsList;
    }

    public ArrayList<ClassGoal> getAllFutureGoals() {

        ArrayList<ClassGoal> futureGoalsList = new ArrayList<ClassGoal>();
        String selectQuery = "SELECT  * FROM futureGoalsTbl";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ClassGoal futureGoal = new ClassGoal("",999);

                futureGoal.profileName = (c.getString(c.getColumnIndex("profileName")));
                futureGoal.setName((c.getString(c.getColumnIndex("goalName"))));
                futureGoal.setTotal((c.getInt(c.getColumnIndex("total"))));
                int type =(c.getInt(c.getColumnIndex("type")));
                if(type == 1){futureGoal.type = true;}else{futureGoal.type=false;}

                // adding to list
                futureGoalsList.add(futureGoal);
            } while (c.moveToNext());
        }

        return futureGoalsList;
    }

    public ArrayList<ClassArchiveItem> getAllPastTotals() {
        ArrayList<ClassArchiveItem> pastTotals = new ArrayList<ClassArchiveItem>();
        String selectQuery = "SELECT  * FROM pastTotalsTbl";

        Log.e("3311", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                pastTotals.add(new ClassArchiveItem(c.getString(c.getColumnIndex("profileName")), c.getInt(c.getColumnIndex("percent")), c.getString(c.getColumnIndex("date"))));
                //needs to take date too - model past totals as an object?


            } while (c.moveToNext());
        }

        return pastTotals;
    }
    /*
    * ---- update/replace row methods -----
    */

    public void updateProfileRow(String oldProfileName, String newProfileName, int refreshDay) {//should possibly take a profileObject instead - consider when in context
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE profilesTbl " +
                " SET profileName = '"+newProfileName+"',refreshDay = "+refreshDay+
                " WHERE profileName LIKE '"+oldProfileName+"'"
                );
        //could split into a profileName change and a refreshDay change method
    }

    public void updateGoalRow(ClassGoal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("44331", "g "+ goal.getButton(0));
                 db.execSQL("UPDATE goalsTbl " +
                         "SET done = " + goal.getDone() + ", percent = " + goal.percent +
                         ", b0 = " + goal.getButton(0) +
                         ", b1 = " + goal.getButton(1) +
                         ", b2 = " + goal.getButton(2) +
                         ", b3 = " + goal.getButton(3) +
                         ", b4 = " + goal.getButton(4) +
                         ", b5 = " + goal.getButton(5) +
                         ", b6 = " + goal.getButton(6) +
                         ", bt0 = " + goal.buttonsThrough[0] +
                         ", bt1 = " + goal.buttonsThrough[1] +
                         ", bt2 = " + goal.buttonsThrough[2] +
                         ", bt3 = " + goal.buttonsThrough[3] +
                         ", bt4 = " + goal.buttonsThrough[4] +
                         ", bt5 = " + goal.buttonsThrough[5] +
                         ", bt6 = " + goal.buttonsThrough[6] +
                         " WHERE profileName LIKE '" + goal.profileName + "'" +
                         " AND goalName LIKE '" + goal.getName() +
                         "'");

        Log.i("44331 prof", goal.profileName);
        Log.i("44331 prof", goal.getName());
        //could split into a profileName change and a refreshDay change method

    }


    /*
    * ---- method to delete a particular object - method to clear all too/parse style ones -----
    */

    public void deleteProfileRow(String profileName){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("44331", "DELETE FROM profilesTbl " +
                "WHERE profileName LIKE '"+profileName+"'");
        db.execSQL("DELETE FROM profilesTbl " +
                "WHERE profileName LIKE '" + profileName + "'");
        db.execSQL("DELETE FROM goalsTbl " +
                "WHERE profileName LIKE '" + profileName + "'");
        db.execSQL("DELETE FROM futureGoalsTbl " +
                "WHERE profileName LIKE '"+profileName+"'");
        db.execSQL("DELETE FROM pastTotalsTbl " +
                "WHERE profileName LIKE '"+profileName+"'");

    }


    public void renameProfileThroughout(String oldProfileName, String newProfileName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE goalsTbl " +
                "SET profileName = '" + newProfileName +
                "' WHERE profileName LIKE '" + oldProfileName +
                "'");
        db.execSQL("UPDATE profilesTbl " +
                "SET profileName = '" + newProfileName +
                "' WHERE profileName LIKE '" + oldProfileName +
                "'");
        db.execSQL("UPDATE futureGoalsTbl " +
                "SET profileName = '" + newProfileName +
                "' WHERE profileName LIKE '" + oldProfileName +
                "'");
        db.execSQL("UPDATE pastTotalsTbl " +
                "SET profileName = '" + newProfileName +
                "' WHERE profileName LIKE '" + oldProfileName +
                "'");

    }



    public void clearProfileTbl(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM profilesTbl");
    }
    public void clearGoalsTbl(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM goalsTbl");
    }
    public void clearGoalsTbl(String profileName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM goalsTbl WHERE profileName Like '"+profileName+"'");
    }
    public void clearFutureGoalsTbl(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM futureGoalsTbl");
    }
    public void clearFutureGoalsTbl(String profileName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM futureGoalsTbl WHERE profileName LIKE '"+profileName+"'");
    }
    public void clearPastTotalsTbl(){

        Log.i("44331", "pastots called");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM pastTotalsTbl");
    }
    public void clearPastTotalsTbl(String profileName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM pastTotalsTbl WHERE profileName Like '"+profileName+"'");
    }
    public void clearAllTbls(){

        clearProfileTbl();
        clearGoalsTbl();
        clearFutureGoalsTbl();
        clearPastTotalsTbl();

    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    /*
    * ---- count rows methods? unless the loaded datastore can deal with this itself -----
    */



    //it may be wise to have just one datastore - dont know why i'd have more than one?

    //i should model a goal and extend it to future and active goal objects

    //model past totals

    //look into methods and logic that needs changed/repositioned throughout the app - including new tables format
    // and new how datastore will be different/what it needs to hold and if it should be one single datastore?

    //add methods into goal object class, for instance, after all methods that change something to call the appropriate sql update method?
    // this can be done using the goal's name?? i think??

    //look at how parse stuff needs to be changed
    //make toJSON class to handle that




    //test that these methods work before doing way too much work on the rest of the app stuff??

    //make a 'update all goals matching profilename' method for reorder operation

    //make a 'remove all database rows from all tables method' and 'save all these values to all tables method' for parse loading down

    //make a 'close database' method to use after all methods are called from in context

}
