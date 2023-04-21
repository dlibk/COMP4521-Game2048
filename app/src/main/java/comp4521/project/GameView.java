package comp4521.project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import comp4521.project.game.action.Action;
import comp4521.project.game.mode.Mode;
import comp4521.project.utils.GameShouldStopException;

public class GameView extends GridLayout {

    public final List<Card> cards = new ArrayList<>();

    public final Game game = GameFactory.createGame(Mode.CLASSIC, 4);

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
        setOnTouchListener(new OnTouchListener() {
            private double startX, startY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        System.out.println("On Action DOWN");
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
                                try {
                                    game.pushAction(action);
                                } catch (GameShouldStopException ignored) {
                                    gameStop();
                                }
                            System.out.println(action);
                        }
                        view.performClick();
                        break;
                }
                return true;
            }
        });

        game.setOnMapUpdate((gameMap) -> {
            List<Integer> values = game.getFlattenMap();
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) < 0) {
                    cards.get(i).setInvisible();
                } else {
                    cards.get(i).setValue(values.get(i));
                }
            }
        });
    }

    public void addCard(@NonNull Card card) {
        cards.add(card);
    }

    private void gameStop() {
        setOnTouchListener(null);
        throw new RuntimeException("Game Stopped!");
    }

    public void refreshGame() {
        game.updateMap();
    }
}
