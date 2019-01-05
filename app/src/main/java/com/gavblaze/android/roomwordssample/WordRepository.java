package com.gavblaze.android.roomwordssample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;

class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    /*Add a constructor that gets a handle to the database and initializes the member variables.*/
    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDataAccessObject();
        mAllWords = db.wordDataAccessObject().getAllWords();
    }

    /*Add a wrapper method called getAllWords() that returns the cached words as LiveData*/
    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    /*Add a wrapper for the insert() method.
    Use an AsyncTask to call insert() on a non-UI thread, or your app will crash.
    Room ensures that you don't do any long-running operations on the main thread, which would block the UI.*/
    void insertWord(Word word) {
        new InsertAsyncTask(mWordDao).execute(word);
    }

    /* Add a wrapper for the deleteAll() method.*/
    void deleteAllWords() {
        new DeleteAsyncTask(mWordDao).execute();
    }

    /*Add a wrapper for deleting a single word*/
    void deleteWord(Word word) {
       new DeleteWordAsyncTask(mWordDao).execute(word);
    }

    void updateWord(Word word) {
        new UpdateWordAsyncTask(mWordDao).execute(word);
    }


    private static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao mAsyncTaskDao;

        InsertAsyncTask(WordDao wordDao) {
            this.mAsyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            mAsyncTaskDao.insert(words[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao mAsyncTaskDao;
        DeleteAsyncTask(WordDao wordDao) {
            this.mAsyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao mAsyncTaskDao;
        DeleteWordAsyncTask(WordDao wordDao) {
            this.mAsyncTaskDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            mAsyncTaskDao.deleteWord(words[0]);
            return null;
        }
    }

    private static class UpdateWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao mAsyncTaskDao;
        UpdateWordAsyncTask(WordDao wordDao) {
            this.mAsyncTaskDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            mAsyncTaskDao.update(words[0]);
            return null;
        }
    }
}
