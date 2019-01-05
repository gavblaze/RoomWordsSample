package com.gavblaze.android.roomwordssample;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;

public class WordViewModel extends AndroidViewModel {

    /*Add a private member variable to hold a reference to the Repository.*/
    private WordRepository mWordRepository;

    /*Add a private LiveData member variable to cache the list of words.*/
    private LiveData<List<Word>> mAllWords;

    /*Add a constructor that gets a reference to the WordRepository
    and gets the list of all words from the WordRepository.*/
    public WordViewModel(@NonNull Application application) {
        super(application);
        mWordRepository = new WordRepository(getApplication());
        this.mAllWords = mWordRepository.getAllWords();
    }

    /*Add a "getter" method that gets all the words.
    This completely hides the implementation from the UI.*/
    public LiveData<List<Word>> getmAllWords() {
        return mAllWords;
    }

    /*Create a wrapper insert() method that calls the Repository's insert() method.
    In this way, the implementation of insert() is completely hidden from the UI.*/
    public void insert(Word word) {
        mWordRepository.insertWord(word);
    }

    /*wrapper method to delete all words*/
    public void deleteAll() {
        mWordRepository.deleteAllWords();
    }

    /*wrapper method to delete a single word*/
    public void deleteWord(Word word) {
        mWordRepository.deleteWord(word);
    }

    /*wrapper method to update a single word*/
    public void updateWord(Word word) {
        mWordRepository.updateWord(word);
    }
}
