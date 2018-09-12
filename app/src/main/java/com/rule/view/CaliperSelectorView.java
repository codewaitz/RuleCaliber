package com.rule.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作者：created by YangKui
 * 日期：on 2018/9/11.16.
 * 说明：
 */

public class CaliperSelectorView extends LinearLayout implements RuleHorizontalView.OnRuleListener {
    /**
     * 当前刻度值
     */
    private int curRuleValue;
    /**
     * 当前刻度文本值
     */
    private String curRuleTextValue;

    /**
     * 卡尺组件
     */
    private CaliperView caliperView;
    /**
     * 卡尺文本
     */
    private TextView textCaliperView;

    public CaliperSelectorView(Context context) {
        this(context, null);
    }

    public CaliperSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaliperSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CaliperSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context); // 初始化
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initialize(Context context) {
        curRuleValue = 10000;
        curRuleTextValue = "10,000";
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
        if (getChildCount() > 0) removeAllViews();
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT);
        // 装载标题
        TextView textTitle = new TextView(context);
        textTitle.setTextColor(Color.parseColor("#464646"));
        textTitle.setTextSize(14f);
        textTitle.setText("投资金额");
        lp.topMargin = dip2px(context, 10);
        textTitle.setLayoutParams(lp);
        textTitle.setGravity(Gravity.CENTER);
        addView(textTitle);
        // 装载文字
        textCaliperView = new TextView(context);
        textCaliperView.setTextColor(Color.parseColor("#464646"));
        textCaliperView.setTextSize(24f);
        textCaliperView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textCaliperView.setText(curRuleTextValue);
        lp.topMargin = dip2px(context, 10);
        textCaliperView.setLayoutParams(lp);
        textCaliperView.setGravity(Gravity.CENTER);
        addView(textCaliperView);
        // 装载卡尺
        caliperView = new CaliperView(context);
        lp.topMargin = dip2px(context, 5);
        caliperView.setLayoutParams(lp);
        caliperView.setOnRuleListener(this);
        addView(caliperView);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onRule(int ruleValue, String ruleTextValue) {
        curRuleValue = ruleValue;
        curRuleTextValue = ruleTextValue;
        if (textCaliperView != null) {
            textCaliperView.setText(ruleTextValue);
        }
    }

    /**
     * 滚动到刻度值
     *
     * @param value
     * @param smooth
     */
    public void scrollToRuleValue(int value, boolean smooth) {
        if (caliperView != null) caliperView.scrollToRuleValue(value, smooth);
    }

    /**
     * 获取当前刻度值
     *
     * @return
     */
    public int getRuleValue() {
        return curRuleValue;
    }

    /**
     * 获取当前刻度文本值
     *
     * @return
     */
    public String getRuleTextValue() {
        return curRuleTextValue;
    }
}
