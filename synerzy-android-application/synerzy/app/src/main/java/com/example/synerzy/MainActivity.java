package com.example.synerzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView mRecyclerView;
    private fileEntryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton searchButton;
    static String CURRENT_ID = "yetToOpen";
    final ArrayList<fileEntry> entryList = new ArrayList<>();
    HashMap<String,String> filesYet = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        System.out.println("ROH : ip is" + ip);

        appStartActions asa = new appStartActions(this , ip);
        asa.execute();

        mDrawerLayout=(DrawerLayout)findViewById(R.id.navigation_drawerlayout);
        NavigationView navigationView=findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.Open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView  = findViewById(R.id.rvFileEntry);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new fileEntryAdapter(entryList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new fileEntryAdapter.OnItemClickListener() {
            @Override
            public void OnItemCick(int position) {
//                entryList.get(position).changeName("will be");
//                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void OnDownloadClick(int position , ImageButton download_button, ProgressBar download_bar) {
                System.out.println("ROH : "+ entryList.get(position) + " is to be downloaded");
                downloadOnClick(entryList.get(position).getFileName() , download_button , download_bar);
            }
        });

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent floatingIntent= new Intent(MainActivity.this,Main2Activity.class);
                startActivity(floatingIntent);
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("central_list");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if(filesYet.containsKey(ds.getKey().toString()) == false) {
                        filesYet.put(ds.getKey().toString() , ds.getValue().toString());
                        entryList.add(new fileEntry(ds.getKey().toString(), ds.getValue().toString()));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = findViewById(R.id.searchBar);
                modifyHashMapSearch(searchText.getText().toString());
            }
        });
        Button fullListButton = findViewById(R.id.fullListButton);
        fullListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyHashMapFullList();
            }
        });

    }
    public boolean isMatch(String s , String t){
        for(int i =0;i < s.length();i++){
            int match = 0;
            while(match < t.length() && i+match < s.length() && s.charAt(i+match) == t.charAt(match)){
                match++;
            }
            if(match >= 3 || 2*match > t.length() ){
                return true;
            }
        }
        return false;
    }

    public void modifyHashMapSearch(String keyWord){
        entryList.clear();
        for(Map.Entry<String , String> pa : filesYet.entrySet()){
            if(isMatch(pa.getKey() , keyWord) == true){
                entryList.add(new fileEntry(pa.getKey() , pa.getValue()));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void modifyHashMapFullList(){
        entryList.clear();
        for(Map.Entry<String , String> pa : filesYet.entrySet()){
                entryList.add(new fileEntry(pa.getKey() , pa.getValue()));
                mAdapter.notifyDataSetChanged();
        }
    }
    public void downloadOnClick(String fileName,ImageButton download_button, ProgressBar download_bar){
        System.out.println("ROH : "+ fileName + " is to be downloaded");
        DownloadClass dc = new DownloadClass(this , fileName , download_button , download_bar);
        dc.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.musicPlayer1:
                Intent musicIntent= new Intent(MainActivity.this,musicPlayer.class);
                startActivity(musicIntent);
                break;
            case R.id.videoPlayer:
                Intent videoIntent=new Intent(MainActivity.this,videoPlayer.class);
                startActivity(videoIntent);
                break;
        }
        return false;
    }
}
