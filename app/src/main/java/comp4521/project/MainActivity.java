package comp4521.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import comp4521.project.game.Game;
import comp4521.project.game.Mode;

public class MainActivity extends AppCompatActivity {
    GameView gameView;
    Game game;
    ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameView);
        game = gameView.game;
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null)
                            return;
                        String modeName = data.getExtras().getString("mode", "back");
                        Mode mode = Mode.of(modeName);
                        if (mode == null)
                            return;
                        if (mode != game.getMode()) {
                            game.switchMode(mode);
                            gameView.clearScoreBoard();
                        }
                    }
                });

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
        Intent intent = new Intent(this, MenuActivity.class);
        game.engine().pause();
        launcher.launch(intent);
    }

    @Override
    public void onBackPressed() {
        game.engine().pause();
    }
}