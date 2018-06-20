package xin.awell.filesync.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import xin.awell.filesync.model.LoadProgress;

public class TransferDBHelper extends SQLiteOpenHelper {
    public TransferDBHelper(Context context) {
        super(context, "file_sync_transfer_db", null, 1);
    }

    private static final String FILE_NAME ="file_name";
    private static final String REMOTE_FILE_PATH = "remote_file_path";
    private static final String LOCAL_FILE_PATH = "local_file_path";
    private static final String TOTAL_LENGTH = "total_length";
    private static final String CURRENT_FINISHED_LENGTH = "current_finished_length";
    private static final String STATUS = "status";

    private static final String CREATE_TABLE = "create table load_progress ("
            + "id integer primary key autoincrement, "
            + "file_name text,"
            + "remote_file_path text,"
            + "local_file_path text,"
            + "total_length integer,"
            + "current_finished_length integer,"
            + "status integer"
            + ");";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }


    public List<LoadProgress> getLoadProgressList(){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from load_progress", null);


        List<LoadProgress> loadProgressList = new ArrayList<>();
        while (cursor.moveToNext()){
            LoadProgress loadProgress = new LoadProgress();
            loadProgress.setId(cursor.getInt(cursor.getColumnIndex("id")));
            loadProgress.setFileName(cursor.getString(cursor.getColumnIndex("file_name")));
            loadProgress.setRemoteFilePath(cursor.getString(cursor.getColumnIndex("remote_file_path")));
            loadProgress.setLocalFilePath(cursor.getString(cursor.getColumnIndex("local_file_path")));
            loadProgress.setFileLength(cursor.getLong(cursor.getColumnIndex("total_length")));
            loadProgress.setCurrentFinishedLength(cursor.getLong(cursor.getColumnIndex("current_finished_length")));
            loadProgress.setStatus(cursor.getInt(cursor.getColumnIndex("status")));

            loadProgressList.add(loadProgress);
        }

        return loadProgressList;
    }


    public void saveProgress(LoadProgress loadProgress){
        ContentValues values = new ContentValues();
        loadProgress.setCreateTime(System.currentTimeMillis());
        values.put(FILE_NAME, loadProgress.getFileName());
        values.put(REMOTE_FILE_PATH, loadProgress.getRemoteFilePath());
        values.put(LOCAL_FILE_PATH, loadProgress.getLocalFilePath());
        values.put(TOTAL_LENGTH, loadProgress.getFileLength());
        values.put(CURRENT_FINISHED_LENGTH, loadProgress.getCurrentFinishedLength());
        values.put(STATUS, loadProgress.getStatus());

        this.getWritableDatabase().insert("load_progress", null, values);
    }

    public void updateProgress(LoadProgress loadProgress){
        ContentValues values = new ContentValues();
        values.put("current_finished_length", loadProgress.getCurrentFinishedLength());
        values.put("status", loadProgress.getStatus());
        this.getWritableDatabase().update("load_progress", values, "id=?", new String[]{loadProgress.getId() + ""});
    }

    public void delete(LoadProgress loadProgress){
        this.getWritableDatabase().delete("load_progress", "id=?", new String[]{loadProgress.getId() + ""});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
