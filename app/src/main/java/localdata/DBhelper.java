package localdata;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static String DB_NAME = "easybus";
	public DBhelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String user = "create table if not exists users(fullname TEXT not null,telephone TEXT,email " +
				"TEXT not null,user_id TEXT primary key,roleid TEXT not null)";
		String routes = "create table if not exists routes(data TEXT not null)";
		String prices = "create table if not exists prices(data TEXT not null)";
		String schedules = "create table if not exists schedules(data TEXT not null,source TEXT not null,destination TEXT not null)";
		String tickets = "create table if not exists tickets(data TEXT not null)";
		String gcm = "create table if not exists gcm(data TEXT not null)";
		String enterprise = "create table if not exists enterprise(data TEXT not null)";
		String status = "create table if not exists status(data TEXT not null)";
		String pickups = "create table if not exists pickups(data TEXT not null)";
		String liveTrip = "create table if not exists liveTrip(ScheduleID TEXT not null,TripID TEXT)";

		db.execSQL(user);
		db.execSQL(routes);
		db.execSQL(prices);
		db.execSQL(schedules);
		db.execSQL(tickets);
		db.execSQL(gcm);
		db.execSQL(enterprise);
		db.execSQL(status);
		db.execSQL(pickups);
		db.execSQL(liveTrip);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("drop table if exists users");
		db.execSQL("drop table if exists routes");
		db.execSQL("drop table if exists prices");
		db.execSQL("drop table if exists schedules");
		db.execSQL("drop table if exists tickets");
		db.execSQL("drop table if exists gcm");
		db.execSQL("drop table if exists enterprise");
		db.execSQL("drop table if exists status");
		db.execSQL("drop table if exists pickups");
		db.execSQL("drop table if exists liveTrip");
	}
}
