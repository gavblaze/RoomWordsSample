package com.gavblaze.android.roomwordssample;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Word word);

    @Query("DELETE FROM word_table")
    public void deleteAll();

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    public LiveData<List<Word>> getAllWords();

    @Query("SELECT * from word_table LIMIT 1")
    public Word[] getAnyWord();

    @Delete
    public void deleteWord(Word word);

    @Update
    public void update(Word word);
}
