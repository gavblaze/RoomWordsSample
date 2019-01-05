package com.gavblaze.android.roomwordssample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.gavblaze.android.roomwordssample.MainActivity.EXTRA_UPDATE;
import static com.gavblaze.android.roomwordssample.MainActivity.EXTRA_ID;

public class NewWordActivity extends AppCompatActivity {
    private EditText mEditText;
    private Button mSaveButton;
    public static final String EXTRA_REPLY = "extra_reply";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditText = findViewById(R.id.edit_word);
        mSaveButton = findViewById(R.id.button_save);

        // If we are passed content, fill it in for the user to edit.
        final Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            String word = intentThatStartedThisActivity.getStringExtra(EXTRA_UPDATE);
            mEditText.setText(word);
        }

        // When the user presses the Save button, create a new Intent for the reply.
        // The reply Intent will be sent back to the calling activity (in this case, MainActivity).
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value = mEditText.getText().toString().trim();
                Intent replyIntent = new Intent();

                /*If EditText is blank do display Toast message*/
                if (TextUtils.isEmpty(value)) {
                    // No word was entered, set the result accordingly.
                    setResult(RESULT_CANCELED, replyIntent);
                    Toast.makeText(getApplicationContext(), "Please enter a valid value", Toast.LENGTH_SHORT).show();

                } else {

                    //  Put the new word entered in the extras for the reply Intent.
                    replyIntent.putExtra(EXTRA_REPLY, value);
                    int id = 0;
                    if (intentThatStartedThisActivity != null) {
                        id = intentThatStartedThisActivity.getIntExtra(EXTRA_ID, -1);
                    }
                    if (id != -1) { // ie. if we are updating an existing word
                        setResult(RESULT_OK);
                        replyIntent.putExtra(EXTRA_ID, id);
                    }
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });
    }
}
