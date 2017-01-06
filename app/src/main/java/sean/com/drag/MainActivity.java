package sean.com.drag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import sean.com.drag.drag.DragItemCallBack;
import sean.com.drag.drag.RecycleCallBack;


/**
 * Created by hzhm on 2016/12/27.
 *
 * 功能描述：实现类“今日头条”拖拽排序功能...
 */
public class MainActivity extends AppCompatActivity implements RecycleCallBack {

    private RecyclerView mRecyclerView;
    private DragAdapter mAdapter;
    private ArrayList<String> mList;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("泰然金融" + i);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //将实现的RecycleCallBack接口传入这个Adapter里面，完成对item的点击监听
        mAdapter = new DragAdapter(this, mList);

        //将实现的RecycleCallBack接口传入这个DragItemCallBack里面，完成对item的移动监听
        mItemTouchHelper = new ItemTouchHelper(new DragItemCallBack(this));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * RecycleCallBack接口实现---对删除图标的点击、item视图点击回调
     *
     * @param position
     * @param view
     */
    @Override
    public void itemOnClick(int position, View view) {
        Toast.makeText(MainActivity.this, "当前点击的是" + view.getTag(), Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * RecycleCallBack接口实现---对item视图模块拖拽、移动回调
     *
     * @param from
     * @param to
     */
    @Override
    public void onMove(int from, int to) {
        synchronized (this) {
            if (to == 4 || to == 0 || to == 1) {
                return;
            }
            if (from > to) {//从大到小
                int count = from - to;
                for (int i = 0; i < count; i++) {
                    //-------list-- 将指定List集合中i处元素和j出元素进行交换
                    // i-- 要交换的一个元素的索引。
                    // j-- 要交换的其它元素的索引。
                    if (from - i - 1 == 4) {//保持固定位置不变
                        Collections.swap(mList, from - i, from - i - 2);
                    } else {
                        if (from - i != 4) {//（注：如果from - i == 4，则不进行任何处理...,因为于4相邻的不做移动....
                            //例如：从5-->>3时，count=2,int=0,1;此时
                            // 当int=0时，只做5到3的变换...
                            // 当int=1时，不做处理，正好满足条件...）
                            Collections.swap(mList, from - i, from - i - 1);
                        }
                    }
                }
            }
            if (from < to) {//从小到大
                int count = to - from;
                for (int i = 0; i < count; i++) {
                    if (from + i + 1 == 4) {//保持固定位置不变
                        Collections.swap(mList, from + i, from + i + 2);
                    } else {
                        if (from + i != 4) {
                            Collections.swap(mList, from + i, from + i + 1);
                        }
                    }
                }
            }
            mAdapter.setData(mList);
            //-------实现item模块的拖拽、移动时的通知
            mAdapter.notifyItemMoved(from, to);
            //--------拖拽移动后，其删除光标的位置也要随之移动...
        }
    }
}
