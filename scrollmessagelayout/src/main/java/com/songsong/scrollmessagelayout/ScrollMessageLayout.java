package com.songsong.scrollmessagelayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 宋 宋
 * 实现消息轮播这么一个控件，这个控件的实现原理是在ViewFlipper的源码的基础上启发开发的。
 */
public class ScrollMessageLayout extends FrameLayout implements Animation.AnimationListener,
        View.OnClickListener{
    private int timeInterval = 3 * 1000;//默认时间的间隔
    private int animationContinueTime = 500;//默认动画持续时间
    private String textColor = "#FFFFFF";//默认字体颜色
    private int textSize = 16;//默认字体大小
    private Animation anim_out=null, anim_in=null;//动画
    private TextView view_out=null, view_in=null;//两个textView,一个进，一个出
    private List<String> listMessage=null; //需要轮播的消息集合
    private int currentMessageIndex = 0;//当前轮播到的消息索引
    private long lastTimeMillis ;
    private Drawable drawable= switchPictureToDrawable(R.drawable.gonggao); //左边的图片
    private Interpolator interpolator=new DecelerateInterpolator();//默认的插值器（先快后慢）
    private ClickScrollMessageCallBack clickMessageCallBack;


    /**
     * 设置回调
     * @param clickMessageCallBack 接口实例
     */
    public void setClickMessageCallBack(ClickScrollMessageCallBack clickMessageCallBack){
        this.clickMessageCallBack=clickMessageCallBack;
    }
    public ScrollMessageLayout(Context context) {
        super(context, null);
    }
    public ScrollMessageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        checkTimeAndUpdate();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View v) {
        if(null!=clickMessageCallBack) {
            int position=currentMessageIndex-2;
            clickMessageCallBack.onClickScrollMessage(position%3);
        }
    }

    private void initTextView(Context context) {
        if(null==view_out&&null==view_in) {
            view_out = createTextView(context);
            view_out.setOnClickListener(this);
            view_in = createTextView(context);
            view_in.setOnClickListener(this);
            addView(view_in);
            addView(view_out);
        }
    }

    private void initAnimation() {
        if(null==anim_out&&null==anim_in){
            anim_out = createAnimation(0, -1,interpolator);
            anim_in = createAnimation(1, 0,interpolator);
            anim_in.setAnimationListener(this);
        }
    }

    /**
     *  设置要循环播放的信息
     * @param list 滚动的数据集
     */
    public void setScrollMessageList(List<String> list) {
        if(null!=list&&!list.isEmpty()){
            initTextView(getContext());
            initAnimation();
            this.listMessage = list;
            currentMessageIndex =0;
            updateMessage(view_out);
            updateTextMessageAndAnim();
        }else{
            Log.e("list","list集合不能为空");
            throw new NullPointerException(" list is null");
        }
    }


    /**
     * 跟新textView的text以及播放动画
     */
    private void updateTextMessageAndAnim() {
        if (currentMessageIndex%2!=0) {
            updateMessage(view_in);
            view_out.startAnimation(anim_out);
            view_in.startAnimation(anim_in);
            this.bringChildToFront(view_in);
            this.updateViewLayout(view_in, view_in.getLayoutParams());
        } else {
            updateMessage(view_out);
            view_in.startAnimation(anim_out);
            view_out.startAnimation(anim_in);
            this.bringChildToFront(view_out);
            this.updateViewLayout(view_out, view_out.getLayoutParams());
        }
    }

    /**
     * 把系统当前的时间与上一次记录的时间进行对比，
     * 如果大于1s,我们就进行text更新和动画。
     */
    private void checkTimeAndUpdate() {
        if (System.currentTimeMillis() - lastTimeMillis < 1000 ) {
            return ;
        }
        lastTimeMillis = System.currentTimeMillis();
        updateTextMessageAndAnim();
    }

    /**
     * 跟新textView的显示信息
     * @param view TextView
     */
    private void updateMessage(TextView view) {
        view.setCompoundDrawables(drawable, null, null, null);
        String message = getNextScrollMessage();
        if(!TextUtils.isEmpty(message)) {
            view.setText(message);
        }
    }

    /**
     *  获取下一条消息
     * @return 返回下一条信息
     */
    private String getNextScrollMessage() {
        if (isListEmpty(listMessage))
            return null;
        return listMessage.get(currentMessageIndex++%listMessage.size());
    }

    /**
     * 创建一个TextView
     * @param context 上下文
     * @return 返回一个textView
     */
    private TextView createTextView(Context context){
        TextView textView = new TextView(context);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
        lp.setMargins(20,0,20,0);
        textView.setLayoutParams(lp);
        textView.setCompoundDrawablePadding(15);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor(textColor));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        return textView;
    }

    /**
     * 创建一个Animation实例，解释一下参数，因为我们在内部使用的是Animation.RELATIVE_TO_SELF
     * so 所以我们参数传值 是-1 ，0 ，1。其实这里的1就相当于100%了。
     * 很好理解，如果还是理解不了，就去翻API
     * @param fromY Y轴的起点值
     * @param toY Y轴的终点值
     * @param <T> 插值器
     * @return 返回一个animation实例
     */
    private <T> Animation createAnimation(float fromY, float toY,T interpolator) {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,fromY,Animation.RELATIVE_TO_SELF, toY);
        anim.setDuration(animationContinueTime);
        anim.setStartOffset(timeInterval);
        anim.setInterpolator((Interpolator) interpolator);
        return anim;
    }

    /**
     *  将资源图片转换为Drawable对象
     * @param ResId 资源
     * @return 返回一个drawable对象
     */
    private Drawable switchPictureToDrawable(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() , drawable.getMinimumHeight());
        return drawable;
    }

    /**
     *  list是否为空
     * @param list 数据集
     * @return true 如果是空
     */
    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 设置滚动文字的大小
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize){
        this.textSize=textSize;
    }

    /**
     * 设置滚动文字的颜色
     * @param textColor 文字颜色
     */
    public void setTextColor(String textColor){
        this.textColor=textColor;
    }
    /**
     *设置动画时间间隔
     * @param timeInterval 设置动画时间间隔
     */
    public void setTimeInterval(int timeInterval){
        this.timeInterval=timeInterval;
    }

    /**
     * 设置动画的持续是时间
     * @param continueTime 动画持续时间
     */
    public void setAnimationContinueTime(int continueTime){
       this.animationContinueTime=continueTime;
    }

    /**
     * 设置左边的图片
     * @param resId 资源id
     */
    public void setTextViewDrawable(int resId){
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() , drawable.getMinimumHeight());
       this.drawable=drawable;
    }

    /**
     * 给动画设置插值其
     * @param interpolator 插值器
     */
    public void setInterpolator(Interpolator interpolator){
        this.interpolator=interpolator;
    }

    /**
     * 这里通过创建者模式，做了个链式调用
     */
    public class Builder {
        private List<String> list;//滚动的数据集
        public Builder(List<String> list){
            this.list=list;
        }

        /**
         * 设置textSize
         * @param textSize text大小
         */
        public  Builder setScrollTextSize(int textSize){
            setTextSize(textSize);
            return this;
        }

        /**
         * 设置颜色
         * @param strColor 颜色值
         */
        public Builder setScrollTextColor(String strColor){
            setTextColor(strColor);
            return this;
        }

        /**
         * 设置TextView drawable
         * @param resId 资源id
         */
        public Builder setScrollTextDrawable(int resId){
            setTextViewDrawable(resId);
            return this;
        }

        /**
         * 设置动画的时间间隔
         * @param timeInterval 间隔值
         */
        public Builder setScrollTimeInterval(int timeInterval){
            setTimeInterval(timeInterval);
            return this;
        }

        /**
         * 设置动画的持续时间
         * @param continueTime 时间值
         */
        public Builder setScrollAnimationContinueTime(int continueTime){
            setAnimationContinueTime(continueTime);
            return this;
        }

        /**
         * 设置动画的插值器
         * @param interpolator
         */
        public Builder setScrollInterpolator(Interpolator interpolator){
            setInterpolator(interpolator);
            return this;
        }

        /**
         * 设置回调监听
         * @param scrollMessageCallBack 点击回调
         * @return
         */
        public Builder setScrollClickMessageCallBack(ClickScrollMessageCallBack scrollMessageCallBack){
            setClickMessageCallBack(scrollMessageCallBack);
            return this;
        }
        
        /**
         * 最终构建
         */
        public void build(){
            setScrollMessageList(list);
        }
    }

}