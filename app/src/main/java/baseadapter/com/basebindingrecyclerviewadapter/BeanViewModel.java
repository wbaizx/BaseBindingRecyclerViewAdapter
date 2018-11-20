package baseadapter.com.basebindingrecyclerviewadapter;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * RecyclerView 中的 ViewModel 最好使用原装模式
 */
public class BeanViewModel extends BaseObservable {
    private String name;
    private String sex;
    private int age;

    private ItemOnClickListener listener;

    public BeanViewModel(String name, String sex, int age) {
        setName(name);
        setSex(sex);
        setAge(age);
    }

    /**
     * item点击事件
     */
    public void add(int age) {
        setAge(age + 1);
        if (listener != null) {
            listener.onClick(age);
        }
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        notifyPropertyChanged(BR.sex);
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    public void setItemOnClickListener(ItemOnClickListener listener) {
        this.listener = listener;
    }

    public interface ItemOnClickListener {
        void onClick(int age);
    }
}
