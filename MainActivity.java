package com.glide;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import adapters.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;//声明一个视图容器
    RecyclerAdapter adapter;//声明视图容器的适配器
    SwipeRefreshLayout fresher;//声明一个容器，它拥有相当方便的刷新api
    List<String> urls;//声明一个对象数组，用来放置recyclerview容器中的item们所需要的各种信息
    LinearLayoutManager manager;//声明一个recyclerview要用的布局管理器，它决定了recycleview的item们的展现位置或方式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urls = new ArrayList<>();//声明一个对象数组，用来存储recyclerview的Item中需要展示的图片链接，文字等等，本例中存储的是图片链接的字符串
        urls.addAll(getUrls());//数组的指向不能变，只能在urls初次指向的内存地址上增删元素，要避免将url指向其他新的内存空间，这样才能保证后面一些api运行正常
        recyclerView = findViewById(R.id.recycler);
        fresher = findViewById(R.id.fresher);
        adapter = new RecyclerAdapter(urls,getApplicationContext());//new出一个自定义过后的适配器
        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        fresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//swipeRefreshLayout中的方法，无法下拉仍要下拉时，会出现刷新动画，
            @Override                                                             //并触发onRefresh方法，你可以在该方法中写入刷新的具体动作
            public void onRefresh() {
                urls.clear();//保证urls变量不指向其他变量的情况下，将对象数组清空
                urls.addAll(getUrls());//重新添加新的数组元素进入原本的内存地址，这里我调用的getUrls()方法会返回一个大小为10的字符串数组，字符串都代表着图片路径
                adapter.notifyDataSetChanged();//更新接收到的变动的数据集合，前提时之前创建的对象数组的内存指向没有变化。
                fresher.setRefreshing(false);//将刷新动画停止
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {//添加recyclerview滑动监听
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING||newState ==RecyclerView.SCROLL_STATE_IDLE){//newState参数会包含当前recyclerview最新的状态
                    //如果当前状态为拖动(RecyclerView.SCROLL_STATE_DRAGGING)或者停止(RecyclerView.SCROLL_STATE_IDLE)时，则继续进行下面的判断
                   if (manager.findLastVisibleItemPosition()>=urls.size()-2){//当前可见的item的位置（最小值为0）如果快要到达对象数组的总大小（最小值为1）了，则开始加载新的数据并展示

                       urls.addAll(getUrls());//添加了新的数据进去，你也可以通过改成网络请求的方法获得真正的数据
                       adapter.notifyDataSetChanged();//刷新recyclerview中数据的展示，前提是数组指向的内存地址一直没有变化

                        //注意图片等大体积资源可能导致的内存溢出问题！！！
                        //图片的展示我写在适配器中的onBindViewHolder()方法，用的是Glide库

                   }

                }
            }
        });

    }



    private List<String> getUrls(){
        List<String> urls = new ArrayList<>();
        urls.add("https://images.pexels.com/photos/2395255/pexels-photo-2395255.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        urls.add("https://images.pexels.com/photos/2339038/pexels-photo-2339038.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        urls.add("https://images.pexels.com/photos/2443865/pexels-photo-2443865.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2440009/pexels-photo-2440009.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2334531/pexels-photo-2334531.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2394446/pexels-photo-2394446.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2232112/pexels-photo-2232112.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2377474/pexels-photo-2377474.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2437293/pexels-photo-2437293.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://images.pexels.com/photos/2442773/pexels-photo-2442773.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        return urls;
    }
}
