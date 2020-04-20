package qu.edu.qa.seniorproject.localDB;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Location.class } , version = 1 , exportSchema = false)
public abstract  class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;
    public abstract LocationDao dataDao();


    public synchronized static AppDataBase getInstance(Context context) {
        Log.v("app data"," before if");
        if (INSTANCE == null) {
            Log.v("app data","inside if ");
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDataBase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDataBase.class,"my-database")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                       loadDataBase(context);
                    }
                })
                .build();
    }

    private static void loadDataBase(Context context) {
        final Context mContext = context ;
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getInstance(mContext).dataDao().insertAllLocation(populateData());
            }
        });
    }

    public static List<Location> populateData(){
        List<Location> list = new ArrayList<>();

        list.add(new Location("Qatar University Mosque", 25.376349,51.4909));
        list.add(new Location("Qatar University Library", 25.377047,51.489666));
        list.add(new Location("Qatar University Qpost Branch", 25.375477,51.488915));
        list.add(new Location("Qatar University College of Engineering", 25.375448,51.490267));
        list.add(new Location("Qatar University A05-Administration Building", 25.37759,51.491982));
        list.add(new Location("Qatar University A06- Men's Foundation Building", 25.378501,51.491542));
        list.add(new Location("Qatar University H12-College of Medicine", 25.380149,51.492282));
        list.add(new Location("Qatar University Qatar National Bank Branch", 25.377333,51.491306));
        list.add(new Location("Qatar University B03-Engineering Research Buildings", 25.37585,51.49063));
        list.add(new Location("Qatar University Annex Building", 25.375273,51.491177));
        list.add(new Location("Qatar University College of Education ", 25.373732,51.491311));
        list.add(new Location("Qatar University Bookstore ", 25.37254,51.491858));
        list.add(new Location("Qatar University B05-Main Men's Building ", 25.374144,51.49182));
        list.add(new Location("Qatar University B01- Higher Administration Building", 25.373703,51.492523));
        list.add(new Location("Qatar University B13- Men Activities Building", 25.373373,51.493199));
        list.add(new Location("Qatar University A02- Sport Court ", 25.37489,51.494229));
        list.add(new Location("Qatar University Men's Gym ", 25.376121,51.493719));
        list.add(new Location("Qatar University C12- Admission and Registration ", 25.376208,51.487577));
        list.add(new Location("Qatar University H08- College of Business and Economics", 25.376914,51.486676));
        list.add(new Location("Qatar University Student Parking Lot", 25.377786,51.490506));


        return list ;
    }
}
