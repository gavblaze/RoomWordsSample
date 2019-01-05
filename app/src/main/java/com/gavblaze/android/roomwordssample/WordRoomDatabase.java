package com.gavblaze.android.roomwordssample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Word.class}, version = 2, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    private static WordRoomDatabase DB_INSTANCE;

    /*Define the DAOs that work with the database*/
    public abstract WordDao wordDataAccessObject();


    public static WordRoomDatabase getDatabase(final Context context) {
        if (DB_INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (DB_INSTANCE == null) {
                    // Create database here
                    DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return DB_INSTANCE;
    }

    /*There is no data in the database yet*/
    /*Create the callback for populating the database*/
    private static RoomDatabase.Callback sRoomDatabaseCallback  = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //intial populate of the database
            new PopulateDbAsyncTask(DB_INSTANCE).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao mDao;
        String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsyncTask(WordRoomDatabase db) {
            this.mDao = db.wordDataAccessObject();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // If we have no words, then create the initial list of words
            if (mDao.getAnyWord().length < 1) {
                for (int i = 0; i <= words.length - 1; i++) {
                    Word word = new Word(words[i]);
                    mDao.insert(word);
                }
            }
            return null;
        }
    }
}
