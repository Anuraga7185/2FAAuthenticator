package com.authentication.a2faauthenticator.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.authentication.a2faauthenticator.entity.AuthenticationDetail;

@Database(entities = {AuthenticationDetail.class}, version = 1, exportSchema = false)
public abstract class AuthDatabase extends RoomDatabase {
    public abstract AuthDao authDao();

    private static volatile AuthDatabase DB_INSTANCE;

    public static AuthDatabase getInstance(Context context) {
        if (DB_INSTANCE == null) {
            synchronized (AuthDatabase.class) {
                if (DB_INSTANCE == null) {
                    DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AuthDatabase.class, "auth_database").build();
                }
            }
        }
        return DB_INSTANCE;
    }

}
