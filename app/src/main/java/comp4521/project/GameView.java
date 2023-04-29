package comp4521.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class GameView extends GridLayout {

    public static final Animation scoreboardAnimation = new ScaleAnimation(
            0.8f, 1.2f, 0.8f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
    );

    protected Game game;
    protected TextView scoreboard;

    public GameView(Context context) {
        super(context);
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void initView() {
        game = Game.of(getColumnCount());
        setOnTouchListener(new OnTouchListener() {
            private double startX, startY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        double offsetX = event.getX() - startX;
                        double offsetY = event.getY() - startY;

                        if (Math.sqrt(offsetX * offsetX + offsetY * offsetY) <= 10) {
                            return false;
                        }
                        Action action = null;
                        try {
                            double k = offsetY / offsetX;

                            if (k < 0.5 && k > -0.5) {
                                action = offsetX > 0 ? Action.RIGHT : Action.LEFT;
                            } else if (k > 2 || k < -2) {
                                action = offsetY < 0 ? Action.UP : Action.DOWN;
                            }
                        } catch (NumberFormatException ignored) {
                            action = offsetY < 0 ? Action.UP : Action.DOWN;
                        } finally {
                            if (action != null)
                                game.pushAction(action, score -> {
                                    var sum = score + Integer.parseInt(scoreboard.getText().toString());
                                    if (sum > 999999)
                                        sum = 999999;
                                    else if (sum > 99999)
                                        scoreboard.setTextSize(20);
                                    else if (sum > 9999)
                                        scoreboard.setTextSize(25);
                                    scoreboard.setText(String.valueOf(sum));
                                    if (score > 0)
                                        scoreboard.startAnimation(scoreboardAnimation);
                                });
                        }
                        view.performClick();
                        break;
                }
                return true;
            }
        });
        game.setGameStopHandler(() -> {
            game.engine().pause();
            ((Activity) getContext()).runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Game 2048")
                        .setMessage("Game End.\nYour score is " + scoreboard.getText())
                        .setPositiveButton("Restart Game", (dialog, which) -> {
                            game.initialize();
                            clearScoreBoard();
                        })
                        .setNegativeButton("Go To Menu", (dialog, which) ->
                                ((MainActivity) getContext()).goMenu(this));
                builder.create().show();
            });
        });
    }

    public void setScoreboard(@NonNull TextView scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void clearScoreBoard() {
        if (scoreboard != null)
            scoreboard.setText("0");
    }
}
