package comp4521.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameView gameView = findViewById(R.id.gameView);
        for (int i = 0; i < gameView.getChildCount(); i++) {
            if (gameView.getChildAt(i) instanceof Card) {
                Card card = (Card) gameView.getChildAt(i);
                gameView.addCard(card);
                card.initialize();
            }
        }
        gameView.refreshGame();
    }
}