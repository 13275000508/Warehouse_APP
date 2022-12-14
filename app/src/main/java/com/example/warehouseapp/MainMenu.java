package com.example.warehouseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Samir Macias, MArk Perez, Megan Jen
 * @version 1.0
 * Is the 'Main Menu' of the application where all the potential options and or features to use are shown to the user
 */
public class MainMenu extends AppCompatActivity {

    @Override
	/**
     * Creates the activity's layout
	* @param savedInstanceState
	**/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    /**
     * Calls the AddItem class
	* @param obj is the button that was clicked
	**/
    public void addItem(View obj)
    {
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
	/**
     * Calls the Scan In class
	* @param obj is the button that was clicked
	**/
    public void scanIn(View obj)
    {
        Intent intent  = new Intent(this, ScanIn.class);
        startActivity(intent);
    }

    /**
     * Calls the Scan Out class
     * @param obj is the button that was clicked
     */
    public void scanOut(View obj)
    {
        Intent intent  = new Intent(this, ScanOut.class);
        startActivity(intent);
    }

    /**
     * Calls the Login class
	* @param view is the button that was clicked
	**/
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    /**
     * Calls the PriceQuantity class
     * @param obj is the button that was clicked
     */

    public void search(View obj){
        Intent intent = new Intent(this, PriceQuantity.class);
        startActivity(intent);
    }
}

