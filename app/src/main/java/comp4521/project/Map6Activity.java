package comp4521.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class Map6Activity extends AppCompatActivity {

    GameView gameView;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map6);
        gameView = findViewById(R.id.gameView);
        game = gameView.game;
        var cellList = game.getCellList();
        for (int i = 0; i < gameView.getChildCount(); i++) {
            Card card = (Card) gameView.getChildAt(i);
            cellList.get(i).bindCard(card);
        }
        gameView.setScoreboard(findViewById(R.id.scoreboard));
        game.initialize();
    }

    public void reset(View v) {
        game.initialize();
        gameView.clearScoreBoard();
    }

    public void goMenu(View v) {
        game.engine().pause();
        finish();
    }

    @Override
    public void onBackPressed() {
        game.engine().pause();
        super.onBackPressed();
    }
}