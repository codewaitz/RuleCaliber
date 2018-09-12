package com.rule.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 作者：created by YangKui
 * 日期：on 2018/9/11.14.
 * 说明：卡尺组件View
 */

public class CaliperView extends FrameLayout {
    /**
     * 卡尺容器
     */
    private RuleHorizontalView ruleHorizontalView;
    /**
     * 指示器
     */
    private View cursorView;

    public CaliperView(Context context) {
        this(context, null);
    }

    public CaliperView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaliperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CaliperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context); // 初始化
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initialize(Context context) {
        // 装载View数据
        if (getChildCount() > 0) removeAllViews();
        ruleHorizontalView = new RuleHorizontalView(context);
        addView(ruleHorizontalView);
        // 装载指示器
        final int width = dip2px(context, 2), height = dip2px(context, 50); // 默认宽高度
        cursorView = new View(context);
        LayoutParams lp = new LayoutParams(width, height);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        cursorView.setBackgroundColor(Color.parseColor("#508CEE"));
        cursorView.setLayoutParams(lp);
        addView(cursorView);
    }

    /**
     * 获取卡尺容器
     *
     * @return
     */
    public RuleHorizontalView getRuleHorizontalView() {
        return ruleHorizontalView;
    }

    /**
     * 设置指示器颜色
     *
     * @param color
     */
    public void setCursorColor(int color) {
        if (cursorView != null) cursorView.setBackgroundColor(color);
    }

    /**
     * 设置指示器宽度
     *
     * @param width
     */
    public void setCursorWidth(int width) {
        if (cursorView != null) {
            LayoutParams lp = (LayoutParams) cursorView.getLayoutParams();
            lp.width = width;
            cursorView.setLayoutParams(lp);
        }
    }

    /**
     * 设置指示器高度
     *
     * @param height
     */
    public void setCursorHeight(int height) {
        if (cursorView != null) {
            LayoutParams lp = (LayoutParams) cursorView.getLayoutParams();
            lp.height = height;
            cursorView.setLayoutParams(lp);
        }
    }

    /**
     * 设置卡尺监听
     *
     * @param onRuleListener
     */
    public void setOnRuleListener(RuleHorizontalView.OnRuleListener onRuleListener) {
        if (ruleHorizontalView != null) ruleHorizontalView.setOnRuleListener(onRuleListener);
    }

    /**
     * 滚动到刻度值
     *
     * @param value
     * @param smooth
     */
    public void scrollToRuleValue(int value, boolean smooth) {
        if (ruleHorizontalView != null) ruleHorizontalView.setRuleValue(value, smooth);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
