package com.pasc.lib.userbase.base.data;


import com.pasc.lib.userbase.base.data.user.User;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * 非用户数据库
 * Created by duyuan797 on 17/10/30.
 */

@Database(name = SmtDb.NAME, version = SmtDb.VERSION)
public class SmtDb {
    public static final String NAME = "SmtDatabase";

    //public static final int VERSION = 2;//新增、删除、修改变结构必须更改版本号
    public static final int VERSION = 3;//新增 payAccountId


    @Migration(version = VERSION, database = SmtDb.class)
    public static class UserUpdate extends AlterTableMigration<User> {

        public UserUpdate(Class<User> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.TEXT, "payAccountId");
        }


    }


}
