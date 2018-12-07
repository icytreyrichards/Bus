package localdata;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import utility.Utility;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

public class Data extends Activity {
	public static String server = "http://safari.co.ug/eservices/";
	//public static String server = "http://10.0.2.117:81/easybus/";
	//public static String server = "http://192.168.42.238:81/easybus/";
	public static Boolean is_Record(Context context,String sql) {
		Boolean result = false;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery(sql, null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = true;

			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String User_Id(Context context) {
		String result = "";
		try {

			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from users", null);
			int row = cur.getCount();
			if (row > 0) {
				while (cur.moveToNext()) {
					result = cur.getString(3);
				}
			}
			cur.close();
			db.close();
		}
		catch (NullPointerException er) {

		}
		return result;
	}
	public static String getLocalProfile_picture(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from photos", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(1);
			}
		}
		cur.close();
		db.close();
		return result;
	}


	public static String local(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from local", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}

	


	public static Boolean isprofilepic(Context context) {
		Boolean result = false;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from profile_pic", null);
		int row = cur.getCount();

		if (row > 0) {
			while (cur.moveToNext()) {
				result = true;

			}
		}
		cur.close();
		db.close();
		return result;
	}

	public static Boolean is_LocalRecord(Context context) {
		Boolean result = false;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = true;
			}
		}
		cur.close();
		db.close();
		return result;
	}

	public static Boolean iscoverpage(Context context) {
		Boolean result = false;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from coverpage", null);
		int row = cur.getCount();

		if (row > 0) {
			while (cur.moveToNext()) {
				result = true;

			}
		}
		cur.close();
		db.close();
		return result;
	}

	public static ArrayList<String> Profile(Context context) {
		ArrayList<String> result = new ArrayList<String>();
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();
		Log.e("biography", "records" + row);
		if (row > 0) {
			while (cur.moveToNext()) {

				result.add(cur.getString(0));
				result.add(cur.getString(1));
				result.add(cur.getString(2));
			}
		}
		cur.close();
		db.close();
		return result;
	}

	public static String UserID(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(3);

			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String role(Context context) {
		String result = null;
		result=getEnterpriseRole(context);
		/*
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();

		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(3).trim();


			}
		}
		cur.close();
		db.close();
		*/
		return result;
	}


	public static String photo(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(5);

			}
		}
		cur.close();
		db.close();
		Log.e("local etrieved path", result);
		return result;
	}

	public static String telephone(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();

		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(1);

			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String fullname(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();

		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}

	public static String email(Context context) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from users", null);
		int row = cur.getCount();

		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(2);
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String UpdateProfilePicture(Context context,String path) {
		String result = null;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getWritableDatabase();
		db.execSQL("update users set photo='"+path+"'");
		return result;
	}
	public static String lostdata(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from getschedule", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}

	public static String notificationsdata(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from notifications", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String friendsdata(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from friends", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String regstring(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from gcm", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static void SaveData(Context context,String data,String table) {
		int result = 0;
		try {
			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getWritableDatabase();
			db.execSQL("delete from " + table);
		/*
			ContentValues values = new ContentValues();
			values.put("data", data);
			//String where = "filename=?";
			//String[] whereArgs = new String[] {String.valueOf(filename)};
			db.update(table,values, where null, /*whereArgs null);
		*/
				ContentValues values = new ContentValues();
				values.put("data", data);
				db.insert(table, null, values);
				result = 1;

			} catch (Exception er) {

			}


	}

	public static void LocalRegistryreg(Context context,String fullname, String telephone,String email,
								 String user_id) {
		int result = 0;
		try {
			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("fullname", fullname);
			values.put("telephone", telephone);
			values.put("email", email);
			values.put("user_id", user_id);;
			db.insert("users", null, values);
			result = 1;

		} catch (Exception er) {

		}

	}
	public static void UpdateUsers(Context context,String fullname, String telephone, String email) {
		int result = 0;
		try {
			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getWritableDatabase();
			db.execSQL("update users set fullname='"+fullname+"',telephone='"+telephone+"',email='"+email+"'");
			result = 1;

		} catch (Exception er) {

		}

	}
	public static void SaveSchedules(Context context,String data, String source, String destination) {
		int result = 0;
		try {
			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getWritableDatabase();
			if(!is_Record(context,"select * from schedules"))
			{
				db.execSQL("insert into schedules (data,source,destination) values ('" + data + "','" + source + "','" + destination + "')");
			}
			else {
				db.execSQL("update schedules set data='" + data + "'");
			}

			result = 1;

		} catch (Exception er) {

		}

	}
	public static String getLocalRoutes(Context context) {
		String result = "";
		try {

			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from routes", null);
			int row = cur.getCount();
			if (row > 0) {
				while (cur.moveToNext()) {
					result = cur.getString(0);
				}
			}
			cur.close();
			db.close();
		}
		catch (NullPointerException er) {

		}
		return result;
	}
	public static String getSchedules(Context context,String source,String destination) {
		String result = "";
		try {

			DBhelper help = new DBhelper(context);
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from schedules", null);
			int row = cur.getCount();
			if (row > 0) {
				while (cur.moveToNext()) {
					result = cur.getString(0);
				}
			}
			cur.close();
			db.close();
		}
		catch (NullPointerException er) {

		}
		return result;
	}
	public static String getData(Context ctx,String table) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from "+table, null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String getEnterpriseID(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from enterprise", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
				try {
					return  LogicClass.JARRAY(result).getJSONObject(0).getString("UserID");
				}
				catch (Exception er)
				{

				}
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String getEnterpriseRole(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from enterprise", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
				try {
					return  LogicClass.JARRAY(result).getJSONObject(0).getString("RoleID");
				}
				catch (Exception er)
				{

				}
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String getEnterpriseCompanyID(Context ctx) {

		String result = "0";
		try {
			DBhelper help = new DBhelper(ctx);
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from enterprise", null);
			int row = cur.getCount();
			if (row > 0) {
				while (cur.moveToNext()) {
					result = cur.getString(0);
					try {
						result = LogicClass.JARRAY(result).getJSONObject(0).getString("CompanyID");
						Log.e("cid", result);
					} catch (Exception er) {
						result = "0";
						Log.e("error", er.toString());
//Utility.showAlert(result,ctx);
					}
				}
			}
			cur.close();
			db.close();
		}
		catch (Exception er)
		{

		}
		return result;
	}
	public static String getEnterpriseRoute(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from enterprise", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
				try {
					return LogicClass.JARRAY(result).getJSONObject(0).getString("RouteID");
				}
				catch (Exception er)
				{

				}
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String getEnterpriseSource(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from enterprise", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
				try {
					return  LogicClass.JARRAY(result).getJSONObject(0).getString("source");
				}
				catch (Exception er)
				{

				}
			}
		}

		cur.close();
		db.close();
		return result;
	}
	public static String getEnterpriseDestination(Context ctx) {
		String result = "";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from enterprise", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);
				try {
					return  LogicClass.JARRAY(result).getJSONObject(0).getString("destination");
				}
				catch (Exception er)
				{

				}
			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String getEnterpriseData(Context ctx) {
		String result = "0";
		DBhelper help = new DBhelper(ctx);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from enterprise", null);
		int row = cur.getCount();
		if (row > 0) {
			while (cur.moveToNext()) {
				result = cur.getString(0);

			}
		}
		cur.close();
		db.close();
		return result;
	}
	public static String getCurrentSchedule(Context ctx) {
		String result = "0";
		try {
			DBhelper help = new DBhelper(ctx);
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from liveTrip", null);
			int row = cur.getCount();
			if (row > 0) {
				while (cur.moveToNext()) {
					result = cur.getString(0);
				}
			}
			cur.close();
			db.close();
		}
		catch (Exception er)
		{
			DBhelper help = new DBhelper(ctx);
			SQLiteDatabase db = help.getWritableDatabase();
			String liveTrip = "create table if not exists liveTrip(ScheduleID TEXT not null,TripID TEXT not null)";
			db.execSQL(liveTrip);

		}
		return result;
	}
	public static String getCurrentTrip(Context ctx) {
		String result = "0";
		try {
			DBhelper help = new DBhelper(ctx);
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from liveTrip", null);
			int row = cur.getCount();
			if (row > 0) {
				while (cur.moveToNext()) {
					result = cur.getString(1);
				}
			}
			cur.close();
			db.close();
		}
		catch (Exception er)
		{
			DBhelper help = new DBhelper(ctx);
			SQLiteDatabase db = help.getWritableDatabase();
			String liveTrip = "create table if not exists liveTrip(ScheduleID TEXT not null,TripID TEXT not null)";
			db.execSQL(liveTrip);

		}
		return result;
	}
	public static void startTrip(Context context,String schedule,String TripID) {
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getWritableDatabase();
		int result = 0;
		try {
			db.execSQL("delete from  liveTrip");
			    ContentValues values = new ContentValues();
				values.put("ScheduleID", schedule);
			    values.put("TripID", TripID);
				db.insert("liveTrip", null, values);
				Log.e("started this Journey ID",schedule);
			Log.e("started this Trip ID",TripID);
			result = 1;

		} catch (Exception er) {
          //assuming we all know the error thats likely to be caused
			if(er.toString().contains("exist")) {
				String liveTrip = "create table if not exists liveTrip(ScheduleID TEXT not null,TripID TEXT not null)";
				db.execSQL(liveTrip);
				startTrip(context, schedule,TripID);
			}
			//attempt to start this operation again
		}


	}
	public static void stopTrip(Context context) {
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getWritableDatabase();
		int result = 0;
		try {
			db.execSQL("delete from  liveTrip");
			result = 1;

		} catch (Exception er) {
			//assuming we all know the error thats likely to be caused
			if(er.toString().contains("exist")) {
				String liveTrip = "create table if not exists liveTrip(ScheduleID TEXT not null)";
				db.execSQL(liveTrip);

			}
			//attempt to start this operation again
		}


	}
}
