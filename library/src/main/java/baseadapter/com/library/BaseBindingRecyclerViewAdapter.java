package baseadapter.com.library;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
    protected BaseListChangedCallback<T> listChangedCallback;

    /**
     * 头部数据列表
     */
    private ArrayList<ViewInfo> headerlist = new ArrayList<>();
    /**
     * 尾部数据列表
     */
    private ArrayList<ViewInfo> footerlist = new ArrayList<>();

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
        if (isHeader(position)) {
            //返回头部viewType
            return headerlist.get(position).getItemViewType();
        } else if (isFooter(position)) {
            //返回尾部viewType
            int footerPosition = position - getHeadersCount() - getListCount();
            return footerlist.get(footerPosition).getItemViewType();
        } else {
            //返回adapter viewType
            int listPosition = position - getHeadersCount();
            return getLayoutViewType(list.get(listPosition));
        }
    }

    @NonNull
    @Override
    public BaseBindingViewHolder<DB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View view = getHeaderFooterForViewType(viewType);
        if (view != null) {
            //头尾部处理
            return new BaseBindingViewHolder<>(view);
        }

        //adapter处理
        DB binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(viewType), parent, false);
        return new BaseBindingViewHolder<>(binding);
    }

    /**
     * 检查是否是头尾部 viewType
     */
    private View getHeaderFooterForViewType(int viewType) {
        for (ViewInfo info : headerlist) {
            if (info.getItemViewType() == viewType) {
                return info.getView();
            }
        }
        for (ViewInfo info : footerlist) {
            if (info.getItemViewType() == viewType) {
                return info.getView();
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseBindingViewHolder<DB> holder, int position) {
        if (isHeader(position) || isFooter(position)) {
            //头尾部处理
        } else {
            //adapter处理
            int listPosition = position - getHeadersCount();
            T vm = list.get(listPosition);
            convert(holder.getDataBinding(), vm, listPosition, holder.getItemViewType());
            holder.getDataBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + getHeadersCount() + getFootersCount();
    }

    /**
     * 返回列表数量
     */
    public int getListCount() {
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseBindingViewHolder<DB> holder) {
        super.onViewAttachedToWindow(holder);
        //解决瀑布布局头尾独占一行
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //解决网格布局头尾独占一行
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
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
    protected abstract void convert(DB dataBinding, T vm, int listPosition, int viewType);

    /**
     * 根据位置判断是否是头部
     */
    public boolean isHeader(int position) {
        return position < getHeadersCount();
    }

    /**
     * 根据位置判断是否是尾部
     */
    public boolean isFooter(int position) {
        return position >= (getHeadersCount() + getListCount());
    }

    /**
     * 添加头部
     */
    public void addHeaderView(View view) {
        headerlist.add(new ViewInfo(view, ViewTypeManage.getViewType()));
        notifyDataSetChanged();
    }

    /**
     * 删除指定头部
     */
    public void removeHeaderView(View view) {
        for (int i = 0; i < getHeadersCount(); i++) {
            if (headerlist.get(i).getView() == view) {
                headerlist.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 删除所有头部
     */
    public void removeHeaderAllView() {
        headerlist.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加尾部
     */
    public void addFooterView(View view) {
        footerlist.add(new ViewInfo(view, ViewTypeManage.getViewType()));
        notifyDataSetChanged();
    }

    /**
     * 删除指定尾部
     */
    public void removeFooterView(View view) {
        for (int i = 0; i < getHeadersCount(); i++) {
            if (footerlist.get(i).getView() == view) {
                footerlist.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 删除所有尾部
     */
    public void removeFooterAllView() {
        footerlist.clear();
        notifyDataSetChanged();
    }


    /**
     * 返回数据列表
     */
    public ObservableArrayList<T> getList() {
        return list;
    }

    /**
     * 返回头部数量
     */
    public int getHeadersCount() {
        return headerlist.size();
    }

    /**
     * 返回尾部数量
     */
    public int getFootersCount() {
        return footerlist.size();
    }

    public Context getContext() {
        return context;
    }
}
