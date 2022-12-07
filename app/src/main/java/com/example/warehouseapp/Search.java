package com.example.warehouseapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Gives the user a list of all the values inside the database
 */
public class Search extends AppCompatActivity {

    private int val=0;

    AutoCompleteTextView textsearch;
    RecyclerView recyclerView;
    DatabaseReference database;
    com.example.warehouseapp.MyAdapter myAdapter;
    ArrayList<com.example.warehouseapp.Item> list;
    private SearchView searchView;
    /**
     * Initialize values, and add eventListeners for the activity's functionality
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Initialize values
        recyclerView = findViewById(R.id.listData);
        searchView=findViewById(R.id.searchView);
        searchView.clearFocus();
        // establish connection to the database
        database = FirebaseDatabase.getInstance().getReference("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        final ArrayList<String> list1 = new ArrayList<>();
        myAdapter = new com.example.warehouseapp.MyAdapter(Search.this, list);
        recyclerView.setAdapter(myAdapter);

searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterList(newText);
        return true;
    }
});

        database.addValueEventListener(new ValueEventListener() {

            /**
             * will go through the realtime database in firebase and get key to a location
             *
             * Since we have 3 additional paths in our data we will have to repeat this process 4 more times to reach the point storing the data
             *
             * @param snapshot will create a copy of the data in the firebase realtime database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (final DataSnapshot dataSnapshot : snapshot.getChildren()){


                    final String user = dataSnapshot.getKey();

                    // establish connection to the database and get reference to the next path
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Items").child(user);
                    ValueEventListener eventListener = new ValueEventListener() {

                        /**
                         * will go through the realtime database in firebase and get key to a location
                         *
                         * @param snapshot will create a copy of the data in the firebase realtime database
                         */
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(final DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                final String user1 = dataSnapshot1.getKey();

                                // establish connection to the database and get reference to the next path
                                DatabaseReference re = FirebaseDatabase.getInstance().getReference().child("Items").child(user).child(user1);
                                ValueEventListener eventListener1 = new ValueEventListener() {

                                    /**
                                     * will go through the realtime database in firebase and get key to a location
                                     *
                                     * @param snapshot will create a copy of the data in the firebase realtime database
                                     */
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                            String user2 = dataSnapshot2.getKey();


                                            // get reference to the path in the realtime database holding the values that describe an item
                                            DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Items").child(user).child(user1).child(user2);
                                            ValueEventListener eventListener2 = new ValueEventListener() {

                                                /**
                                                 * will go through the realtime database in firebase and get key to a location
                                                 *
                                                 * @param snapshot will create a copy of the data in the firebase realtime database
                                                 */
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot dataSnapshot3 : snapshot.getChildren()){
                                                        // will iterate through the values stored and retrieve them all
                                                        String item = dataSnapshot3.getValue().toString();

                                                        // will add item object created from the retrieved data from dataSnapshot3
                                                        list1.add(item);

                                                        // this will iterate through list1 and only get one items information by stopping after the 8th
                                                        // element since an item should only have 8 characteristics
                                                        val++;
                                                        if(val%8 == 0) {
                                                            String name = list1.get(4);
                                                            String brand = list1.get(0);
                                                            String quant = list1.get(6);
                                                            String price = list1.get(5);
                                                            String color = list1.get(1);
                                                            String condition = list1.get(3);
                                                            String comments = list1.get(2);

                                                            // use the item constructor from Item class to add the data for one product into the list
                                                            com.example.warehouseapp.Item product2 = new com.example.warehouseapp.Item(name, brand, condition, quant, price, color, comments);


                                                            // will add the object product to the list
                                                            list.add(product2);

                                                            //clear the list after data is added to the Arraylist list
                                                            list1.clear();
                                                        }



                                                        //Toast.makeText(Search.this, dataSnapshot3.getValue().toString(),Toast.LENGTH_SHORT).show();
                                                    }
                                                    // update data
                                                    myAdapter.notifyDataSetChanged();
                                                }

                                                /**
                                                 *
                                                 * @param error
                                                 */
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            };
                                            r.addListenerForSingleValueEvent(eventListener2);
                                        }
                                    }

                                    /**
                                     *
                                     * @param error
                                     */
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                re.addListenerForSingleValueEvent(eventListener1);
                            }
                        }

                        /**
                         *
                         * @param error
                         */
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    ref.addListenerForSingleValueEvent(eventListener);
                }
            }

            /**
             *
             * @param error
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.searchMenu);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);

                return true;
            }


    });
        return super.onCreateOptionsMenu(menu);
    }

    private void filterList(String newText) {
                ArrayList<Item> filterList = new ArrayList<>();
                for (Item x : list) {
                    if (x.getName().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT)))
                    {
                        filterList.add(x);
                    }
                }
                if(filterList.isEmpty()){
                    Toast.makeText(this, "No Data Find", Toast.LENGTH_SHORT).show();
                }
                else {
                  myAdapter.setFilteredList(filterList);
                }
    }


}