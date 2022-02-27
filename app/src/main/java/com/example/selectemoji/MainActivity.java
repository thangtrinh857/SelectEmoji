package com.example.selectemoji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> randomEmojis;
    String emojiLookUp;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide title bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initGame();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initGame();
    }

    private void initGame()
    {
        startTime = System.currentTimeMillis();

        String[] emojis = getRandomEmojis();
        randomEmojis = new ArrayList<>(Arrays.asList(emojis));

        setEmojiLookUp();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item, emojis);

        GridView mainGridView = findViewById(R.id.gridView);
        mainGridView.setAdapter(myAdapter);
        mainGridView.setOnItemClickListener(onPickEmoji());
    }

    private String[] getRandomEmojis()
    {
        String[] emojis = getResources().getStringArray(R.array.emoji_list);
        Collections.shuffle(Arrays.asList(emojis));
        return emojis;
    }

    private void setEmojiLookUp()
    {
        TextView mainItem = findViewById(R.id.mainItem);

        Random rand = new Random();
        int pos = rand.nextInt(randomEmojis.size());
        emojiLookUp = randomEmojis.get(pos);

        mainItem.setText(emojiLookUp);
    }

    private AdapterView.OnItemClickListener onPickEmoji()
    {
        return (adapterView, view, i, l) -> {
            TextView textView = (TextView) view;
            String pickedEmoji = textView.getText().toString();
            if (pickedEmoji.equals(emojiLookUp)) {
                textView.setText("");
                randomEmojis.remove(pickedEmoji);

                if (randomEmojis.size() == 0) {
                    startActivityResult(true);
                    return;
                }

                setEmojiLookUp();
            }
            else {
                startActivityResult(false);
            }
        };
    }

    private void startActivityResult(boolean isWin)
    {
        Intent intent = new Intent(getApplicationContext(), Result.class);
        intent.putExtra("isWin", isWin);

        long timeCount = (System.currentTimeMillis() - startTime) / 1000L;
        intent.putExtra("timeCount", timeCount);

        startActivity(intent);
    }
}