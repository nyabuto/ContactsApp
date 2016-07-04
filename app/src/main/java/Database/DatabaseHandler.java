package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Geofrey on 2/12/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="ContactsApp";
    private static final String TABLE_NAME="all_contacts";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  name VARCHAR(50)," +
                "  phone_no VARCHAR(20)," +
                "  email VARCHAR(100)," +
                "  image_url VARCHAR(100)," +
                "  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
       db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
