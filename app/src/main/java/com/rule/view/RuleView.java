package com.rule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 作者：created by YangKui
 * 日期：on 2018/9/7.16.
 * 说明：（金额）卡尺
 */

public class RuleView extends View implements RuleHorizontalView.OnScrollListener {
    // 上下文
    private Context context;
    // 自定义相关数据
    private float leftMargin = 540;//距离左边距离
    private float rightMargin = 540;//距离右边边距离
    private float topMargin = 100; // 距离顶部距离
    private float bottomMargin = 50;// 距离底部距离
    private int minRuleValue = 10000; // 最小刻度值
    private int maxRuleValue = 1000000;// 最大刻度值
    private int spaceRuleValue = 1000;// 刻度间隔值
    private float textRuleSpace = 15;// 文本和刻度间隔
    private float ruleWidth = 2; // 刻度宽度
    private int maxRuleCount = 10;// 一个大刻度包含刻度数
    private float maxRuleHeight = 40;//大刻度高度
    private float minRuleSpaceWidth = 28;// 小刻度间隔宽度
    private float minRuleHeight = 25; // 小刻度高度
    private float ruleLineHeight = 2;//刻度线高度
    private float textSize = 50f;//文本字体大小
    private int textColor = Color.parseColor("#5E5E5E");// 文本颜色
    private int ruleColor = Color.parseColor("#5E5E5E");// 刻度尺颜色
    // 得到相关尺寸数据
    private float maxRuleSpaceWidth;// 大刻度间隔宽度
    private String[] texts;// 刻度刻度文本信息
    private int sumMaxRule; // 大刻度的个数
    private float textHeight;//获取文本的高度
    private float viewWidth;//View的宽度
    private float viewHeight;//View的高度
    // 绘制工具
    private Paint paint;// 画笔
    private int ruleValue;//卡尺值

    public RuleView(Context context) {
        this(context, null);
    }

