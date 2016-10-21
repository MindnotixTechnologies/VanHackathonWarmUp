package todo.list.warmup.adpt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import todo.list.warmup.R;
import todo.list.warmup.bean.ToDoList;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private Context context;
    private List<ToDoList> data = new ArrayList();
    private View.OnClickListener onItemClickListener;
    private View.OnLongClickListener onLongClickListener;

    public ToDoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false);

        viewGroup.setOnClickListener(onItemClickListener);
        viewGroup.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ToDoList list = data.get(position);

        holder.title.setText(list.getId() + "   " + list.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public ToDoList getItem(int position) {
        return data.get(position);
    }

    public void add(ToDoList list) {
        this.data.add(list);

        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(List<ToDoList> list) {
        this.data.addAll(list);
        notifyItemRangeInserted(0,getItemCount());
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.title);
        }
    }


}
