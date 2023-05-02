package comp4521.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    Intent intent5, intent6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        intent5 = new Intent(this, Map5Activity.class);
        intent6 = new Intent(this, Map6Activity.class);
    }

    public void goClassic4(View v) {
        Intent intent = new Intent();
        intent.putExtra("mode", "classic");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void goSpeed(View v) {
        Intent intent = new Intent();
        intent.putExtra("mode", "speed");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void goZero(View v) {
        Intent intent = new Intent();
        intent.putExtra("mode", "zero");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void goClassic5(View v) {
        startActivity(intent5);
    }

    public void goClassic6(View v) {
        startActivity(intent6);
    }
}