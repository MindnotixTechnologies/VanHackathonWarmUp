package todo.list.warmup.dia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import me.drakeet.materialdialog.MaterialDialog;
import todo.list.warmup.R;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class Dialog extends MaterialDialog {

    private Context context;

    private ListView optionsListView;
    private EditText edit;

    public Dialog(Context context) {
        super(context);
        this.context = context;
        setCanceledOnTouchOutside(true);
    }


    public Dialog setOptions(String... options) {
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter(context, android.R.layout.simple_list_item_1);

        for (int i = 0; i < options.length; i++) {
            arrayAdapter.add(options[i]);
        }

        optionsListView = (ListView) LayoutInflater.from(context).inflate(R.layout.options, null, false);

        optionsListView.setAdapter(arrayAdapter);
        setContentView(optionsListView);

        return this;
    }

    public Dialog setOnItemClickListener(final AdapterView.OnItemClickListener listener) {
        if (optionsListView != null)
            optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dismiss();

                    if (listener != null)
                        listener.onItemClick(adapterView, view, i, l);
                }
            });

        return this;
    }


    public Dialog setEdit(String hint, String text) {
        View v = LayoutInflater.from(context).inflate(R.layout.material_edit_dialog, null, false);

        ((TextView) v.findViewById(R.id.title)).setText(R.string.update);
        ((TextView) v.findViewById(R.id.message)).setText(R.string.want_update_item);
        edit = (EditText) v.findViewById(R.id.edit);
        edit.setText(text);
        edit.setHint(hint);

        setView(v);

        return this;
    }


    @Override
    public MaterialDialog setPositiveButton(int resId, final View.OnClickListener listener) {
        return super.setPositiveButton(resId, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick(view);

                dismiss();
            }
        });
    }


    @Override
    public MaterialDialog setPositiveButton(String text, final View.OnClickListener listener) {
        return super.setPositiveButton(text, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick(view);

                dismiss();
            }
        });
    }


    public MaterialDialog setPositiveButton(int res) {
        return super.setPositiveButton(res, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public MaterialDialog setNegativeButton(int resId, final View.OnClickListener listener) {
        return super.setNegativeButton(resId, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick(view);

                dismiss();
            }
        });
    }


    @Override
    public MaterialDialog setNegativeButton(String text, final View.OnClickListener listener) {
        return super.setNegativeButton(text, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick(view);

                dismiss();
            }
        });
    }

    public MaterialDialog setNegativeButton(int res) {
        return super.setNegativeButton(res, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public EditText getEdit() {
        return edit;
    }
}
