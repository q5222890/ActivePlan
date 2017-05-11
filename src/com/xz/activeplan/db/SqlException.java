package com.xz.activeplan.db;

/**
 * 数据库访问异常类，所有SQLite的异常都将抛出。
 * 
 * @author johnny
 * 
 */
public class SqlException extends Exception {
	private static final long serialVersionUID = 1L;

	public SqlException() {
		super();
	}

	public SqlException(String message) {
		super(message);
	}

	public SqlException(String message, java.lang.Throwable throwable) {
		super(message, throwable);
	}

	public SqlException(Throwable throwable) {
		super(throwable);
	}
}
