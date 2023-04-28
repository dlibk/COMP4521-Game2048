package comp4521.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setButtons();
    }

    public void setButtons() {
        findViewById(R.id.classic4).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("mode", "classic");
            setResult(RESULT_OK, intent);
            finish();
        });

        findViewById(R.id.speed).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("mode", "speed");
            setResult(RESULT_OK, intent);
            finish();
        });

        findViewById(R.id.zero).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("mode", "zero");
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}