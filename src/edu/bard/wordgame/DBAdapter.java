/*Name: Jacob Fauber
 * Lab 6
 */

package edu.bard.wordgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {
	
	/** Database information **/
	private static final String DATABASE_NAME = "leveldatabase.db";
	private static final String DATABASE_TABLE = "level";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ID = "_id";
	public static final int COL_ID = 0;

	public static final String KEY_TITLE = "task";
	public static final int COL_TITLE = 1;
	
	public static final String KEY_LEVELTEXT = "date";
	public static final int COL_LEVELTEXT = 2;
	
	public static final String KEY_SPOOFTEXT = "status";
	public static final int COL_SPOOFTEXT = 3;

	// TODO: this is incomplete
	public static String[] ALL_COLUMNS = new String[] {KEY_ID, KEY_TITLE, KEY_LEVELTEXT, KEY_SPOOFTEXT};
	
	/**
	 * Database of Apps that the ContentProvider interfaces with.
	 */
	private SQLiteDatabase m_DB;
	private DBOpenHelper m_dbHelper;
	private final Context context;
	
	public DBAdapter (Context _context) {
		context = _context;
		m_dbHelper = new DBOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION);

	}
	
	public void close() {
		m_dbHelper.close();
	}
	
	public void open() throws SQLiteException {
		m_DB = m_dbHelper.getWritableDatabase();
	}
	
	/**
	 * Insert new todo item.
	 */
	public long insertItem(LevelItem item) {
		//TODO
		ContentValues values = new ContentValues(4);
		values.putNull(ALL_COLUMNS[COL_ID]);
		values.put(ALL_COLUMNS[COL_TITLE], item.title);
		values.put(ALL_COLUMNS[COL_LEVELTEXT], item.levelText);
		values.put(ALL_COLUMNS[COL_SPOOFTEXT], item.fakeLevelText);
		long returnValue = m_DB.insert(DATABASE_TABLE, null, values);
			
		return returnValue;
	}

	/**
	 * Insert new todo item.
	 */
	public boolean replaceItem(LevelItem olditem, LevelItem item) {
		
		Cursor m_cursor;
		try{
			m_cursor = query(olditem.title);
		}catch(SQLException e){
			return false;	
		}
		
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[COL_ID], m_cursor.getLong(COL_ID));
		values.put(ALL_COLUMNS[COL_TITLE], item.title);
		values.put(ALL_COLUMNS[COL_LEVELTEXT], item.levelText);
		values.put(ALL_COLUMNS[COL_SPOOFTEXT], item.fakeLevelText);

		long returnVal = m_DB.update(DATABASE_TABLE, values, KEY_ID + " = ?", new String[] {Integer.toString(m_cursor.getInt(COL_ID))});
		m_cursor.close();
		if(returnVal >=0)
			return true;
		else
			return false;
	}
		

	public boolean removeAll() {
		return m_DB.delete(DATABASE_TABLE,null, null) > 0;
	}
	
	public boolean removeTask(String name) {
		Log.i("la", name+"alsad");
		Cursor m_cursor = query(name);
		return m_DB.delete(DATABASE_TABLE, KEY_ID + " = ?", new String[] {Integer.toString(m_cursor.getInt(COL_ID))}) > 0;
	}
	
	
	/**
	 * Update the task with given task string.
	 * @param item will replace all other fields of todo item (not the task)
	 * @return
	 */
	public boolean updateItem(LevelItem item) {
		//TODO
		Cursor m_cursor;
		try{
			m_cursor = query(item.title);
		}catch(SQLException e){
			return false;	
		}
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[COL_ID], m_cursor.getLong(COL_ID));
		values.put(ALL_COLUMNS[COL_TITLE], item.title);
		values.put(ALL_COLUMNS[COL_LEVELTEXT], item.levelText);
		values.put(ALL_COLUMNS[COL_SPOOFTEXT], item.fakeLevelText);

		m_DB.update(DATABASE_TABLE, values, null, null);
		
		m_cursor.close();
		return true;
	}
	
	/**
	 * Return cursor to all todo items in database
	 * @return cursor to all todo items in database
	 */
	
	public Cursor getAllItems() {
		return m_DB.query(DATABASE_TABLE, ALL_COLUMNS,
				null, null, null, null, KEY_ID);
	}
	/**
	 * Return cursor result of a query
	 */
	public Cursor query(String name) throws SQLException {
		String[] columns = new String[] {KEY_ID, KEY_TITLE, KEY_LEVELTEXT, KEY_SPOOFTEXT};
		Cursor result = m_DB.query(true, DATABASE_TABLE, columns,
				KEY_TITLE + " = " + "'" + name + "'", 
				null, null, null, null, null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No itemfound for row: " + name);
		}
		return result;
	}
	
	/*************************************************************************
	 * Logic for database creation and update.
	 ********************************************************************************/
	private static class DBOpenHelper extends SQLiteOpenHelper {
		/** Database SQL **/
		// TODO : incomplete
		public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + 
				KEY_ID + " integer primary key autoincrement, " +
				KEY_TITLE + " text, " +
				KEY_LEVELTEXT + " text, " +
				KEY_SPOOFTEXT + " text"
				+ " );";

		
		public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE;
		
		public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			Log.w("DBADapter","Upgrading from version " + _oldVersion +
					" to " + _newVersion + ", which will destroy all old data.");
			_db.execSQL(DATABASE_DROP); // drop old
			onCreate(_db); // create new
		}
	}
	
	
}