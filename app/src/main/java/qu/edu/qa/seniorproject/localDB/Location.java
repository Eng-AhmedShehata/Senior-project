package qu.edu.qa.seniorproject.localDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "lat")
    private Double mLatitude;

    @ColumnInfo(name = "long")
    private Double mLong;

    @ColumnInfo(name = "fav")
    private int favNum;

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getLong() {
        return mLong;
    }

    public void setLong(Double mLong) {
        this.mLong = mLong;
    }

    public int getFavNum() {
        return favNum;
    }

    public void setFavNum(int favNum) {
        this.favNum = favNum;
    }

    public Location(int id, String name, Double mLatitude, Double mLong) {
        this.id = id;
        this.name = name;
        this.mLatitude = mLatitude;
        this.mLong = mLong;
        this.favNum = 0;
    }

    @Ignore
    public Location(String name, Double mLatitude, Double mLong) {
        this.name = name;
        this.mLatitude = mLatitude;
        this.mLong = mLong;
        this.favNum = 0;
    }
}
