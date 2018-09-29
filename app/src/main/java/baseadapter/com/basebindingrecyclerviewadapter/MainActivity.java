package baseadapter.com.basebindingrecyclerviewadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import baseadapter.com.basebindingrecyclerviewadapter.databinding.ActivityMainBinding;
import baseadapter.com.library.BaseBindingRecyclerViewAdapter;
import baseadapter.com.library.helper.ItemTouchHelperCallback;
import baseadapter.com.library.helper.OnItemDragListener;
import baseadapter.com.library.helper.OnItemSwipeListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        activityMainBinding.recycler1.setVisibility(View.VISIBLE);
//        activityMainBinding.recycler1.setLayoutManager(new LinearLayoutManager(this));
//        activityMainBinding.recycler1.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        NormalAdapter adapter = new NormalAdapter();
//        activityMainBinding.recycler1.setAdapter(adapter);
//        addTestData(adapter.getList());


//        activityMainBinding.recycler2.setVisibility(View.VISIBLE);
//        activityMainBinding.recycler2.setLayoutManager(new LinearLayoutManager(this));
//        activityMainBinding.recycler2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        MoreAdapter adapter2 = new MoreAdapter();
//        addTestData(adapter2.getList());
//        activityMainBinding.recycler2.setAdapter(adapter2);


//        activityMainBinding.recycler3.setVisibility(View.VISIBLE);
//        activityMainBinding.recycler3.setLayoutManager(new LinearLayoutManager(this));
//        activityMainBinding.recycler3.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        final MoreAdapter adapter3 = new MoreAdapter();
//        adapter3.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
//        adapter3.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
//        adapter3.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
//        adapter3.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
//        addTestData(adapter3.getList());
//        activityMainBinding.recycler3.setAdapter(adapter3);

        activityMainBinding.recycler4.setVisibility(View.VISIBLE);
        activityMainBinding.recycler4.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.recycler4.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        final DraggableAdapter adapter4 = new DraggableAdapter();
        adapter4.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler4, false));
        adapter4.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler4, false));
        adapter4.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler4, false));
        adapter4.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler4, false));
        addTestData(adapter4.getList());
        activityMainBinding.recycler4.setAdapter(adapter4);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter4));
        itemTouchHelper.attachToRecyclerView(activityMainBinding.recycler4);
        //开启拖拽
        adapter4.setDragEnabled(true);
        //开启侧滑
        adapter4.setSwipeEnabled(true);
        adapter4.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(int listPosition) {
                Log.e("efafewa", "onItemDragStart" + "--" + listPosition);
            }

            @Override
            public void onItemDraging(int fromListPosition, int toListPosition) {
                Log.e("efafewa", "onItemDraging" + "--" + fromListPosition + "--" + toListPosition);
            }

            @Override
            public void onItemDragEnd(int listPosition) {
                Log.e("efafewa", "onItemDragEnd" + "--" + listPosition);
            }
        });
        adapter4.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(int listPosition) {
                Log.e("gwefwae", "onItemSwipeStart" + "--" + listPosition);
            }

            @Override
            public void onItemSwipeing(RecyclerView.ViewHolder viewHolder, int listPosition) {
                Log.e("gwefwae", "onItemSwipeing" + "--" + listPosition);
//                adapter3.getList().remove(listPosition);
                //侧滑后如果没有删除选中项需要恢复item状态
                adapter4.recoverItem(viewHolder, listPosition);
            }
        });
    }


    private void addTestData(final ObservableArrayList<BeanViewModel> list) {
        //测试数据
        for (int i = 0; i < 30; i++) {
            if (i % 2 == 0) {
                list.add(new BeanViewModel("a", "女", i));
            } else {
                list.add(new BeanViewModel("a", "男", i));
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 18; i < 20; i++) {
                    list.add(1, new BeanViewModel("a", "男", i));
                }
            }
        }, 1000);
    }

    public void removeView(final BaseBindingRecyclerViewAdapter adapter) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.removeHeaderAllView();
                adapter.removeFooterAllView();
            }
        }, 3000);
    }
}
