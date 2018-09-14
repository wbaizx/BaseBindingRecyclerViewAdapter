# BaseBindingRecyclerViewAdapter


 一个简单RecyclerView的Adapter封装，使用Databinding模式，配合ObservableArrayList，使用简单。
 直接操作list就能自动更新UI，除非特殊情况（什么是特殊我不知道）下，不需要手动调notifyDataSetChanged()等方法。


 导入方法

        implementation 'com.github.wbaizx:BaseBindingRecyclerViewAdapter:1.0.1'


## 普通使用方法

 创建adapter

public class NormalAdapter extends BaseBindingRecyclerViewAdapter<NormalRecyclerviewLayoutBinding, BeanViewModel> {

    @Override
    protected int getLayoutViewType(BeanViewModel beanViewModel) {
        return 0;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.normal_recyclerview_layout;
    }

    @Override
    protected void convert(NormalRecyclerviewLayoutBinding dataBinding, BeanViewModel vm, int position, int viewType) {
        dataBinding.setVm(vm);
    }
}

 第一个泛型是你列表xml布局对应生成的Binding，第二个泛型是列表数据的ViewModel。这基本是固定模板，你需要改的只有布局和dataBinding和绑定对应ViewModel。
 其他的数据监听等都在ViewModel中。

 然后使用它

        recycler1.setLayoutManager(new LinearLayoutManager(this));
        NormalAdapter adapter = new NormalAdapter();
        recycler1.setAdapter(adapter);

 BaseBindingRecyclerViewAdapter还有一个带一个ObservableArrayList参数的构造方法，在继承时覆写就能使用，如下：

    public NormalAdapter(ObservableArrayList<BeanViewModel> list) {
        super(list);
    }

 对数据的操作，通过

        adapter.getList()

 获取到ObservableArrayList类型的list（如果使用带参数的构造创建的则是同一个list），然后直接操作list就可以，UI会自动更新。
 但需要注意列表数据的viewModel模型不要使用LiveData，不然item项的内容改变不能实时更新UI。

## 多布局使用方法

public class MoreAdapter extends BaseBindingRecyclerViewAdapter<ViewDataBinding, BeanViewModel> {
    @Override
    protected int getLayoutViewType(BeanViewModel beanViewModel) {
        if (beanViewModel.getSex().equals("男")) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    protected int getLayoutId(int viewType) {
        if (viewType == 1) {
            return R.layout.normal_recyclerview_layout;
        } else {
            return R.layout.normal_recyclerview_layout2;
        }
    }

    @Override
    protected void convert(ViewDataBinding dataBinding, BeanViewModel vm, int position, int viewType) {
        dataBinding.setVariable(BR.vm, vm);
    }
}

 第一个泛型改成了Binding的基类ViewDataBinding，然后在getLayoutViewType中返回你想要的viewType（最好不要为负数，否则可能与头尾布局的viewType冲突）。
 然后在getLayoutId中根据viewType返回对应布局。
 对数据的操作方式相同。
 最后注意convert中的绑定，因为这里不知道具体的Binding类型但是数据的ViewModel肯定是相同的，所以可以使用setVariable()设置，BR.vm是根据你xml布局中的变量名生成的，如下：

        <variable
            name="vm"
            type="xxx.com.xxx.BeanViewModel" />



## 其他方法

 添加头尾部（瀑布和网格布局会适配到整行，inflate第二个参数最好传入当前RecyclerView）

        adapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, recycler, false));
        adapter.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, recycler, false));

 一般的如点击事件直接在viewModel中处理就好，如果在页面中需要回调（比如页面跳转，因为ViewModel中不好持有Context）,可以创建一个接口，
 然后在adapter中传递给ViewModel，然后在ViewModel的点击处理中回调

    @Override
    protected void convert(NormalRecyclerviewLayoutBinding dataBinding, BeanViewModel vm, int position, int viewType) {
        dataBinding.setVm(vm);
        vm.setItemOnClickListener(listener);
    }

 其他还有些方法就不一一列出来了。