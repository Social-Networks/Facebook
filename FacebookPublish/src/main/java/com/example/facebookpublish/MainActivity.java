/**
 * © 2014 Ifraag Campaign. All rights reserved. This code is only licensed and owned by Ifraag Campaign.
 * Please keep this copyright information if you are going to use this code.
 */

package com.example.facebookpublish;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.facebookclient.FacebookClient;
import com.facebook.Session;

public class MainActivity extends ActionBarActivity {

    /* An instance of facebook client that handles all required communication with facebook server. */
    private FacebookClient fbClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        /* Get current location information (longitude & latitude) of the user */
        Location myLocation = getUserLocation();

        /* An instance of FacebookView interface to implement how would my layout views change as result for facebook session
        state changes. */
        MyFBView myFacebookView = new MyFBView();
        fbClient = new FacebookClient(this, myFacebookView, myLocation );
        fbClient.activateSession(savedInstanceState);
        fbClient.getUserProfileInformation();
        fbClient. getFacebookPageId(myLocation);
    }


    @Override
    public void onStart() {
        super.onStart();

        /* Add session callback that will be called in case session state is changed */
        fbClient.getSession().addCallback(fbClient.getStatusCallback());
    }

    @Override
    public void onStop() {
        super.onStop();
        /* Add session callback that will be called in case session state is changed */
        fbClient.getSession().addCallback(fbClient.getStatusCallback());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Update active session with active permission whether it is read or publish permission */
        fbClient.getSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FacebookClient.PENDING_PUBLISH_KEY, fbClient.isPendingPublishReauthorization());
        /*Session session = Session.getActiveSession();*/
        Session.saveSession(fbClient.getSession(), outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            Log.d("MyFBClient","Settings button is pressed");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }


    }

    /* Create an instance from FacebookView interface implementing updateLayoutViews method that should be called whenever it is
    * required to update the view contents of your layout. In fact, it will be called from facebook session state change callback. */
    private class MyFBView implements FacebookClient.FacebookView {
        @Override
        public void updateLayoutViews() {
            /* Do nothing in the UI when the session status changes. Everything will be sent without any need to user interaction */

            Session session = fbClient.getSession();
            if (session.isOpened()) {
                Log.i("MyFBClient","State of session is "+ session.getState());
            } else {
                Log.i("MyFBClient","Session is closed");
            }
        }
    }

    private Location getUserLocation(){

        /* Coordinates of current location, they are obtained by GPS/WiFi location sensors in user's phone */
        Location myLocation;

        /* TODO: Use Phone sensors and set default location attributes to Cairo where Latitude is 30.0380279 & Longitude is 31.2405339 */
        myLocation  =  new Location("");
        myLocation.setLatitude(30.0380279);
        myLocation.setLongitude(31.2405339);

        return myLocation;
    }
}