    public RuleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RuleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RuleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initPaint();//初始化画笔
        calculate();// 数据计算
    }

    /***
     * 关系RuleHorizontalView
     * @param ruleHorizontalView
     */
    public void relatedView(RuleHorizontalView ruleHorizontalView) {
        ruleHorizontalView.setOnScrollListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) Math.ceil(viewWidth), (int) Math.ceil(viewHeight));
    }

    /****
     * 初始化画笔Paint
     */
    private void initPaint() {
        paint = new Paint();
        //设置抗锯齿，如果不设置，加载位图的时候可能会出现锯齿状的边界，如果设置，边界就会变的稍微有点模糊，锯齿就看不到了。
        paint.setAntiAlias(true);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        paint.setTextAlign(Paint.Align.LEFT);
        // 获取文本高度
        textHeight = getTextHeight("520,1314", paint);
    }

    /***
     * 计算相关数据值
     */
    private void calculate() {
        // 两边距离，做一像素的适配
        leftMargin = getScreenWidth() / 2;
        rightMargin = leftMargin;
        // 大刻度间隔宽度
        maxRuleSpaceWidth = (ruleWidth + minRuleSpaceWidth) * maxRuleCount;
        // 计算文本数据
        sumMaxRule = maxRuleValue / (spaceRuleValue * maxRuleCount);
        texts = new String[sumMaxRule];
        for (int i = 0, count = minRuleValue; i < sumMaxRule; i++) {
            // 设置文本数据
            texts[i] = formatDouble(count);
            count += spaceRuleValue * maxRuleCount;
        }
        // View宽高计算
        viewWidth = sumMaxRule * maxRuleSpaceWidth + leftMargin + rightMargin - maxRuleSpaceWidth;
        viewHeight = textHeight + textRuleSpace + maxRuleHeight + topMargin + ruleLineHeight + bottomMargin;
        //初始化默认值
        ruleValue = minRuleValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 如果没有刻度就不用画
        if (paint == null || sumMaxRule < 1) return;
        // 画View
        for (int i = 0; i < sumMaxRule; i++) {
            drawRuleText(i, canvas, paint);//画刻度值
            drawRule(i, canvas, paint); // 画刻度
        }
        drawLine(canvas, paint); // 画整条基准线
        super.onDraw(canvas);
    }

    /**
     * 画刻度文本值
     *
     * @param index
     * @param canvas
     * @param paint
     */
    private void drawRuleText(int index, Canvas canvas, Paint paint) {
        if (canvas == null || paint == null) return;
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        float x = index * maxRuleSpaceWidth + leftMargin - getTextWidth
                (texts[index], paint) / 2;
        float y = textHeight + topMargin;
        canvas.drawText(texts[index], x, y, paint);
    }

    /**
     * 画刻度
     *
     * @param index
     * @param canvas
     * @param paint
     */
    private void drawRule(int index, Canvas canvas, Paint paint) {
        if (canvas == null || paint == null) return;
        //画大刻度
        paint.setColor(ruleColor);
        paint.setStrokeWidth(ruleWidth);
        float maxX = index * maxRuleSpaceWidth + leftMargin;
        float maxY = topMargin + textHeight + textRuleSpace;
        canvas.drawLine(maxX, maxY, maxX, maxY + maxRuleHeight, paint);
        //画小刻度
        if (index < sumMaxRule - 1) {
            float minY = maxY + maxRuleHeight - minRuleHeight;
            for (int i = 1; i < maxRuleCount; i++) {
                float minX = maxX + minRuleSpaceWidth * i + ruleWidth * i;
                canvas.drawLine(minX, minY, minX, minY + minRuleHeight, paint);
            }
        }
    }

    /**
     * 画整条基准线
     *
     * @param canvas
     * @param paint
     */
    private void drawLine(Canvas canvas, Paint paint) {
        if (canvas == null || paint == null) return;
        paint.setColor(ruleColor);
        paint.setStrokeWidth(ruleLineHeight);
        float lineY = textHeight + textRuleSpace + maxRuleHeight + topMargin;
        canvas.drawLine(leftMargin, lineY, viewWidth - rightMargin, lineY, paint);
    }

    /**
     * 获取屏幕宽度
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels; // 屏幕宽度（像素）
    }

    /**
     * 转换double为千分位
     *
     * @param value
     * @return
     */
    private String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat(",###,##0"); //没有小数
        return df.format(new BigDecimal(value));
    }


    /***
     * 去掉，的字符串，转int值
     * @param stringValue
     * @return
     */
    private int formatString2Int(String stringValue) {
        if (stringValue == null || "".equals(stringValue)) return 0;
        stringValue = stringValue.replaceAll(",", "");
        return Integer.parseInt(stringValue);
    }

    /**
     * 通过画笔获取文本的宽度
     *
     * @param text
     * @param paint
     * @return
     */
    private int getTextWidth(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * 通过画笔获取文本的高度
     *
     * @param text
     * @param paint
     * @return
     */
    private int getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 获取卡尺数值
     *
     * @return
     */
    public int getRuleValue() {
        return ruleValue;
    }

    /**
     * 获取卡尺文本值
     *
     * @return
     */
    public String getRuleTextValue() {
        return formatDouble(ruleValue);
    }

    /**
     * 滚动器监听处理
     *
     * @param scrollType
     * @param scrollX
     */
    @Override
    public void onScrollChanged(RuleHorizontalView ruleHorizontalView, RuleHorizontalView.ScrollType scrollType, int scrollX) {
        try {
            if (ruleHorizontalView == null) return;
            scrollX = scrollX < 0 ? 0 : scrollX; // 去掉无用值
            float oneRuleWidth = minRuleSpaceWidth + ruleWidth; // 单个刻度的宽度
            double countRuleWidth = scrollX / oneRuleWidth;// 计算滚动多少个刻度宽
            // 当前刻度不准确，需进行矫正
            if (scrollX % oneRuleWidth != 0) {
                if (Math.abs(Math.floor(countRuleWidth) - countRuleWidth) < Math.abs(Math.ceil
                        (countRuleWidth) - countRuleWidth)) {
                    scrollX = (int) (Math.floor(countRuleWidth) * oneRuleWidth);
                    countRuleWidth = Math.floor(countRuleWidth);
                } else {
                    scrollX = (int) (Math.ceil(countRuleWidth) * oneRuleWidth);
                    countRuleWidth = Math.ceil(countRuleWidth);
                }
            }
            // 滚动值转换到刻度值
            if (Math.abs(countRuleWidth) / maxRuleCount < sumMaxRule - 1) {
                int dix = (int) Math.abs(countRuleWidth) / maxRuleCount;
                int lieValue = (int) Math.abs(dix * maxRuleCount - countRuleWidth);
                ruleValue = formatString2Int(texts[dix]) + lieValue * spaceRuleValue;
            } else {
                // 对刻度最大限度处理
                ruleValue = maxRuleValue;
                scrollX = (int) maxRuleSpaceWidth * (sumMaxRule - 1);
            }
            //滚动停止, View微调
            if (scrollType == RuleHorizontalView.ScrollType.IDLE) {
                // 滚动到相应的刻度值上
                ruleHorizontalView.smoothScrollTo(scrollX, 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 滚动到刻度值
     *
     * @param ruleHorizontalView
     * @param value
     * @param smooth
     */
    @Override
    public void onScrollRuleValue(RuleHorizontalView ruleHorizontalView, int value, boolean
            smooth) {
        try {
            if (ruleHorizontalView == null) return;
            // 去掉无用值
            value = value < minRuleValue ? minRuleValue : value > maxRuleValue ? maxRuleValue : value;
            // 赋值当前得到的刻度值
            ruleValue = value;
            // 单个刻度的宽度
            float oneRuleWidth = minRuleSpaceWidth + ruleWidth;
            int maxCountRule = value / (spaceRuleValue * maxRuleCount);
            double minCountRule = Math.abs((double) value / (spaceRuleValue * maxRuleCount) -
                    maxCountRule) * maxRuleCount;
            // 边界值处理
            maxCountRule--;
            maxCountRule = maxCountRule < 0 ? 0 : maxCountRule;
            minCountRule = minCountRule < 0 ? 0 : minCountRule;
            // 滚动处理
            int scrollX = (int) (maxCountRule * maxRuleSpaceWidth + oneRuleWidth * minCountRule);// 滚动距离
            if (smooth) {
                ruleHorizontalView.smoothScrollTo(scrollX, 0);
            } else {
                ruleHorizontalView.scrollTo(scrollX, 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
