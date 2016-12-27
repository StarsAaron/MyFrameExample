/*
###########################################################################
滑动返回工具类

https://github.com/ikew0ng/SwipeBackLayout
###########################################################################

使用说明：

 1.将SwipeBackLayout添加进项目的依赖中

 2.让activity继承SwipeBackActivity，可以通过getSwipeBackLayout()方法自定义SwipeBackLayout

 3.在主题中设置<item name="android:windowIsTranslucent">true</item>

 例子：

 public class DemoActivity extends SwipeBackActivity {

 private SwipeBackLayout mSwipeBackLayout;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 。。。
     mSwipeBackLayout = getSwipeBackLayout();
     //设置可以滑动的区域，推荐用屏幕像素的一半来指定
     mSwipeBackLayout.setEdgeSize(200);
     //设定滑动关闭的方向 EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM，EDGE_ALL
     mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
     }
 }
 ...

 */