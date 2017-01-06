package sean.com.drag;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sean.com.drag.drag.DragHolderCallBack;
import sean.com.drag.drag.RecycleCallBack;

/**
 * Created by hzhm on 2016/12/27.
 *
 * 功能描述：处理adapter的item相应的拖拽、拖拽停止、点击...相应事件...
 * 相关问题描述：notifyItemChanged(i)与notifyDataSetChanged()的区别，
 * notifyItemChanged(i)：通知相应position的item发生改变，即就是取消其相应的状态，重新再调onBindViewHolder()（注：这里可以处理选中的item避免通知，而通知其他的item发生改变...）
 * notifyDataSetChanged()：通知所有item发生改变，即就是取消所有的状态，重新再调onBindViewHolder()
 */
public class DragAdapter extends RecyclerView.Adapter<DragAdapter.DragHolder> {

    private List<String> list;

    private RecycleCallBack mRecycleClick;
    private boolean isSelected = false;

    private ObjectAnimator animator;
    private ObjectAnimator animator2;
    private ObjectAnimator animator3;
    private AnimatorSet animatorSet;

    public DragAdapter(RecycleCallBack click, List<String> data) {
        this.list = data;
        this.mRecycleClick = click;
    }

    public void setData(List<String> data) {
        this.list = data;
    }

    @Override
    public DragHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);
        return new DragHolder(view, mRecycleClick);
    }

    @Override
    public void onBindViewHolder(final DragHolder holder, final int position) {

        //---直接设置控件的属性值,避免通过设置属性动画而导致的adapter复用的问题...
//        if(holder.text.getTag(R.id.tag_width) != null && holder.text.getTag(R.id.tag_height) != null){
//            int width = (int) holder.text.getTag(R.id.tag_width);
//            int height = (int) holder.text.getTag(R.id.tag_height);
//            holder.text.getLayoutParams().width = width;
//            holder.text.getLayoutParams().height = height;
//            holder.text.setWidth(holder.text.getLayoutParams().width);
//            holder.text.setHeight(holder.text.getLayoutParams().height);
//        }

        holder.text.setText(list.get(position));
        //给相应的item打上相应的标签，即保存、记录它的原始状态
        holder.itemView.setTag(list.get(position));
        if (position == 0) {
            holder.text.setTextColor(Color.parseColor("#FFFFFF"));
            holder.text.setBackgroundResource(R.drawable.item_recommend_bg);
        } else if (position == 1) {
            holder.text.setTextColor(Color.parseColor("#999999"));
            holder.text.setBackgroundResource(R.drawable.item_no_move_bg);
        } else {
            holder.text.setTextColor(Color.parseColor("#666666"));
            holder.text.setBackgroundResource(R.drawable.item_bg);
        }
        if (isSelected) {//---选中相关item时，其他item的背景设置虚线边框
            if (position != 0 || position != 1) {
                holder.text.setTextColor(Color.parseColor("#666666"));
                holder.text.setBackgroundResource(R.drawable.item_touch_bg);
            }
        } else {//---停止选中拖拽时，消除adapter复用导致动画效果...
            ObjectAnimator animator = (ObjectAnimator) holder.item.getTag(R.id.tag_first);
            ObjectAnimator animator2 = (ObjectAnimator) holder.item.getTag(R.id.tag_second);
            ObjectAnimator animator3 = (ObjectAnimator) holder.item.getTag(R.id.tag_three);
            if (animator != null && animator2 != null && animator3 != null) {
                animator.setFloatValues(1f, 1f);
                animator2.setFloatValues(1f, 1f);
                animator3.setFloatValues(1f, 1f);
                animator.end();
                animator2.end();
                animator3.end();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DragHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, DragHolderCallBack {

        public TextView text;
        public LinearLayout item;
        private RecycleCallBack mClick;

        public DragHolder(View itemView, RecycleCallBack click) {
            super(itemView);
            mClick = click;
            item = (LinearLayout) itemView.findViewById(R.id.item);
            text = (TextView) itemView.findViewById(R.id.text);
            item.setOnClickListener(this);
        }

        /**
         * 1、对每个item做选中、拖动时处理
         */
        @Override
        public void onSelect() {
            //---设置效果1：使用属性动画，长按选中item后设置相应的效果
            animator = ObjectAnimator.ofFloat(item, "scaleX", 1f, 1.1f);
            animator2 = ObjectAnimator.ofFloat(item, "scaleY", 1f, 1.1f);
            animator3 = ObjectAnimator.ofFloat(item, "alpha", 1f, 0.6f);

            animatorSet = new AnimatorSet();
            animatorSet.play(animator).with(animator2).with(animator3);
            animatorSet.setDuration(100);
            animatorSet.start();
            text.setBackgroundResource(R.drawable.item_touch_bg);
            for (int i = 2; i < list.size(); i++) {
                if (i != getAdapterPosition()) {
                    notifyItemChanged(i);
                    notifyDataSetChanged();
                }
            }
            isSelected = true;

            //---设置效果2：z直接改变控件宽、高、透明度等等属性值
//            text.setTextColor(Color.parseColor("#4d666666"));
//            int width = text.getWidth();
//            int height = text.getHeight();
//            text.getLayoutParams().width = ((int) (1.1 * width));
//            text.getLayoutParams().height = ((int) (1.1 * height));
//            text.setWidth(text.getLayoutParams().width);
//            text.setHeight(text.getLayoutParams().height);
//            text.setTag(R.id.tag_width, width);
//            text.setTag(R.id.tag_height, height);
//            mListener.changeItemBackground();
        }

        /**
         * 2、对每个item做停止拖动时处理
         */
        @Override
        public void onClear() {
            isSelected = false;
            item.setTag(R.id.tag_first, animator);
            item.setTag(R.id.tag_second, animator2);
            item.setTag(R.id.tag_three, animator3);
            notifyDataSetChanged();
        }

        /**
         * 3、对每个item做OnClick时处理
         */
        @Override
        public void onClick(View v) {
            if (null != mClick) {
                mClick.itemOnClick(getAdapterPosition(), v);
            }
        }
    }
}
