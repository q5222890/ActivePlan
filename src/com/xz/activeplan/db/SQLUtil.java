package com.xz.activeplan.db;

public class SQLUtil {
	private static SQLUtil sqlUtil;

	public synchronized static SQLUtil getInstance() {
		if (sqlUtil == null) {
			sqlUtil = new SQLUtil();
		}
		return sqlUtil;
	}

	
}
