package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Content.class, ContentQuery.class, Domain.class, Security.class, Attributes.class, EateryDublin.class}, version = 1, exportSchema = false)
public abstract class ContentDeliverySystem extends RoomDatabase {

    private static ContentDeliverySystem contentDeliverySystem;

    public abstract DatabaseAccessObject dao();

    public static ContentDeliverySystem getInstance(Context context) {
        if (contentDeliverySystem == null) {
            contentDeliverySystem = Room.databaseBuilder(context.getApplicationContext(), ContentDeliverySystem.class, "contentDeliverySystem.db").build();
        }
        return contentDeliverySystem;
    }

}