package sean.com.drag.drag;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Author: zhuwt
 * Date: 2016/7/4 15:52
 * Description: 说明
 * PackageName: sean.com.drag.drag.DragItemCallBack
 * Copyright: 杭州存网络科技有限公司
 *
 * 功能描述：拖拽模块监听处理类，主要时实现了ItemTouchHelper.Callback接口，对模块的拖拽做相关的监听处理
 *
 **/
public class DragItemCallBack extends ItemTouchHelper.Callback {

    private RecycleCallBack mCallBack;

    public DragItemCallBack(RecycleCallBack callBack) {
        this.mCallBack = callBack;
    }

    /**
     * getMovementFlags方法返回每隔行动(闲置,拖动,刷)的方向，比如拖动的方向定义
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        int action = ItemTouchHelper.ACTION_STATE_IDLE | ItemTouchHelper.ACTION_STATE_DRAG;
//        return makeFlag(action,dragFlags);

        //设置相关位置不能移动拖拽
        if(viewHolder.getAdapterPosition() == 1 || viewHolder.getAdapterPosition() == 0 || viewHolder.getAdapterPosition() == 4){
            return makeMovementFlags(0, 0);
        }else {//---如果当前item的位置为0时，则禁止拖拽...
            return makeMovementFlags(dragFlags, 0);
        }
    }

    /**
     * onMove方法里的两个ViewHolder就是移动前和移动后的Item，通过getAdapterPosition()就能获取移动的位置
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int start = viewHolder.getAdapterPosition();
        int end = target.getAdapterPosition();
        mCallBack.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * (要实现左右滑动删除时可以使用)
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * item选中、拖动时
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //判断相应的RecycleView的viewHolder是否实现了DragHolderCallBack接口
        if (viewHolder instanceof DragHolderCallBack) {
            DragHolderCallBack holder = (DragHolderCallBack) viewHolder;
            //实现、调用这个接口的onSelect方法
            holder.onSelect();
        }
    }

    /**
     * item停止拖动时
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // 这个是防止RecycleView还在计算视图的时候，改变了item的状态。
        // 否则会报出异常：cannot call this method while recyclerview is computing a layout or scrolling。
        if (!recyclerView.isComputingLayout()) {
            //判断相应的RecycleView的viewHolder是否实现了DragHolderCallBack接口
            if (viewHolder instanceof DragHolderCallBack) {
                DragHolderCallBack holder = (DragHolderCallBack) viewHolder;
                //实现、调用这个接口的onClear方法
                holder.onClear();
            }
        }
    }
}
