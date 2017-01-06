package sean.com.drag.drag;

import android.view.View;

/**
 *
 * 功能描述：这个接口被整个MainActivity所实现，用于处理RecycleView里面相应item模块的点击事件、及其拖拽事件
 **/
public interface RecycleCallBack {

    //item的点击事件
    void itemOnClick(int position,View view);

    //item的拖拽事件
    void onMove(int from,int to);
}
