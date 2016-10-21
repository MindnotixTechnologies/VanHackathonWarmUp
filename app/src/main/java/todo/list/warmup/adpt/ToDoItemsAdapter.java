package todo.list.warmup.adpt;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import todo.list.warmup.R;
import todo.list.warmup.api.RestItem;
import todo.list.warmup.bean.ToDoItem;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class ToDoItemsAdapter extends RecyclerView.Adapter<ToDoItemsAdapter.ViewHolder> {

    private Context context;
    private List<ToDoItem> data = new ArrayList();
    private View.OnLongClickListener onLongClickListener;

    public ToDoItemsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false);

        viewGroup.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ToDoItem item = data.get(position);

        holder.item.setText(item.getId() + "   " + item.getDescription());
        holder.item.setPaintFlags(item.isChecked() ? Paint.STRIKE_THRU_TEXT_FLAG : Paint.ANTI_ALIAS_FLAG);

        holder.checked.setOnCheckedChangeListener(null);
        holder.checked.setChecked(item.isChecked());
        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                data.get(position).setChecked(b);
                notifyItemChanged(position);

                RestItem.check(data.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public ToDoItem getItem(int position) {
        return data.get(position);
    }

    public void add(ToDoItem item) {
        this.data.add(item);

        notifyItemInserted(getItemCount() - 1);
    }

    public void update(int position, ToDoItem item) {
        this.data.set(position, item);
        notifyItemChanged(position);
    }


    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item;
        private CheckBox checked;

        public ViewHolder(View v) {
            super(v);
            this.item = (TextView) v.findViewById(R.id.item);
            this.checked = (CheckBox) v.findViewById(R.id.checkbox);
        }
    }


    public void addAll(List<ToDoItem> list) {
        this.data.addAll(list);
        notifyItemRangeInserted(0,getItemCount());
    }
}
