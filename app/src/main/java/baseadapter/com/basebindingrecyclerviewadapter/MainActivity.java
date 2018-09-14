package baseadapter.com.basebindingrecyclerviewadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import baseadapter.com.basebindingrecyclerviewadapter.databinding.ActivityMainBinding;
import baseadapter.com.library.BaseBindingRecyclerViewAdapter;

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


        activityMainBinding.recycler3.setVisibility(View.VISIBLE);
        activityMainBinding.recycler3.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.recycler3.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        MoreAdapter adapter3 = new MoreAdapter();
        adapter3.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
        adapter3.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
        adapter3.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
        adapter3.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, activityMainBinding.recycler3, false));
        addTestData(adapter3.getList());
        activityMainBinding.recycler3.setAdapter(adapter3);
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
