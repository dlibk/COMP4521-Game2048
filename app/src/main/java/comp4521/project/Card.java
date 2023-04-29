package comp4521.project;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Card extends CardView {

    public static final Map<Integer, Integer> colorSource = new HashMap<>();

    public static final Animation synthesisAnimation = new ScaleAnimation(0.9f, 1.1f, 0.9f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public static final Animation generateAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public static final Animation disappearAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public static final Animation appearAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    static {
        colorSource.put(0, R.color._0);
        colorSource.put(2, R.color._2);
        colorSource.put(4, R.color._4);
        colorSource.put(8, R.color._8);
        colorSource.put(16, R.color._16);
        colorSource.put(32, R.color._32);
        colorSource.put(64, R.color._64);
        colorSource.put(128, R.color._128);
        colorSource.put(256, R.color._256);

        synthesisAnimation.setDuration(150);
        generateAnimation.setDuration(150);
        disappearAnimation.setDuration(100);
        appearAnimation.setDuration(100);
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

    private TextView textView = null;

    private int getColorFromResource(int value) {
        int id = Objects.requireNonNull(Card.colorSource.getOrDefault(value, R.color._default));
        return getResources().getColor(id, getContext().getTheme());
    }

    public void setValue(int value) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (textView == null)
                textView = (TextView) getChildAt(0);
            if (value >= 0) {
                textView.setText(String.valueOf(value));
                textView.setTextSize(getAppropriateTextSize(value));
                setCardBackgroundColor(getColorFromResource(value));
                textView.setVisibility(VISIBLE);
                startAnimation(appearAnimation);
            } else {
                startAnimation(disappearAnimation);
                textView.setVisibility(GONE);
                setCardBackgroundColor(getResources().getColor(R.color.semitransparent, getContext().getTheme()));
            }
        });
    }

    public void freeze() {
        Context context = getContext();
        ((Activity) context).runOnUiThread(() -> {
            if (textView == null)
                textView = (TextView) getChildAt(0);
            setCardBackgroundColor(getResources().getColor(R.color.gray_400, context.getTheme()));
            textView.setTextColor(getResources().getColor(R.color.black, context.getTheme()));
            startAnimation(appearAnimation);
        });
    }

    public void unfreeze() {
        Context context = getContext();
        ((Activity) context).runOnUiThread(() -> {
            if (textView == null)
                textView = (TextView) getChildAt(0);
            textView.setTextColor(getResources().getColor(R.color.white, context.getTheme()));
            String txt = textView.getText().toString();
            if (txt.isEmpty()) {
                startAnimation(disappearAnimation);
                textView.setVisibility(GONE);
                setCardBackgroundColor(getResources().getColor(R.color.semitransparent, context.getTheme()));
            } else {
                int value = Integer.parseInt(txt);
                setCardBackgroundColor(getColorFromResource(value));
                textView.setVisibility(VISIBLE);
                startAnimation(appearAnimation);
            }
        });
    }

    private int getAppropriateTextSize(int value) {
        Context context = getContext();
        if (context instanceof MainActivity) {
            return value >= 1000 ? 30 : 45;
        } else if (context instanceof Map5Activity) {
            if (value < 100)
                return 45;
            else if (value >= 1000)
                return 25;
            else
                return 30;
        } else if (context instanceof Map6Activity) {
            if (value < 100)
                return 40;
            else if (value >= 1000)
                return 20;
            else
                return 25;
        } else {
            return 45;
        }
    }
}
