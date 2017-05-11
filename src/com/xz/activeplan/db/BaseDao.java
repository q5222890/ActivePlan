package com.xz.activeplan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;
import android.util.Log;

/**
 * 数据库基类，完成数据库的基本操作：关闭cursor，关闭db，获取数据库数据，升级数据库数据（包括插入和修改）
 * 
 * @author johnny
 * 
 */
public abstract class BaseDao extends SqliteHelper {
	private static final String TAG = "BaseDao";
	private static final String DB_NAME = "db_wscloud";
	@SuppressWarnings("unused")
	private Context mContext;

	public BaseDao(Context context) {
		this(context, "db_wscloud", null, 1);
	}

	public BaseDao(Context context, String name) {
		this(context, name, null, 1);
	}

	public BaseDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name == null ? DB_NAME : name, factory, version);
		this.mContext = context;
	}

	/**
	 * 关闭数据库
	 * 
	 * @param db
	 */
	protected void closeDb(SQLiteDatabase db) {
		if (db != null && db.isOpen()) {
			db.close();
			db = null;
		}
	}

	/**
	 * 关闭游标
	 * 
	 * @param cursor
	 */
	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
	}

	/**
	 * 获取数据库数据
	 * 
	 * @param db
	 *            database操作类
	 * @param sql
	 *            数据库语句
	 * @param dbAdapter
	 *            数据库数据转化适配类
	 * @return
	 */
	public Object getObject(SQLiteDatabase db, String sql, IDbAdapter dbAdapter)
			throws Exception {
		Cursor cursor = db.rawQuery(sql, null);
		try {
			return dbAdapter.getDbObject(cursor);
		} finally {
			closeCursor(cursor);
		}
	}

	public int getCount(SQLiteDatabase db, String sql) {
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			return cursor.getCount();
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return 0;
	}

	/**
	 * 升级数据库数据，该操作将手动开启事物。
	 * 
	 * @param db
	 *            database操作类
	 * @param sqls
	 *            数据库语句数组
	 * @throws SqlException
	 *             数据升级失败将抛出，包括出现空语句，数据升级失败。
	 */
	public void upData(SQLiteDatabase db, String... sqls) throws SqlException {
		if (sqls.length == 0) {
			throw new SqlException("sql语句不能为空");
		}
		try {
			db.beginTransaction();
			for (String sql : sqls) {
				if (TextUtils.isEmpty(sql)) {
					throw new SqlException("sql语句不能为空");
				}
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new SqlException("插入数据失败");
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			createTableOnCreate(db);
			onUpgrade(db, 1001, db.getVersion());
		} catch (SqlException e) {
			Log.d(TAG, String.valueOf(e));
		}
	}

	protected void createTableOnCreate(SQLiteDatabase db) throws SqlException {
//		String createOrderTable = "create table if not exists Orders(orderid primary key,total,price,truename,tel,address,time,buytime,paid,sent,handled,payType,goodsTypeCount)";
//		upData(db, createOrderTable);
	};

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	/**
	 * 获取数据库数据转化适配类接口
	 * 
	 * @author CSC
	 * 
	 */
	public interface IDbAdapter {
		public Object getDbObject(Cursor cursor);
	}

}
