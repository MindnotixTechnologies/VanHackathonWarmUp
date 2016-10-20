package todo.list.warmup.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import todo.list.warmup.R;
import todo.list.warmup.Snack;
import todo.list.warmup.adpt.ToDoItemsAdapter;
import todo.list.warmup.bean.ToDoItem;
import todo.list.warmup.dia.Dialog;
import todo.list.warmup.view.DividerItemDecoration;

import static todo.list.warmup.R.id.fab;
import static todo.list.warmup.R.string.update;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SELECTED_LIST_ID = "SELECTED_LIST_ID";

    private int selectListId = 0;

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ToDoItemsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        bindViews();
        customConfiguration();
        bindListeners();
        setup();

        for (int i = 1; i <= 3; i++) {
            adapter.add(new ToDoItem("Item Example " + i));
        }

    }

    private void bindViews() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.floatingActionButton = (FloatingActionButton) findViewById(fab);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
    }

    private void customConfiguration() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        selectListId = (int) getIntent().getExtras().get(SELECTED_LIST_ID);

        setTitle(getString(R.string.items_title, selectListId));

    }

    private void bindListeners() {
        floatingActionButton.setOnClickListener(this);
    }

    private void setup() {
        this.adapter = new ToDoItemsAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = recyclerView.getChildLayoutPosition(view);
                final ToDoItem item = adapter.getItem(position);

                final Dialog dialog = new Dialog(ItemActivity.this);
                dialog.setOptions(getString(update), getString(R.string.delete)).setTitle(R.string.options);
                dialog.setPositiveButton(R.string.cancel).setTitle(R.string.options).show();
                dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                        final Dialog innerDialog = new Dialog(ItemActivity.this);

                        switch (i) {
                            case 0:
                                innerDialog.setTitle(R.string.update).setMessage(R.string.want_delete);
                                innerDialog.setEdit(getString(R.string.item_hint), item.getDescription());
                                innerDialog.setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        item.setDescription(innerDialog.getEditText().getText().toString());

                                        adapter.update(position, item);
                                        Snack.show(floatingActionButton, R.string.item_updated);

                                    }
                                });

                                innerDialog.setNegativeButton(R.string.cancel).show();
                                break;

                            case 1:
                                innerDialog.setTitle(R.string.delete).setMessage(R.string.want_delete);
                                innerDialog.setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        adapter.remove(position);
                                    }
                                });

                                innerDialog.setNegativeButton(R.string.cancel).show();
                                break;

                        }
                    }
                });

                return true;
            }
        });
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.fab:
                final Dialog dialog = new Dialog(ItemActivity.this)
                        .setTitle(R.string.add).setMessage(R.string.want_new_item)
                        .setEdit(getString(R.string.item_hint), null);
                dialog.setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.add(new ToDoItem(dialog.getEditText().getText().toString()));
                        Snack.show(floatingActionButton, R.string.item_created);

                    }
                }).setNegativeButton(R.string.cancel).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
