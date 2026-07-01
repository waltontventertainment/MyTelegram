package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class VoIPEmojiReactionsView extends FrameLayout {

    private static final String[] REACTIONS = {"❤️", "😂", "👏", "🔥", "😮", "😍"};
    private static final int ACCENT = 0xFF8B5CF6;

    private final LinearLayout pickerRow;
    private boolean pickerVisible = false;
    private final FrameLayout floatContainer;
    private final List<FloatingEmoji> activeEmojis = new ArrayList<>();

    private TextView toggleBtn;

    public VoIPEmojiReactionsView(Context context) {
        super(context);
        setClipChildren(false);
        setClipToPadding(false);

        floatContainer = new FrameLayout(context);
        floatContainer.setClipChildren(false);
        floatContainer.setClipToPadding(false);
        addView(floatContainer, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        pickerRow = new LinearLayout(context);
        pickerRow.setOrientation(LinearLayout.HORIZONTAL);
        pickerRow.setBackgroundDrawable(buildPill(context));
        pickerRow.setPadding(
                AndroidUtilities.dp(10), AndroidUtilities.dp(6),
                AndroidUtilities.dp(10), AndroidUtilities.dp(6));
        pickerRow.setAlpha(0f);
        pickerRow.setScaleX(0.7f);
        pickerRow.setScaleY(0.7f);
        pickerRow.setVisibility(GONE);

        for (String emoji : REACTIONS) {
            TextView btn = new TextView(context);
            btn.setText(emoji);
            btn.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, 26);
            btn.setGravity(Gravity.CENTER);
            btn.setPadding(AndroidUtilities.dp(4), 0, AndroidUtilities.dp(4), 0);
            btn.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.75f).scaleY(0.75f).setDuration(80).start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.animate().scaleX(1f).scaleY(1f)
                            .setInterpolator(new OvershootInterpolator())
                            .setDuration(200).start();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        launchFloatingEmoji(emoji);
                    }
                }
                return true;
            });
            pickerRow.addView(btn, LayoutHelper.createLinear(
                    LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                    Gravity.CENTER_VERTICAL));
        }

        addView(pickerRow, LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0, 0, 120));

        toggleBtn = buildToggleButton(context);
        addView(toggleBtn, LayoutHelper.createFrame(44, 44,
                Gravity.BOTTOM | Gravity.LEFT, 16, 0, 0, 120));
    }

    private TextView buildToggleButton(Context context) {
        TextView btn = new TextView(context);
        btn.setText("😊");
        btn.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, 22);
        btn.setGravity(Gravity.CENTER);
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        bg.setColor(0x88000000);
        btn.setBackground(bg);
        btn.setOnClickListener(v -> togglePicker());
        return btn;
    }

    private android.graphics.drawable.GradientDrawable buildPill(Context context) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        gd.setCornerRadius(AndroidUtilities.dp(30));
        gd.setColor(0xCC0F0F1A);
        gd.setStroke(AndroidUtilities.dp(1), 0x558B5CF6);
        return gd;
    }

    private void togglePicker() {
        pickerVisible = !pickerVisible;
        if (pickerVisible) {
            pickerRow.setVisibility(VISIBLE);
            pickerRow.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(250)
                    .setInterpolator(new OvershootInterpolator(1.1f))
                    .start();
        } else {
            pickerRow.animate().alpha(0f).scaleX(0.7f).scaleY(0.7f)
                    .setDuration(180)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> pickerRow.setVisibility(GONE))
                    .start();
        }
    }

    private void launchFloatingEmoji(String emoji) {
        if (floatContainer.getHeight() == 0) return;

        int count = (int) (Math.random() * 2) + 1;
        for (int i = 0; i < count; i++) {
            final int delay = i * 120;
            postDelayed(() -> spawnOneEmoji(emoji), delay);
        }

        if (pickerVisible) togglePicker();
    }

    private void spawnOneEmoji(String emoji) {
        Context ctx = getContext();
        TextView floater = new TextView(ctx);
        floater.setText(emoji);
        floater.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, 34);
        floater.setGravity(Gravity.CENTER);

        float randomX = (float) (Math.random() * 0.6 + 0.2);
        int startX = (int) (floatContainer.getWidth() * randomX - AndroidUtilities.dp(20));
        int startY = floatContainer.getHeight() - AndroidUtilities.dp(180);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                AndroidUtilities.dp(44), AndroidUtilities.dp(44));
        lp.leftMargin = startX;
        lp.topMargin  = startY;
        floater.setLayoutParams(lp);
        floater.setAlpha(0f);
        floater.setScaleX(0.4f);
        floater.setScaleY(0.4f);
        floatContainer.addView(floater);

        FloatingEmoji entry = new FloatingEmoji(floater, startY);
        activeEmojis.add(entry);

        float targetY = startY - AndroidUtilities.dp(300 + (int) (Math.random() * 120));
        float wobble  = (float) (Math.random() * AndroidUtilities.dp(30) - AndroidUtilities.dp(15));

        floater.animate()
                .alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .start();

        ValueAnimator riseAnim = ValueAnimator.ofFloat(startY, targetY);
        riseAnim.setDuration(1800);
        riseAnim.setStartDelay(80);
        riseAnim.setInterpolator(new DecelerateInterpolator(1.2f));
        riseAnim.addUpdateListener(a -> {
            float val = (float) a.getAnimatedValue();
            float fraction = a.getAnimatedFraction();
            float sway = (float) Math.sin(fraction * Math.PI * 2.5) * wobble;
            if (floater.getParent() != null) {
                FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) floater.getLayoutParams();
                p.topMargin  = (int) val;
                p.leftMargin = startX + (int) sway;
                floater.setLayoutParams(p);
            }
        });
        riseAnim.start();

        floater.animate()
                .alpha(0f)
                .setDuration(500)
                .setStartDelay(1350)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(() -> {
                    if (floater.getParent() != null) floatContainer.removeView(floater);
                    activeEmojis.remove(entry);
                })
                .start();
    }

    private static class FloatingEmoji {
        final TextView view;
        final int startY;
        FloatingEmoji(TextView v, int y) { view = v; startY = y; }
    }
}
