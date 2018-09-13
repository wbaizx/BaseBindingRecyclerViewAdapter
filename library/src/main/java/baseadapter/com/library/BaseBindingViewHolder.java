package baseadapter.com.library;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseBindingViewHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private DB dataBinding;

    public BaseBindingViewHolder(DB dataBinding) {
        super(dataBinding.getRoot());
        this.dataBinding = dataBinding;
    }

    /**
     * 这个构造方法用于使用头尾的情况
     */
    public BaseBindingViewHolder(View view) {
        super(view);
    }

    public DB getDataBinding() {
        return dataBinding;
    }
}
