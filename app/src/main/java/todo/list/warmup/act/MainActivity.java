package todo.list.warmup.act;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.drakeet.materialdialog.MaterialDialog;
import todo.list.warmup.R;
import todo.list.warmup.adpt.ToDoListAdapter;
import todo.list.warmup.bean.ToDoList;
import todo.list.warmup.dia.Dialog;
import todo.list.warmup.view.DividerItemDecoration;

import static todo.list.warmup.R.id.fab;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ToDoListAdapter adapter;


    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);


        bindViews();
        customConfiguration();
        bindListeners();
        setup();


        for (int i = 1; i <= 5; i++) {
            adapter.add(new ToDoList("List Example " + i));
        }

    }

    private void bindViews() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.floatingActionButton = (FloatingActionButton) findViewById(fab);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void customConfiguration() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setTitle(R.string.main_title);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void bindListeners() {
        floatingActionButton.setOnClickListener(this);

    }

    private void setup() {
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(MainActivity.this,user.getUid(),Toast.LENGTH_LONG).show();
                    Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("AUTH", "onAuthStateChanged:signed_out");
                }
            }
        };

        this.adapter = new ToDoListAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);

                Intent i = new Intent(MainActivity.this, ItemActivity.class);
                i.putExtra(ItemActivity.SELECTED_LIST_ID, adapter.getItem(position).getId());
                startActivity(i);
            }
        });

        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = recyclerView.getChildLayoutPosition(view);

                final MaterialDialog dialog = new MaterialDialog(MainActivity.this)
                        .setTitle(R.string.delete).setCanceledOnTouchOutside(true)
                        .setMessage(R.string.want_delete);

                dialog.setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        adapter.remove(position);

                    }
                }).setNegativeButton(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.fab:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle(R.string.add).setMessage(R.string.want_new_list);
                dialog.setEdit(getString(R.string.list_hint), null);
                dialog.setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.add(new ToDoList(dialog.getEdit().getText().toString()));
                        Snackbar.make(view, R.string.list_created, Snackbar.LENGTH_LONG)
                                .setAction(android.R.string.ok, null).show();


                    }
                });
                dialog.setNegativeButton(R.string.cancel).show();
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);


        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                            Toast.makeText(MainActivity.this, getString(R.string.auth_fail),
                                    Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }



}
