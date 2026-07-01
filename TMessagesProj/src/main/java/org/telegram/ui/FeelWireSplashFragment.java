package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.LayoutHelper;

public class FeelWireSplashFragment extends BaseFragment {

    private static final int COLOR_BG        = 0xFF0F0F1A;
    private static final int COLOR_ACCENT    = 0xFF8B5CF6;
    private static final int COLOR_ACCENT2   = 0xFF4A1FA8;
    private static final int COLOR_WHITE     = 0xFFE8E8F0;
    private static final int COLOR_SUBTEXT   = 0xFF6060A0;

    private FrameLayout rootView;
    private LogoView    logoView;
    private TextView    wordMarkView;
    private TextView    taglineView;
    private View        glowView;

    private boolean navigated = false;

    @Override
    public View createView(Context context) {
        actionBar.setAddToContainer(false);

        rootView = new FrameLayout(context);
        rootView.setBackgroundColor(COLOR_BG);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        glowView = new View(context) {
            private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            @Override
            protected void onDraw(Canvas canvas) {
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int r  = (int) AndroidUtilities.dp(160);
                glowPaint.setShader(new android.graphics.RadialGradient(
                        cx, cy - AndroidUtilities.dp(40), r,
                        new int[]{0x308B5CF6, 0x154A1FA8, 0x00000000},
                        new float[]{0f, 0.5f, 1f},
                        Shader.TileMode.CLAMP));
                canvas.drawRect(0, 0, getWidth(), getHeight(), glowPaint);
            }
        };
        glowView.setAlpha(0f);
        rootView.addView(glowView, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        logoView = new LogoView(context);
        logoView.setAlpha(0f);
        logoView.setScaleX(0.5f);
        logoView.setScaleY(0.5f);
        rootView.addView(logoView, LayoutHelper.createFrame(120, 120,
                Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, -90, 0, 0));

        wordMarkView = new TextView(context);
        wordMarkView.setText("FeelWire");
        wordMarkView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 38);
        wordMarkView.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        wordMarkView.setTextColor(COLOR_WHITE);
        wordMarkView.setLetterSpacing(0.04f);
        wordMarkView.setGravity(Gravity.CENTER);
        wordMarkView.setAlpha(0f);
        wordMarkView.setTranslationY(AndroidUtilities.dp(20));
        rootView.addView(wordMarkView, LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 55, 0, 0));

        taglineView = new TextView(context);
        taglineView.setText("Feel every message.");
        taglineView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        taglineView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        taglineView.setTextColor(COLOR_SUBTEXT);
        taglineView.setLetterSpacing(0.06f);
        taglineView.setGravity(Gravity.CENTER);
        taglineView.setAlpha(0f);
        rootView.addView(taglineView, LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 105, 0, 0));

        fragmentView = rootView;
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startSplashAnimation();
    }

    private void startSplashAnimation() {
        ValueAnimator glowAnim = ValueAnimator.ofFloat(0f, 1f);
        glowAnim.setDuration(800);
        glowAnim.setInterpolator(new DecelerateInterpolator());
        glowAnim.addUpdateListener(a -> {
            if (glowView != null) glowView.setAlpha((float) a.getAnimatedValue());
        });

        ValueAnimator logoAnim = ValueAnimator.ofFloat(0f, 1f);
        logoAnim.setDuration(700);
        logoAnim.setStartDelay(200);
        logoAnim.setInterpolator(new OvershootInterpolator(1.2f));
        logoAnim.addUpdateListener(a -> {
            float v = (float) a.getAnimatedValue();
            if (logoView != null) {
                logoView.setAlpha(v);
                logoView.setScaleX(0.5f + 0.5f * v);
                logoView.setScaleY(0.5f + 0.5f * v);
            }
        });

        ValueAnimator wordAnim = ValueAnimator.ofFloat(0f, 1f);
        wordAnim.setDuration(500);
        wordAnim.setStartDelay(600);
        wordAnim.setInterpolator(new DecelerateInterpolator());
        wordAnim.addUpdateListener(a -> {
            float v = (float) a.getAnimatedValue();
            if (wordMarkView != null) {
                wordMarkView.setAlpha(v);
                wordMarkView.setTranslationY(AndroidUtilities.dp(20) * (1f - v));
            }
        });

        ValueAnimator taglineAnim = ValueAnimator.ofFloat(0f, 1f);
        taglineAnim.setDuration(400);
        taglineAnim.setStartDelay(900);
        taglineAnim.setInterpolator(new DecelerateInterpolator());
        taglineAnim.addUpdateListener(a -> {
            if (taglineView != null) taglineView.setAlpha((float) a.getAnimatedValue());
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(glowAnim, logoAnim, wordAnim, taglineAnim);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!navigated && getParentActivity() != null) {
                        navigated = true;
                        navigateToIntro();
                    }
                }, 900);
            }
        });
        set.start();
    }

    private void navigateToIntro() {
        ValueAnimator fadeOut = ValueAnimator.ofFloat(1f, 0f);
        fadeOut.setDuration(350);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeOut.addUpdateListener(a -> {
            float v = (float) a.getAnimatedValue();
            if (rootView != null) rootView.setAlpha(v);
        });
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (getParentActivity() != null && !getParentActivity().isFinishing()) {
                    presentFragment(new IntroActivity(), true);
                }
            }
        });
        fadeOut.start();
    }

    @Override
    public boolean isSwipeBackEnabled() {
        return false;
    }

    private static class LogoView extends View {

        private final Paint bgPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint checkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint glowRing  = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect      = new RectF();
        private final Path  checkPath = new Path();
        private LinearGradient bgGradient;

        public LogoView(Context context) {
            super(context);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            bgGradient = new LinearGradient(
                    0, 0, w, h,
                    new int[]{0xFF9D78FF, 0xFF8B5CF6, 0xFF4A1FA8},
                    new float[]{0f, 0.45f, 1f},
                    Shader.TileMode.CLAMP);
            bgPaint.setShader(bgGradient);

            buildCheckPath(w, h);
        }

        private void buildCheckPath(int w, int h) {
            float cx  = w / 2f;
            float cy  = h / 2f;
            float sz  = w * 0.28f;

            checkPath.reset();
            checkPath.moveTo(cx - sz, cy);
            checkPath.lineTo(cx - sz * 0.2f, cy + sz * 0.8f);
            checkPath.lineTo(cx + sz, cy - sz * 0.7f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int w = getWidth();
            int h = getHeight();
            if (w == 0 || h == 0) return;

            float radius = w * 0.28f;

            glowRing.setColor(0x308B5CF6);
            glowRing.setStyle(Paint.Style.FILL);
            canvas.drawCircle(w / 2f, h / 2f, w * 0.52f, glowRing);

            rect.set(w * 0.1f, h * 0.1f, w * 0.9f, h * 0.9f);
            canvas.drawRoundRect(rect, radius, radius, bgPaint);

            checkPaint.setColor(Color.WHITE);
            checkPaint.setStyle(Paint.Style.STROKE);
            checkPaint.setStrokeWidth(w * 0.085f);
            checkPaint.setStrokeCap(Paint.Cap.ROUND);
            checkPaint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawPath(checkPath, checkPaint);
        }
    }
}
