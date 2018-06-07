/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Main2Activity extends AppCompatActivity {

    private static final int WORD_LENGTH = 3;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2,word3;
    Button start_btn,undo_btn;

    private Stack<LetterTile> placedTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        start_btn = (Button)findViewById(R.id.start_button);
        undo_btn = (Button)findViewById(R.id.button);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length() == WORD_LENGTH)
                    words.add(word);
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 4);

        View word3LinearLayout = findViewById(R.id.word3);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
      word3LinearLayout.setOnDragListener(new DragListener());


        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());


    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2 + " " + word3);
                }

                placedTiles.push(tile);
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    Log.e("tag","drag started!");
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2+ " "+word3);
                        start_btn.setEnabled(true);
                        undo_btn.setEnabled(false);
                    }

                    placedTiles.push(tile);

                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        start_btn.setEnabled(false);
        undo_btn.setEnabled(true);
        placedTiles = new Stack<>();
        LinearLayout word1LinearLayout = (LinearLayout)findViewById(R.id.word1);
        word1LinearLayout.removeAllViews();
        LinearLayout word2LinearLayout = (LinearLayout)findViewById(R.id.word2);
        word2LinearLayout.removeAllViews();
        LinearLayout word3LinearLayout = (LinearLayout)findViewById(R.id.word3);
        word3LinearLayout.removeAllViews();

        stackedLayout.clear();

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");

        word1 = words.get(random.nextInt(words.size()));
        word2 = words.get(random.nextInt(words.size()));
        word3 = words.get(random.nextInt(words.size()));
        //Toast.makeText(this, word1 + "  "+word2+"  "+word3, Toast.LENGTH_SHORT).show();

        Log.e("tag",word1 + "   "+word2 + "   "+word3);

        int counter1=0,counter2=0,counter3=0;
        String scramble = "";

        while(counter1 < WORD_LENGTH && counter2 < WORD_LENGTH && counter3<WORD_LENGTH){
            int whichWord = random.nextInt(3);
            if (whichWord == 0){
                scramble += word1.charAt(counter1);
                counter1++;
            }
            else if (whichWord==1)
            {
                scramble+=word2.charAt(counter2);
                counter2++;
            }
            else{
                scramble+=word3.charAt(counter3);
                counter3++;
            }
        }

        if (counter1 == WORD_LENGTH && counter2 < WORD_LENGTH && counter3 <WORD_LENGTH){
            while(counter2 < WORD_LENGTH && counter3 < WORD_LENGTH){
                int whichWord = random.nextInt(2);
                if (whichWord == 0) {
                    scramble += word2.charAt(counter2);
                    counter2++;
                }
                else{
                    scramble+=word3.charAt(counter3);
                    counter3++;
                }
            }
            if (counter2 == WORD_LENGTH && counter3 < WORD_LENGTH){
                scramble +=word3.substring(counter3);
            }

            if (counter3 == WORD_LENGTH && counter2 < WORD_LENGTH){
                scramble +=word2.substring(counter2);
            }

        }
        if (counter2 == WORD_LENGTH && counter1 < WORD_LENGTH && counter3 <WORD_LENGTH){
            while(counter1 < WORD_LENGTH && counter3 < WORD_LENGTH){
                int whichWord = random.nextInt(2);
                if (whichWord == 0) {
                    scramble += word1.charAt(counter1);
                    counter1++;
                }
                else{
                    scramble+=word3.charAt(counter3);
                    counter3++;
                }
            }
            if (counter3 == WORD_LENGTH && counter1 < WORD_LENGTH){
                scramble +=word1.substring(counter1);
            }

            if (counter1 == WORD_LENGTH && counter3 < WORD_LENGTH){
                scramble +=word3.substring(counter3);
            }

        }
        if (counter3 == WORD_LENGTH && counter2 < WORD_LENGTH && counter1 <WORD_LENGTH){
            while(counter2 < WORD_LENGTH && counter1 < WORD_LENGTH){
                int whichWord = random.nextInt(2);
                if (whichWord == 0) {
                    scramble += word2.charAt(counter2);
                    counter2++;
                }
                else{
                    scramble+=word1.charAt(counter1);
                    counter1++;
                }


            }
            if (counter1 == WORD_LENGTH && counter2 < WORD_LENGTH){
                scramble +=word2.substring(counter2);
            }

            if (counter2 == WORD_LENGTH && counter1 < WORD_LENGTH){
                scramble +=word1.substring(counter1);
            }
        }




        messageBox.setText(scramble);
        //Toast.makeText(this, String.valueOf(scramble.length()), Toast.LENGTH_SHORT).show();

        for (int i =scramble.length()-1 ; i >= 0 ; i--){
            stackedLayout.push(new LetterTile(this, scramble.charAt(i)));
        }

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wordstack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting) {
            Intent i=new Intent(this,SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onUndo(View view) {

        if(!placedTiles.isEmpty()){
            LetterTile tile = placedTiles.pop();
            tile.moveToViewGroup(stackedLayout);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
