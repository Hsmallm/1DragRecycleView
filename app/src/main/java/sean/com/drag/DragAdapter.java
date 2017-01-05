package sean.com.drag;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sean.com.drag.drag.DragHolderCallBack;
import sean.com.drag.drag.RecycleCallBack;


/**
 * Author: zhuwt
 * Date: 2016/6/21 18:07
 * Description: 说明
 * PackageName: com.ancun.autopack.ProjectAdapter
 * Copyright: 杭州存网络科技有限公司
 **/
public class DragAdapter extends RecyclerView.Adapter<DragAdapter.DragHolder> {

    private List<String> list;

    private RecycleCallBack mRecycleClick;
    public SparseArray<Integer> show = new SparseArray<>();
    private boolean firstInto = true;

    private ObjectAnimator animator;
    private ObjectAnimator animator2;
    private ObjectAnimator animator3;
    private AnimatorSet animatorSet;
    private OnSelectListener mListener;

    private interface OnSelectListener {
        void changeItemBackground();
    }

    private void setSelectListener(OnSelectListener listener) {
        mListener = listener;
    }

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
//        if(holder.text.getTag(R.id.tag_width) != null && holder.text.getTag(R.id.tag_height) != null){
//            int width = (int) holder.text.getTag(R.id.tag_width);
//            int height = (int) holder.text.getTag(R.id.tag_height);
//            holder.text.getLayoutParams().width = width;
//            holder.text.getLayoutParams().height = height;
//            holder.text.setWidth(holder.text.getLayoutParams().width);
//            holder.text.setHeight(holder.text.getLayoutParams().height);
//        }

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
        holder.text.setText(list.get(position));
        //给相应的item打上相应的标签，即保存、记录它的原始状态
        holder.itemView.setTag(list.get(position));
        if (!firstInto && null == show.get(position)){
            holder.text.setBackgroundResource(R.drawable.item_touch_bg);
        }else {
            //        setSelectListener(new OnSelectListener() {
//            @Override
//            public void changeItemBackground() {
//
//            }
//        });
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
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DragHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, DragHolderCallBack {

        public TextView text;
        //        public ImageView del;
        public LinearLayout item;
        private RecycleCallBack mClick;

        public DragHolder(View itemView, RecycleCallBack click) {
            super(itemView);
            mClick = click;
            item = (LinearLayout) itemView.findViewById(R.id.item);
            text = (TextView) itemView.findViewById(R.id.text);
//            del = (ImageView) itemView.findViewById(R.id.del);
            item.setOnClickListener(this);
//            del.setOnClickListener(this);
        }

        /**
         * 1、对每个item做选中、拖动时处理
         */
        @Override
        public void onSelect() {
            show.clear();
            show.put(getAdapterPosition(), getAdapterPosition());
//            itemView.setBackgroundColor(Color.LTGRAY);
//            del.setVisibility(View.VISIBLE);

            //长按选中item后设置相应的效果
            animator = ObjectAnimator.ofFloat(item, "scaleX", 1f, 1.1f);
            animator2 = ObjectAnimator.ofFloat(item, "scaleY", 1f, 1.1f);
            animator3 = ObjectAnimator.ofFloat(item, "alpha", 1f, 0.6f);

            animatorSet = new AnimatorSet();
            animatorSet.play(animator).with(animator2).with(animator3);
            animatorSet.setDuration(100);
            animatorSet.start();
            text.setBackgroundResource(R.drawable.item_touch_bg);
//            firstInto = false;
//            notifyItemRangeChanged(3,list.size()-2,getAdapterPosition());
//            item.setTag(R.id.tag_|select,animatorSet);
//            notifyDataSetChanged();
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
            firstInto = true;
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
                show.clear();
                mClick.itemOnClick(getAdapterPosition(), v);
            }
        }
    }
}
