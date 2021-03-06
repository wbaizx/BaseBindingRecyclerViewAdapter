# BaseBindingRecyclerViewAdapter


 一个简单RecyclerView的Adapter封装，使用Databinding模式，配合ObservableArrayList，使用简单。
 直接操作list就能自动更新UI，除非特殊情况下，不需要手动调notifyDataSetChanged()等方法。


 导入方法
```java
        allprojects {
            repositories {
		  ...
                maven { url 'https://jitpack.io' }
            }
        }

        implementation 'com.github.wbaizx:BaseBindingRecyclerViewAdapter:1.1.0'
```
## 普通使用方法

 创建adapter

```java
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
    protected void convert(BaseBindingViewHolder<NormalRecyclerviewLayoutBinding> holder, BeanViewModel vm, int listPosition) {
        holder.getDataBinding().setVm(vm);
    }
}

```
 第一个泛型是你列表xml布局对应生成的Binding，第二个泛型是列表数据的ViewModel。

 convert中第一个参数holder可以获取到对应binding,viewType,根view等。
 第二个参数是对应的viewModel。
 第三个参数是数据在list中的位置（不包括头尾部）。

 在convert中绑定对应viewModel即可。

 然后使用它
```java
        dataBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        NormalAdapter adapter = new NormalAdapter();
        dataBinding.recyclerview.setAdapter(adapter);
```
 BaseBindingRecyclerViewAdapter还有一个带一个ObservableArrayList参数的构造方法，在继承时覆写就能使用，如下：
```java
    public NormalAdapter(ObservableArrayList<BeanViewModel> list) {
        super(list);
    }
```
 对数据的操作，通过

        adapter.getList()

 获取到ObservableArrayList类型的list（如果使用带参数的构造创建的则是同一个list），然后直接操作list就可以，UI会自动更新。
 但需要注意列表数据的viewModel模型最好不要使用LiveData，因为liveData模式的viewModel初始化不太方便，所以使用原生方式就行。
 ```java
 extends BaseObservable

 @Bindable

 notifyPropertyChanged(BR.name);
 ```

## 多布局使用方法

```java
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
    protected void convert(BaseBindingViewHolder<ViewDataBinding> holder, BeanViewModel vm, int listPosition) {
        holder.getDataBinding().setVariable(BR.vm, vm);
    }
}

```

 第一个泛型改成了Binding的基类ViewDataBinding，然后在getLayoutViewType中返回你想要的viewType（最好不要为负数，否则可能与头尾布局的viewType冲突）。
 然后在getLayoutId中根据viewType返回对应布局。
 对数据的操作方式相同。
 最后注意convert中的绑定，因为这里不知道具体的Binding类型但是数据的ViewModel肯定是相同的，所以可以使用setVariable()设置，BR.vm是根据你xml布局中的变量名生成的，
 所以不同xml布局的variable变量名要相同，如下：
```java
        <variable
            name="vm"
            type="xxx.com.xxx.BeanViewModel" />
```


## 其他方法

### 添加头尾部（瀑布和网格布局会适配到整行，inflate第二个参数最好传入当前RecyclerView）
```java
        adapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.head, recycler, false));
        adapter.addFooterView(LayoutInflater.from(this).inflate(R.layout.head, recycler, false));
```
 添加的头尾部也可以随时删掉

### 点击事件

 一般的如点击事件直接在viewModel中处理就好，如果在页面中需要回调（比如页面跳转，因为ViewModel中不好持有Context）,可以创建一个接口，
 然后在adapter中通过根布局设置点击事件回调，或者传递给ViewModel，然后在ViewModel的点击处理中回调
```java
    @Override
    protected void convert(BaseBindingViewHolder<NormalRecyclerviewLayoutBinding> holder, BeanViewModel vm, final int listPosition) {
        holder.getDataBinding().setVm(vm);
        //直接给根布局设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(listPosition);
            }
        });
        //或者把监听器传到viewModel中回调
        vm.setItemOnClickListener(listener);
    }
```
### 添加侧滑删除和拖拽功能

 要使用侧滑或拖拽功能的adapter需要继承BaseBindingDraggableAdapter

```java
   public class DraggableAdapter extends BaseBindingDraggableAdapter<ViewDataBinding, BeanViewModel>
```
 使用
```java
         ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
         itemTouchHelper.attachToRecyclerView(activityMainBinding.recycler);

        //开启拖拽
        adapter.setDragEnabled(true);
        adapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(int listPosition) {
            }
            @Override
            public void onItemDraging(int fromListPosition, int toListPosition) {
            }
            @Override
            public void onItemDragEnd(int listPosition) {
            }
        });

        //开启侧滑
        adapter.setSwipeEnabled(true);
        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(int listPosition) {
            }
            @Override
            public void onItemSwipeing(RecyclerView.ViewHolder viewHolder, int listPosition) {
                //adapter.getList().remove(listPosition);
                //侧滑后如果没有删除选中项需要恢复item状态
                adapter.recoverItem(viewHolder, listPosition);
            }
        });
```
 其余用法与BaseBindingRecyclerViewAdapter

### 其他还有些方法就不一一列出来了。