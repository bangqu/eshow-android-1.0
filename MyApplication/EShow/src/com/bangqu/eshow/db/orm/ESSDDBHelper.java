/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bangqu.eshow.db.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

// TODO: Auto-generated Javadoc
/**
 * SD卡中保存数据库辅助类
 *
 */
public class ESSDDBHelper extends ESSDSQLiteOpenHelper {
	
	/** The model classes. */
	private Class<?>[] modelClasses;

	/**
	 * 初始化一个AbSDDBHelper.
	 *
	 * @param context 应用context
	 * @param dir 数据库文件保存文件夹全路径
	 * @param name 数据库文件名
	 * @param factory 数据库查询的游标工厂
	 * @param version 数据库的新版本号
	 * @param modelClasses 要初始化的表的对象
	 */
	public ESSDDBHelper(Context context, String dir, String name,
						SQLiteDatabase.CursorFactory factory, int version,
						Class<?>[] modelClasses) {
        super(context, dir,name, null, version);
		this.modelClasses = modelClasses;

	}

    /**
     * 描述：表的创建.
     *
     * @param db 数据库对象
     * @see ESSDSQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
		ESTableHelper.createTablesByClasses(db, this.modelClasses);
	}

	/**
	 * 描述：表的重建.
	 *
	 * @param db 数据库对象
	 * @param oldVersion 旧版本号
	 * @param newVersion 新版本号
	 * @see ESSDSQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ESTableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
}
