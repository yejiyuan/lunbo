package com.example.yuanxu_hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String hiddenWord;
    char[] hiddenWordArray;
    TextView textView;
    TextView counterView;
    ImageView imageView;
    Button newGameBtn;

    private List<String> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.hiddenWord);
        counterView = findViewById(R.id.counter);
        newGameBtn = findViewById(R.id.newGameBtn);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.hangman10);

        newGameBtn.setOnClickListener(this);

        readFile();
        newGame();
        Toast.makeText(MainActivity.this, "TIP: Animals", Toast.LENGTH_SHORT).show();
    }

    //Taken & Modified Geography Quiz implementation
    private void readFile() {
        Scanner scanner = new Scanner(getResources().openRawResource(res/drawble/words_list));
        if (wordList == null) wordList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            wordList.add(word.toUpperCase());
        }
        scanner.close();
    }

    public void newGame(){
        imageView.setImageResource(R.drawable.hangman10);
        hiddenWord = getRandomWord();
        hiddenWordArray = hiddenWord.toCharArray();
        String str = "";

        for(int i = 0; i < hiddenWord.length(); i++){
            if(hiddenWordArray[i] == ' '){
                str+= " ";
            } else {
                str += "*";
            }
        }
        textView.setText(str);
    }

    public void keypress(View v){

        Button btn = (Button) v;
        String input = btn.getText().toString();
        String str = "";
        char letter = input.charAt(0);

        if(hiddenWord.contains(input)){
            char[] wordArray = textView.getText().toString().toCharArray();
            for(int i = 0; i < hiddenWordArray.length; i++){
                if(hiddenWordArray[i] == letter){
                    wordArray[i] = letter;
                    str+= wordArray[i];
                } else{
                    str+= wordArray[i];
                }
            }
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.GREEN);
            textView.setText(str);

            //When all letters are found
            if(!str.contains("*") ){
                Toast.makeText(MainActivity.this, "Congratulations! You found the word.\nClick 'New Game' to start a new Game", Toast.LENGTH_SHORT).show();
                gameOver();
            }
        }else{
            int counter = Integer.parseInt(counterView.getText().toString());
            if(counter>0){
                counter--;
                counterView.setText(Integer.toString(counter));

                //Load different image for each counter value
                //https://stackoverflow.com/a/48557508/11031957
                int resId = getResources().getIdentifier("hangman" + counter, "drawable", getPackageName());
                imageView.setImageResource(resId);

                //Disable letter
                btn.setEnabled(false);
                btn.setBackgroundColor(Color.RED);
            }
            if(counter==0){
                Toast.makeText(MainActivity.this, "Game Over! Better luck next time.\nClick 'New Game' to start a new Game", Toast.LENGTH_SHORT).show();
                gameOver();
                textView.setText(hiddenWord);
            }
        }
    }

    private String getRandomWord(){
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size()));
    }

    private void gameOver(){
        newGameBtn.setVisibility(View.VISIBLE);
    }

    //Restart Activity
    //https://stackoverflow.com/a/52331896/11031957
    public void onClick(View v) {
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}