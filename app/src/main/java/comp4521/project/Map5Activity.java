package comp4521.project;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import comp4521.project.game.Game;
import comp4521.project.game.Mode;

public class Map5Activity extends AppCompatActivity {
    GameView gameView;
    Game game;
    MediaPlayer onButtonClickedPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map5);
        init();
    }

    private void init() {
        gameView = findViewById(R.id.gameView);
        game = gameView.game;
        var cellList = game.getCellList();
        for (int i = 0; i < gameView.getChildCount(); i++) {
            Card card = (Card) gameView.getChildAt(i);
            cellList.get(i).bindCard(card);
        }
        gameView.setScoreboard(findViewById(R.id.scoreboard));
        game.initialize();
        onButtonClickedPlayer = MediaPlayer.create(this, R.raw.buttonclicked);
        onButtonClickedPlayer.setVolume(1.0f, 1.0f);
    }

    public void reset(View v) {
        onButtonClickedPlayer.seekTo(0);
        onButtonClickedPlayer.start();
        game.initialize();
        gameView.clearScoreBoard();

    }

    public void goMenu(View v) {
        onButtonClickedPlayer.seekTo(0);
        onButtonClickedPlayer.start();
        game.engine().pause();
        finish();
    }

    @Override
    public void onBackPressed() {
        game.engine().pause();
        super.onBackPressed();
    }
}