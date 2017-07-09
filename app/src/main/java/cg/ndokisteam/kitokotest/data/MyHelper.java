package cg.ndokisteam.kitokotest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * SQLITE
 */

public class MyHelper extends SQLiteAssetHelper {

    private final static String DB_NAME = "kitoko_db.db";
    private final static int DB_VERSION = 1;


    public MyHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
