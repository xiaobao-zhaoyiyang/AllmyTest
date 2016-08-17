package yeeaoo.imooc_recyclerview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private List<String> mDatas;
    private SimpleAdapter mAdapter;
    private LoadDataScrollController mController;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProgressDialog pd;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews();

        /**
         * 创建控制器，同时使当前activity实现数据监听回调接口
         */
//        mController = new LoadDataScrollController(this);

        mAdapter = new SimpleAdapter(this, mDatas);
        recyclerView.setAdapter(mAdapter);
        // 设置RecyclerView的布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, position+"Long", Toast.LENGTH_SHORT).show();
            }
        });
        // 设置RecyclerView的Item间分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        int dividerHeight = dividerItemDecoration.getDividerHeight();
        recyclerView.addItemDecoration(dividerItemDecoration);

        /**
         * 设置监听
         */
//        recyclerView.addOnScrollListener(mController);

//        mSwipeRefresh.setOnRefreshListener(mController);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("MainActivity", "refresh");
                mSwipeRefresh.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int childCount = layoutManager.getChildCount(); // 显示的条目数
                int totalCount = layoutManager.getItemCount(); // 显示的总数据
                Log.i("MainActivity", "childCount = " + childCount + ", itemPosition = " + itemPosition + ", totalCount = " + totalCount);
                if (itemPosition >= totalCount - 1 && newState == RecyclerView.SCROLL_STATE_IDLE){
                    Log.i("MainActivity", "loadMore");
                    loadDatas();
                    Log.i("MainActivity", "loadDatas:" + mDatas.size());
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                itemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                Log.i("MainActivity", ", itemPosition = " + itemPosition);
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerView);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'I'; i++) {
            mDatas.add("" + (char)i);
        }
        Log.i("MainActivity", "initDatas:" + mDatas.size());
    }
    private void loadDatas() {
        for (int i = 0; i < 5; i++) {
            mDatas.add("" + i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                mAdapter.addData(1);
                break;
            case R.id.action_delete:
                mAdapter.delete(1);
                break;
            case R.id.action_listView:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_gridView:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.action_hor_gridView:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.action_staggered:
                Intent inent = new Intent(this, StaggeredGrideViewActivity.class);
                startActivity(inent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void refresh() {
//        Log.i("MainActivity", "refresh");
//        //刷新的接口调
//        mSwipeRefresh.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                mSwipeRefresh.setRefreshing(false);
//                mController.setLoadDataStatus(false);
//            }
//        },2000);
//    }

//    @Override
//    public void loadMore() {
//        Log.i("MainActivity", "loadMore");
//        //加载更多的接口回调
//        pd = new ProgressDialog(this);
//        pd.show();
//        mSwipeRefresh.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadDatas();
//                mAdapter.notifyDataSetChanged();
//                //设置数据加载结束的监听状态
//                mController.setLoadDataStatus(false);
//                pd.dismiss();
//            }
//        },2000);
//    }
}
