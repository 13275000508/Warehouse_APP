package com.example.warehouseapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Displays all the items that have been removed from the database
 */
public class Inventory2 extends AppCompatActivity {
    private static int val = 0;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> finalList = new ArrayList<>();
    private ListView theList;
    private DatabaseReference reff;

    @Override
    /**
     * Scans the 'Removed' section of the database to return to the user all the items that have been scanned out
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory2);

        theList = findViewById(R.id.theList2);
        reff = FirebaseDatabase.getInstance().getReference().child("Removed"); // reference to our database

        //the following retrieves "Items" children, which are the names of the items
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String name = ds.getKey();

                    //------------------------------------------------------------------------------------------------------------------------
                    // we are now referencing the child's child by its name (Ex: Items>>iphone 8)
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Removed").child(name);
                    ValueEventListener eventListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds2 : snapshot.getChildren() )
                            {
                                final String name2 = ds2.getKey();
                                //-----------------------------------------------------------------------------------------------------------
                                DatabaseReference re = FirebaseDatabase.getInstance().getReference().child("Removed").child(name).child(name2);
                                ValueEventListener eventLister3 = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        for(DataSnapshot ds3 : snapshot2.getChildren())
                                        {
                                            String name3 = ds3.getKey();
                                            // ----------------------------------------------------------------------------------------------
                                            DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Removed").child(name).child(name2).child(name3);
                                            r.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot ds4 : snapshot.getChildren())
                                                    {
                                                        String details = ds4.getValue().toString();
                                                        //   Toast.makeText(Inventory.this,"|"+ details+"|", Toast.LENGTH_SHORT).show();  // displays toast, displaying name, helping user know that the item scanned it correct

                                                        //  newList = new DateFormatSymbols().getMonths();
                                                        list.add(details);
                                                        checkList(list);
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                            // ----------------------------------------------------------------------------------------------
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                };
                                re.addListenerForSingleValueEvent(eventLister3);
                                //-------------------------------------------------------------------------------------------------------------
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    };
                    ref.addListenerForSingleValueEvent(eventListener2);
                    //---------------------------------------------------------------------------------------------------------------------------
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore potential errors!
            }
        };
        reff.addListenerForSingleValueEvent(eventListener);
    }

    /**
     * Every single value from the database is passed through this method.
     * After every 8 elements, it combines them together as a single string.
     * Where one item, is 8 elements. After being combined each item would have
     * a single string to be displayed. These strings are then placed into 'finalList'.
     * @param arr arraylist containing specific properties for each item from the database
     */
    public void checkList(ArrayList<String> arr)
    {
        ArrayList<String> newArray = new ArrayList<>();
        String value = "";

        val++;
        if(val%8 == 0)
        {
            for(int i = 0; i < 8; i++)
            {
                value = value + arr.get(i) + " \n";
            }
            arr.clear();
            finalList.add(value);
        }
    }

    /**
     * Called when the display button is clicked.
     * It adds the 'finalList' into the arrayAdpter in order for it to be placed within the ListView.
     * @param obj
     */
    public void testing(View obj)
    {
        Toast.makeText(Inventory2.this, "Items: "+ finalList.size(), Toast.LENGTH_SHORT).show();  // displays toast, displaying name, helping user know that the item scanned it correct\
        ArrayAdapter<String> arry = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, finalList);
        theList.setAdapter(arry); //displays each item in the database
    }


}