package baseadapter.com.basebindingrecyclerviewadapter;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycler1 = findViewById(R.id.recycler1);
        RecyclerView recycler2 = findViewById(R.id.recycler2);

        recycler1.setVisibility(View.VISIBLE);
        recycler1.setLayoutManager(new LinearLayoutManager(this));
        recycler1.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        final NormalAdapter adapter = new NormalAdapter();
        final View inflate = LayoutInflater.from(this).inflate(R.layout.head, recycler1, false);
        adapter.addHeaderView(inflate);
        recycler1.setAdapter(adapter);
        addTestData(adapter.getList());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.removeHeaderView(inflate);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addHeaderView(inflate);
                    }
                },2000);
            }
        },2000);


        recycler2.setVisibility(View.VISIBLE);
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        recycler2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        MoreAdapter adapter2 = new MoreAdapter();
        adapter2.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, recycler2, false));
        addTestData(adapter2.getList());
        recycler2.setAdapter(adapter2);
    }


    private void addTestData(final ObservableArrayList<BeanViewModel> list) {
        //测试数据
        for (int i = 0; i < 3; i++) {
            list.add(new BeanViewModel("a", "女", i));
        }
        for (int i = 3; i < 30; i++) {
            list.add(new BeanViewModel("a", "男", i));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 18; i < 20; i++) {
                    list.add(1, new BeanViewModel("a", "女", i));
                }
            }
        }, 1000);

    }
}
