package softikoda.com.contactsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import Database.DataObjects;
import Database.ManageContacts;

public class MyContacts extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    ManageContacts manageContacts;
    private  final Context context=this;
    ArrayList<String> deleteIDS = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent = new Intent(MyContacts.this,AddContact.class);
                startActivity(intent);
            }
        });


        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefresh) ;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayData();
            }
        });



        displayData();

    }
    public void displayData(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet(),context,manageContacts);
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    private ArrayList<DataObjects> getDataSet() {
        ManageContacts contacts = new ManageContacts(this);
      ArrayList<HashMap<String,String>> loadedData = contacts.getAllContacts();

        ArrayList results = new ArrayList<DataObjects>();
        for (int index = 0; index < loadedData.size(); index++) {
          HashMap<String,String> loadedContact=loadedData.get(index);
            DataObjects obj = new DataObjects(loadedContact.get("id"),
                    loadedContact.get("name"),loadedContact.get("phone_no"),loadedContact.get("image_url"),loadedContact.get("email"));
            results.add(index, obj);
        }
        return results;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
Toast.makeText(getApplicationContext(),"to delete",Toast.LENGTH_SHORT).show();

       String[] toDeleteIds=getToDelete();
            for (int i=0;i<toDeleteIds.length;i++){
                Log.d("data is : ",toDeleteIds[i]);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String[] getToDelete(){
        String[] toDelete = new String[deleteIDS.size()];
        deleteIDS.toArray(toDelete);

        return toDelete;
    }

    public void receiveToDelete(ArrayList<String> ids){
        deleteIDS.clear();
        this.deleteIDS = ids;
    }
}
