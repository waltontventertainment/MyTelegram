package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class VoIPDoodleOverlayView extends FrameLayout {

    private static final int[] COLORS = {
            0xFFFFFFFF, 0xFF8B5CF6, 0xFFFF4FBE, 0xFFFFE45E, 0xFF3BFFB0, 0xFFFF5454
    };
    private static final float[] SIZES = {4f, 8f, 14f};

    private final DrawCanvas drawCanvas;
    private final FrameLayout toolbar;
    private final TextView doodleToggleBtn;

    private boolean doodleActive = false;
    private int selectedColor = COLORS[0];
    private float selectedSize = SIZES[1];

    public VoIPDoodleOverlayView(Context context) {
        super(context);
        setClipChildren(false);

        drawCanvas = new DrawCanvas(context);
        drawCanvas.setVisibility(GONE);
        addView(drawCanvas, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        toolbar = buildToolbar(context);
        toolbar.setVisibility(GONE);
        addView(toolbar, LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 48, 0, 0));

        doodleToggleBtn = buildToggleButton(context);
        addView(doodleToggleBtn, LayoutHelper.createFrame(44, 44,
                Gravity.BOTTOM | Gravity.RIGHT, 0, 0, 16, 120));
    }

    private TextView buildToggleButton(Context context) {
        TextView btn = new TextView(context);
        btn.setText("✏️");
        btn.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, 20);
        btn.setGravity(Gravity.CENTER);
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        bg.setColor(0x88000000);
        btn.setBackground(bg);
        btn.setOnClickListener(v -> toggleDoodle());
        return btn;
    }

    private FrameLayout buildToolbar(Context context) {
        FrameLayout bar = new FrameLayout(context);
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setCornerRadius(AndroidUtilities.dp(24));
        bg.setColor(0xCC0F0F1A);
        bg.setStroke(AndroidUtilities.dp(1), 0x558B5CF6);
        bar.setBackground(bg);
        bar.setPadding(AndroidUtilities.dp(10), AndroidUtilities.dp(8),
                AndroidUtilities.dp(10), AndroidUtilities.dp(8));

        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        for (int color : COLORS) {
            View dot = new View(context);
            android.graphics.drawable.GradientDrawable dotBg = new android.graphics.drawable.GradientDrawable();
            dotBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            dotBg.setColor(color);
            if (color == COLORS[0]) {
                dotBg.setStroke(AndroidUtilities.dp(2), 0xFF8B5CF6);
            }
            dot.setBackground(dotBg);
            final int c = color;
            dot.setOnClickListener(v -> {
                selectedColor = c;
                drawCanvas.setColor(c);
                updateColorSelection(row, c);
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    AndroidUtilities.dp(22), AndroidUtilities.dp(22));
            lp.setMargins(AndroidUtilities.dp(3), 0, AndroidUtilities.dp(3), 0);
            row.addView(dot, lp);
        }

        View divider = new View(context);
        divider.setBackgroundColor(0x40FFFFFF);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(
                AndroidUtilities.dp(1), AndroidUtilities.dp(28));
        dlp.setMargins(AndroidUtilities.dp(6), 0, AndroidUtilities.dp(6), 0);
        row.addView(divider, dlp);

        for (float size : SIZES) {
            View sizeBtn = new View(context);
            android.graphics.drawable.GradientDrawable sizeBg = new android.graphics.drawable.GradientDrawable();
            sizeBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            sizeBg.setColor(0xFFE8E8F0);
            sizeBtn.setBackground(sizeBg);
            final float s = size;
            sizeBtn.setOnClickListener(v -> {
                selectedSize = s;
                drawCanvas.setStrokeSize(AndroidUtilities.dp(s));
            });
            int btnPx = (int) AndroidUtilities.dp(size + 8);
            LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(btnPx, btnPx);
            slp.setMargins(AndroidUtilities.dp(3), 0, AndroidUtilities.dp(3), 0);
            slp.gravity = Gravity.CENTER_VERTICAL;
            row.addView(sizeBtn, slp);
        }

        View divider2 = new View(context);
        divider2.setBackgroundColor(0x40FFFFFF);
        LinearLayout.LayoutParams dl2 = new LinearLayout.LayoutParams(
                AndroidUtilities.dp(1), AndroidUtilities.dp(28));
        dl2.setMargins(AndroidUtilities.dp(6), 0, AndroidUtilities.dp(6), 0);
        row.addView(divider2, dl2);

        TextView clearBtn = new TextView(context);
        clearBtn.setText("🗑");
        clearBtn.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, 18);
        clearBtn.setGravity(Gravity.CENTER);
        clearBtn.setOnClickListener(v -> drawCanvas.clearAll());
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                AndroidUtilities.dp(32), AndroidUtilities.dp(32));
        clp.setMargins(AndroidUtilities.dp(2), 0, AndroidUtilities.dp(2), 0);
        row.addView(clearBtn, clp);

        bar.addView(row, LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        return bar;
    }

    private void updateColorSelection(LinearLayout row, int selectedC) {
        for (int i = 0; i < row.getChildCount(); i++) {
            View child = row.getChildAt(i);
            if (child.getBackground() instanceof android.graphics.drawable.GradientDrawable) {
                android.graphics.drawable.GradientDrawable gd =
                        (android.graphics.drawable.GradientDrawable) child.getBackground();
                if (i < COLORS.length) {
                    gd.setStroke(COLORS[i] == selectedC ? AndroidUtilities.dp(2) : 0,
                            COLORS[i] == selectedC ? 0xFF8B5CF6 : Color.TRANSPARENT);
                }
            }
        }
    }

    private void toggleDoodle() {
        doodleActive = !doodleActive;
        if (doodleActive) {
            drawCanvas.setVisibility(VISIBLE);
            drawCanvas.setAlpha(0f);
            drawCanvas.animate().alpha(1f).setDuration(200).start();
            toolbar.setVisibility(VISIBLE);
            toolbar.setAlpha(0f);
            toolbar.setScaleX(0.8f);
            toolbar.setScaleY(0.8f);
            toolbar.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(250)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
            doodleToggleBtn.setText("✅");
        } else {
            drawCanvas.animate().alpha(0f).setDuration(200)
                    .withEndAction(() -> drawCanvas.setVisibility(GONE)).start();
            toolbar.animate().alpha(0f).setDuration(180)
                    .withEndAction(() -> toolbar.setVisibility(GONE)).start();
            doodleToggleBtn.setText("✏️");
        }
    }

    public boolean isDoodleActive() { return doodleActive; }

    private static class DrawCanvas extends View {

        private final List<Stroke> strokes = new ArrayList<>();
        private Stroke currentStroke;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private int strokeColor = Color.WHITE;
        private float strokeWidth = AndroidUtilities.dp(8);

        DrawCanvas(Context context) {
            super(context);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            setWillNotDraw(false);
        }

        void setColor(int color) { strokeColor = color; }
        void setStrokeSize(float px) { strokeWidth = px; }

        void clearAll() {
            strokes.clear();
            currentStroke = null;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    currentStroke = new Stroke(strokeColor, strokeWidth);
                    currentStroke.path.moveTo(x, y);
                    currentStroke.lastX = x;
                    currentStroke.lastY = y;
                    strokes.add(currentStroke);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (currentStroke != null) {
                        float mx = (x + currentStroke.lastX) / 2f;
                        float my = (y + currentStroke.lastY) / 2f;
                        currentStroke.path.quadTo(currentStroke.lastX, currentStroke.lastY, mx, my);
                        currentStroke.lastX = x;
                        currentStroke.lastY = y;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (currentStroke != null) {
                        currentStroke.path.lineTo(x, y);
                        currentStroke = null;
                        invalidate();
                    }
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (Stroke s : strokes) {
                paint.setColor(s.color);
                paint.setStrokeWidth(s.width);
                canvas.drawPath(s.path, paint);
            }
            if (currentStroke != null) {
                paint.setColor(currentStroke.color);
                paint.setStrokeWidth(currentStroke.width);
                canvas.drawPath(currentStroke.path, paint);
            }
        }

        private static class Stroke {
            final Path path = new Path();
            final int color;
            final float width;
            float lastX, lastY;
            Stroke(int c, float w) { color = c; width = w; }
        }
    }
}
