package com.example.connectfour.Board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.connectfour.NewGameActivity;
import com.example.connectfour.R;
import com.example.connectfour.SettingsActivities.PauseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**Class which represents the "Vs AI" game board screen.**/
public class VsAIGameBoard extends AppCompatActivity {

    public static final String EXTRA_ROWS = "com.example.connectfour.Board.EXTRA_ROWS";
    public static final String EXTRA_COLUMNS = "com.example.connectfour.Board.EXTRA_COLUMNS";

    int player1Col = Color.RED;
    int player2Col = Color.YELLOW;

    private int rows;
    private int columns;
    private int boardSize;

    int[] image;

    GridView gridView;
    Random rand = new Random();
    ArrayList<ImageModel> imageModels;

    ArrayList<Integer> filledSlot;

    ConnectFourGame game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MediaPlayer media = MediaPlayer.create(VsAIGameBoard.this, R.raw.lol);
        //DARK MODE
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
            ;
        } else {
            setTheme(R.style.AppTheme);
        }

        if (savedInstanceState != null) {

            setRows(8);
            setColumns(8);
            setBoardSize(getRows(), getColumns());
        }
        else {
            Intent intent = getIntent();
            setRows(intent.getIntExtra(NewGameActivity.EXTRA_ROWS, 0));
            setColumns(intent.getIntExtra(NewGameActivity.EXTRA_COLUMNS, 0));
            setBoardSize(getRows(), getColumns());
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_a_i_game_board);
        media.start();

        //Initialise game logic.
        game = new ConnectFourGame(getRows(), getColumns());

        filledSlot = new ArrayList<Integer>();

        //initialise board to be chosen size and fill with board images
        image = new int[boardSize];
        Arrays.fill(image, R.drawable.board);

        TextView playerTurnText = (TextView) findViewById(R.id.playerTurn);
        playerTurnText.setText("Player 1 Turn!");

        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setNumColumns(columns);

        imageModels = new ArrayList<ImageModel>();

        for (int i = 0; i < image.length; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setmThumbIds(image[i]);
            //add to array list of imageModels
            imageModels.add(imageModel);
        }

        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), imageModels, getBoardSize());
        gridView.setAdapter(adapter);

        //item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Tracks whose turn it is
            int turnNo = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if turn number is even player one colour
                if (!filledSlot.contains(position) && turnNo % 2 == 0) {
                    int playablePosition = game.chooseAndDropCounter(position);
                    filledSlot.add(playablePosition);
                    turnNo++;

                    System.out.println("PLAYER CHOOSES: " + position);
                    System.out.println("List of filled slots: " + filledSlot);

                    playerTurnText.setText("AI Turn!");
                    adapter.setBackgroundOfImageView(playablePosition, getPlayer1Col());

                    //PAUSE for 1s - AS cant just delay by sleep because pauses the front end too
                    // so requires a handler for this process.
                    (new Handler()).postDelayed(() -> {
                        //AI TAKES TURN and adds position to filled slots if board has slots
                        if (filledSlot.size() < boardSize) {
                            int playableAIPosition = game.chooseAndDropCounter(chooseAIMove());
                            adapter.setBackgroundOfImageView(playableAIPosition, getGetPlayer2Col());
                            filledSlot.add(playableAIPosition);
                            turnNo++;
                            playerTurnText.setText("Player 1 Turn!");
                        } else {
                            System.out.println("BOARD IS FULL");
                        }
                        adapter.notifyDataSetChanged();
                    }, 500);
                }
            }
        });

        Button pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VsAIGameBoard.this, PauseActivity.class);
                intent.putExtra(EXTRA_ROWS, getRows());
                intent.putExtra(EXTRA_COLUMNS, getColumns());
                startActivity(intent);
                media.stop();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("rows", getRows());
        savedInstanceState.putInt("columns", getColumns());

        super.onSaveInstanceState(savedInstanceState);
    }

    public int getPlayer1Col() {
        return player1Col;
    }

    public void setPlayer1Col(int player1Col) {
        this.player1Col = player1Col;
    }

    public int getGetPlayer2Col() {
        return player2Col;
    }

    public void setGetPlayer2Col(int player2Col) {
        this.player2Col = player2Col;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int rows, int columns) {
        boardSize = rows * columns;
    }

    public ArrayList<Integer> getFilledSlot() {
        return filledSlot;
    }

    public boolean isPosValid(int position){
        if (getFilledSlot().contains(position)){
            return false;
        }
        else {
            return true;
        }
    }

    public int chooseAIMove() {
        int chosenAIPos = rand.nextInt(getBoardSize());
        while (!isPosValid(chosenAIPos)){
            System.out.println("AI TRIED: " + chosenAIPos);
            chosenAIPos = rand.nextInt(getBoardSize());
            if (isPosValid(chosenAIPos)){
                System.out.println("AI CHOOSES: " + chosenAIPos);
                System.out.println("List of filled slots: " + filledSlot);
                //Function will terminate here and not below if this new one is valid.
                return chosenAIPos;
            }
        }
        System.out.println("AI CHOOSES: " + chosenAIPos);
        System.out.println("List of filled slots: " + filledSlot);

        return chosenAIPos;
    }
}
