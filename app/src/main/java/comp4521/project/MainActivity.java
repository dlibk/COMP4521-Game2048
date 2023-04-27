package comp4521.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameView);
        var cellList = getGame().getCellList();
        for (int i = 0; i < gameView.getChildCount(); i++) {
            Card card = (Card) gameView.getChildAt(i);
            card.initialize();
            cellList.get(i).bindCard(card);
        }
        getGame().initialize();
    }

    private Game getGame() {
        return gameView.game;
    }


}