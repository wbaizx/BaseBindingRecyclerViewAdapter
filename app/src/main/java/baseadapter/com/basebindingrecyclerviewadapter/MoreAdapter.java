package baseadapter.com.basebindingrecyclerviewadapter;

import android.databinding.ViewDataBinding;

import baseadapter.com.library.BaseBindingRecyclerViewAdapter;

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
