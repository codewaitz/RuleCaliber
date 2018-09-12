package com.rule.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 作者：created by YangKui
 * 日期：on 2018/9/10.11.
 * 说明：卡尺容器布局
 */

public class RuleHorizontalView extends HorizontalScrollView {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 卡尺View
     */
    private RuleView ruleView;
    /**
     * 滚动监听
     */
    private OnScrollListener onScrollListener = null;
    /**
     * 卡尺监听
     */
    private OnRuleListener onRuleListener = null;
    /**
     * 滚动Hander
     */
    private Handler handler;

    /**
     * 滚动状态:
     * IDLE=滚动停止
     * TOUCH_SCROLL=手指拖动滚动
     * FLING=滚动
     */
    enum ScrollType {
        IDLE, TOUCH_SCROLL, FLING
    }

    /**
     * 记录当前滚动的距离
     */
    private int currentX = -9999999;

    /**
     * 当前滚动状态
     */
    private ScrollType scrollType = ScrollType.IDLE;


    public RuleHorizontalView(Context context) {
        this(context, null);
    }

    public RuleHorizontalView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RuleHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RuleHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initialize(context); // 初始化
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initialize(Context context) {
        handler = new Handler();
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);
        setFillViewport(true);
        // 装载View数据
        ruleView = new RuleView(context);
        ruleView.relatedView(this);
        if (getChildCount() > 0) removeAllViews();
        addView(ruleView);
    }

    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (getScrollX() == currentX) {
                //滚动停止,取消监听线程
                scrollType = ScrollType.IDLE;
                fillListener();// 发起监听模式
                handler.removeCallbacks(this);
                return;
            } else {
                //手指离开屏幕,但是view还在滚动
                scrollType = ScrollType.FLING;
                fillListener();// 发起监听模式
            }
            currentX = getScrollX();
            //滚动监听间隔:milliseconds
            handler.postDelayed(this, 50);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                this.scrollType = ScrollType.TOUCH_SCROLL;
                fillListener();// 发起监听模式
                handler.removeCallbacks(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                handler.post(scrollRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 装载监听器
     */
    private void fillListener() {
        if (onScrollListener != null)
            onScrollListener.onScrollChanged(this, scrollType, getScrollX());
        if (ruleView != null && onRuleListener != null)
            onRuleListener.onRule(ruleView.getRuleValue(),
                    ruleView.getRuleTextValue());
    }

    /**
     * 获取卡尺View
     *
     * @return
     */
    public RuleView getRuleView() {
        return ruleView;
    }

    /**
     * 滚动到刻度值
     *
     * @param value
     * @param smooth
     */
    public void setRuleValue(int value, boolean smooth) {
        if (onScrollListener != null)
            onScrollListener.onScrollRuleValue(this, value, smooth);
        if (ruleView != null && onRuleListener != null)
            onRuleListener.onRule(ruleView.getRuleValue(),
                    ruleView.getRuleTextValue());
    }

    /***
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void onScrollChanged(RuleHorizontalView ruleHorizontalView, ScrollType scrollType, int
                scrollX);

        void onScrollRuleValue(RuleHorizontalView ruleHorizontalView, int value, boolean smooth);
    }

    /**
     * 设置滚动监听
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /***
     * 卡尺监听接口
     */
    public interface OnRuleListener {
        void onRule(int ruleValue, String ruleTextValue);
    }

    /**
     * 设置卡尺监听
     *
     * @param onRuleListener
     */
    public void setOnRuleListener(OnRuleListener onRuleListener) {
        this.onRuleListener = onRuleListener;
    }
}
