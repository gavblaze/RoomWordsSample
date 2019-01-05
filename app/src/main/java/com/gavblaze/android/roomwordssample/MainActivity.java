package com.gavblaze.android.roomwordssample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import static com.gavblaze.android.roomwordssample.NewWordActivity.EXTRA_REPLY;

public class MainActivity extends AppCompatActivity implements WordListAdapter.OnItemClickListener {

    private static final int INSERT_REQUEST_CODE = 333;
    private static final int UPDATE_REQUEST_CODE = 111;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WordViewModel mWordViewModel;
    public static final String EXTRA_UPDATE = "extra_update";
    public static final String EXTRA_ID = "extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), NewWordActivity.class);
                startActivityForResult(intent, INSERT_REQUEST_CODE);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        final WordListAdapter adapter = new WordListAdapter(this);

        mRecyclerView.setAdapter(adapter);

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getmAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                adapter.swapData(words);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word word = adapter.mWordList.get(position);
                Toast.makeText(MainActivity.this, "Deleting " + word.getWord(), Toast.LENGTH_LONG).show();
                mWordViewModel.deleteWord(word);
                adapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_cancelAll:
                mWordViewModel.deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClicked(Word word) {
        Intent intent = new Intent(this, NewWordActivity.class);
        intent.putExtra(EXTRA_UPDATE, word.getWord());
        intent.putExtra(EXTRA_ID, word.getId());
        startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            //on Activity result from click of FAB (Insert new item)
        if (requestCode == INSERT_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            String valueEntered = data.getStringExtra(EXTRA_REPLY);
            Word word = new Word(valueEntered);
            //insert this into the database
            mWordViewModel.insert(word);

            //on Activity result from click of item in recyclerview (Update existing item)
        } else if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            String updatedValue = data.getStringExtra(EXTRA_REPLY);
            int id = data.getIntExtra(EXTRA_ID, -1);
            //Use the Word pojo that takes an id and word value as paramaters.
            // Room will use the primary key (in this case the id) to find the existing entry in the database so it can be updated.
            Word word = new Word(id, updatedValue);
            mWordViewModel.updateWord(word);
        }
    }
}
