package baseadapter.com.library;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BaseBindingRecyclerViewAdapter<DB extends ViewDataBinding, T> extends RecyclerView.Adapter<BaseBindingViewHolder<DB>> {
    private Context context;
    /**
     * 相当于ArrayList，但是增加了观察者模式，
     */
    private ObservableArrayList<T> list;
    /**
     * 数据变化监听器
     */
    private BaseListChangedCallback<T> listChangedCallback;

    private ArrayList<ViewInfo> headerlist = new ArrayList<>();

    /**
     * 构造
     */
    public BaseBindingRecyclerViewAdapter() {
        this(null);
    }

    /**
     * 构造
     */
    public BaseBindingRecyclerViewAdapter(ObservableArrayList<T> list) {
        if (list == null) {
            this.list = new ObservableArrayList<>();
        } else {
            this.list = list;
        }
        listChangedCallback = new BaseListChangedCallback<>(this);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderSCount()) {
            //返回头部viewType
            return headerlist.get(position).getItemViewType();
        } else {
            return getLayoutViewType(list.get(position - getHeaderSCount()));
        }
    }

    @NonNull
    @Override
    public BaseBindingViewHolder<DB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View headerView = getHeaderForViewType(viewType);
        if (headerView != null) {
            //头部处理
            return new BaseBindingViewHolder<>(headerView);
        } else {
            //adapter处理
            DB binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(viewType), parent, false);
            return new BaseBindingViewHolder<>(binding);
        }
    }

    /**
     * 检查是否是头部 viewType
     */
    private View getHeaderForViewType(int viewType) {
        for (ViewInfo info : headerlist) {
            if (info.getItemViewType() == viewType) {
                return info.getView();
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseBindingViewHolder<DB> holder, int position) {
        if (position < getHeaderSCount()) {
            //头部处理
        } else {
            //adapter处理
            T vm = list.get(position - getHeaderSCount());
            convert(holder.getDataBinding(), vm, position - getHeaderSCount(), holder.getItemViewType());
            holder.getDataBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + getHeaderSCount();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        getList().addOnListChangedCallback(listChangedCallback);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        getList().removeOnListChangedCallback(listChangedCallback);
    }

    /**
     * 返回布局type
     */
    protected abstract int getLayoutViewType(T t);

    /**
     * 返回布局
     */
    protected abstract int getLayoutId(int viewType);

    /**
     * 绑定
     */
    protected abstract void convert(DB dataBinding, T vm, int position, int viewType);

    public void addHeaderView(View view) {
        headerlist.add(new ViewInfo(view, ViewInfo.getVIEWTYPE()));
        notifyDataSetChanged();
    }

    public void removeHeaderView(View view) {
        for (int i = 0; i < getHeaderSCount(); i++) {
            if (headerlist.get(i).getView() == view) {
                headerlist.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public ObservableArrayList<T> getList() {
        return list;
    }

    public int getHeaderSCount() {
        return headerlist.size();
    }

    public Context getContext() {
        return context;
    }
}
