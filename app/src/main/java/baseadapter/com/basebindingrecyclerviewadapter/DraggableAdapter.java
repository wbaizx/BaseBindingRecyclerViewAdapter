package baseadapter.com.basebindingrecyclerviewadapter;

import android.databinding.ViewDataBinding;

import baseadapter.com.library.BaseBindingDraggableAdapter;
import baseadapter.com.library.BaseBindingViewHolder;

public class DraggableAdapter extends BaseBindingDraggableAdapter<ViewDataBinding, BeanViewModel> {
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
