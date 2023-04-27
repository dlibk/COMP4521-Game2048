package comp4521.project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Card extends CardView {

    public static final Map<Integer, Integer> colorSource = new HashMap<>();

    public static final Animation synthesisAnimation = new ScaleAnimation(0.9f, 1.1f, 0.9f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public static final Animation generateAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    static {
        colorSource.put(0, R.color._0);
        colorSource.put(2, R.color._2);
        colorSource.put(4, R.color._4);
        colorSource.put(8, R.color._8);
        colorSource.put(16, R.color._16);
        colorSource.put(32, R.color._32);
        colorSource.put(64, R.color._64);

        synthesisAnimation.setDuration(150);
        generateAnimation.setDuration(150);
    }


    public Card(@NonNull Context context) {
        super(context);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Card(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public TextView textView = null;

    public void initialize() {
        assert getChildCount() > 0;
        textView = (TextView) getChildAt(0);
    }

    private int getColorFromResource(int value) {
        int id = Objects.requireNonNull(Card.colorSource.getOrDefault(value, R.color._default));
        return getResources().getColor(id, getContext().getTheme());
    }

    public void setValue(int value) {
        if (value >= 0) {
            textView.setText(String.valueOf(value));

            if (value >= 1000) {
                textView.setTextSize(30);
            } else if (value >= 100){
                textView.setTextSize(45);
            } else {
                textView.setTextSize(45);
            }
            setCardBackgroundColor(getColorFromResource(value));
            textView.setVisibility(VISIBLE);
        } else {
            setInvisible();
        }
    }

    public void setInvisible() {
        textView.setVisibility(GONE);
        setCardBackgroundColor(getResources().getColor(R.color.semitransparent, getContext().getTheme()));
    }
}
