# ScrollMessageLayout
这是一个非常简单的控件库，实现的是类似京东和超级课程表里面消息滚动的效果。因为公司也有这样的需求，刚开始就简单的写了一下，后来想了想，干脆就把他写成一个通用的控件，免的后人再去造轮子。<br>
<p>这个控件的实现原理启发于viewFlipper的源码的理解。大致原理也很简单，就是两个View不停地交换位置，加上动画，就实现了。<br>
因为实现不是很难，而且类也少，就没有必要往maven和jcenter上放了，使用的话，直接下载下来，把关键类放进项目就好了.<br><p>
如果你喜欢，并且也解决了你项目的问题，提高了你的开发效率，请点击右上角的 Star或者Fork 谢谢！（开源是一种兴事，方便快乐你我他！）

# Use
其实使用也非常简单了，简单几行代码就搞定了。<br>
* 在xml中使用控件<br>
<com.songsong.scrollmessagelayout.ScrollMessageLayout<br>
        android:id="@+id/scroll_message"<br>
        android:layout_width="match_parent"<br>
        android:layout_height="40dp"<br>
        android:background="#b3000000"<br>
        android:layout_margin="20dp"<br>
        /><br><p>
* 在java类中使用控件，这里提供了几种方式，一种是普通函数调用，一种是通过链式调用的方式（下面的list是滚动需要的数据集合）<br><p>
	* 普通调用<br>
		scroll_message=(ScrollMessageLayout)findViewById(R.id.scroll_message);<br>
        scroll_message.setClickMessageCallBack(this);<br>
        //设置点击回调监听，如果你想通过点击滚动消息然后做其他的事，那么你就的实现这个接口<br>
        scroll_message.setTimeInterval(2000);<br>
        //设置动画的播放间隔，默认3秒<br>
        scroll_message.setAnimationContinueTime(1000);<br>
        //设置动画播放持续时间，默认500毫秒<br>
        scroll_message.setTextViewDrawable(R.mipmap.tree_tips);<br>
        //设置动画滚动左边的图标，默认也是有的，可以不设置<br>
        scroll_message.setTextSize(15);<br>
        //设置text的大小，默认大小是16<br>
        scroll_message.setTextColor("#adadad");<br>
        //设置文字的颜色，默认的是白色<br>
        scroll_message.setInterpolator(new AccelerateInterpolator());<br>
        //设置动画播放的插值器，默认是DecelerateInterpolator（由快倒慢）,<br>
        // 这些都是android系统给我们提供的，好像提供了6种吧，<br>
        // 如果你觉得这几种都不符合你想要的效果，你也可以自己实现。<br>
        scroll_message.setScrollMessageList(list);<br>
        //最后一步设置list<br><p>
	* 链式调用<br>
	scroll_message=(ScrollMessageLayout)findViewById(R.id.scroll_message);<br>
        scroll_message.new Builder(list)<br>
                .setScrollAnimationContinueTime(300)<br>
                .setScrollClickMessageCallBack(this)<br>
                .setScrollInterpolator(new AnticipateInterpolator())<br>
                .setScrollTextDrawable(R.mipmap.gonggao)<br>
                .setScrollTextColor("#fffaaa")<br>
                .setScrollTextSize(15)<br>
                .setScrollTimeInterval(2000)<br>
                .build();<br><p>
	*最简单调用<br>
	如果你不需要监听点击事件和其他自定义效果，那么你只需要一行代码就可以搞定：<br>
	scroll_message.setScrollMessageList(list);<br>
# 效果图
效果图我就不贴了，自己补脑，哈哈哈.<br><p>


