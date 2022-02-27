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
    ArrayList<String> randomList;
    String curEmoji;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    private void init()
    {
        startTime = System.currentTimeMillis();

        String[] emojis = getRandomEmojis();
        randomList = new ArrayList<>(Arrays.asList(emojis));

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
        int pos = rand.nextInt(randomList.size());
        curEmoji = randomList.get(pos);

        mainItem.setText(curEmoji);
    }

    private AdapterView.OnItemClickListener onPickEmoji()
    {
        return (adapterView, view, i, l) -> {
            TextView textView = (TextView) view;
            String pickedEmoji = textView.getText().toString();
            if (pickedEmoji.equals(curEmoji)) {
                textView.setText("");
                randomList.remove(pickedEmoji);

                if (randomList.size() == 0) {
                    showActivityResult(true);
                    return;
                }

                setEmojiLookUp();
            }
            else {
                showActivityResult(false);
            }
        };
    }

    private void showActivityResult(boolean isWin)
    {
        Intent intent = new Intent(getApplicationContext(), Result.class);
        intent.putExtra("isWin", isWin);

        long timeCount = (System.currentTimeMillis() - startTime) / 1000L;
        intent.putExtra("timeCount", timeCount);

        startActivity(intent);
    }
}