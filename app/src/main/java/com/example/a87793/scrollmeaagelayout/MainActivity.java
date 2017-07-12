package com.example.a87793.scrollmeaagelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.widget.Toast;

import com.songsong.scrollmessagelayout.ClickScrollMessageCallBack;
import com.songsong.scrollmessagelayout.ScrollMessageLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClickScrollMessageCallBack {
    private ScrollMessageLayout scroll_message;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        init();
        //initL();
    }
    private void initData(){
        list=new ArrayList<>();
        list.add("他们都说宋宋最帅了，我觉得他们是对的。");
        list.add("美女都说，钱才是他们的梦想，男人都是找来造钱的机器");
        list.add("世界这么大，我想出去找妹子，妹子在哪，快到哥哥碗里来。");
    }
    private void initL(){
        scroll_message=(ScrollMessageLayout)findViewById(R.id.scroll_message);
        scroll_message.new Builder(list)
                .setScrollAnimationContinueTime(300)
                .setScrollClickMessageCallBack(this)
                .setScrollInterpolator(new AnticipateInterpolator())
                .setScrollTextDrawable(R.mipmap.gonggao)
                .setScrollTextColor("#fffaaa")
                .setScrollTextSize(15)
                .setScrollTimeInterval(2000)
                .build();
    }

    private void init(){
        scroll_message=(ScrollMessageLayout)findViewById(R.id.scroll_message);
        scroll_message.setClickMessageCallBack(this);
        //设置点击回调监听，如果你想通过点击滚动消息然后做其他的事，那么你就的实现这个接口
        scroll_message.setTimeInterval(2000);
        //设置动画的播放间隔，默认3秒
        scroll_message.setAnimationContinueTime(1000);
        //设置动画播放持续时间，默认500毫秒
        scroll_message.setTextViewDrawable(R.mipmap.tree_tips);
        //设置动画滚动左边的图标，默认也是有的，可以不设置
        scroll_message.setTextSize(15);
        //设置text的大小，默认大小是16
        scroll_message.setTextColor("#adadad");
        //设置文字的颜色，默认的是白色
        scroll_message.setInterpolator(new AccelerateInterpolator());
        //设置动画播放的插值器，默认是DecelerateInterpolator（由快倒慢）,
        // 这些都是android系统给我们提供的，好像提供了6种吧，
        // 如果你觉得这几种都不符合你想要的效果，你也可以自己实现。
        scroll_message.setScrollMessageList(list);
        //最后一步设置list
    }

    @Override
    public void onClickScrollMessage(int messageIndex) {
        Toast.makeText(this,"你点击了List中的第 "+messageIndex+" 个数据！",Toast.LENGTH_LONG).show();
    }
}
