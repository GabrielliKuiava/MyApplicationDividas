package com.example.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.model.Divida;

@Database(entities = {Divida.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DividaDao dividaDao();
}