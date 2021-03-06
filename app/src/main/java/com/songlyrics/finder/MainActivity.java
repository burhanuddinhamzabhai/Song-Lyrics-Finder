package com.songlyrics.finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * String array containing items for drawer
     */
    private String[] mNavigationDrawerItemTitles;
    /**
     * Drawer layout object
     */
    private DrawerLayout mDrawerLayout;
    /**
     * list view for showing items inside drawer
     */
    private ListView mDrawerList;
    /**
     * Toolbar object as we want to replace actionBar
     */
    Toolbar toolbar;

    Menu menu;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    FrameLayout contentFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        contentFrame=findViewById(R.id.content_frame);

        /**
         * calling method for setting up toolbar on create
         */
        setupToolbar();

        //setting up drawer
        DataModel[] drawerItem = new DataModel[2];
        //passing selection value
        drawerItem[0] = new DataModel( getResources().getString(R.string.home));
        drawerItem[1] = new DataModel(getResources().getString(R.string.fav));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        /**
         * loading default fragment on start
         * passing song lyrics search activity as default
         */
        loadFragment(new FragmentMain());

    }





    /**
     * setting up default fragment
     * @param fragment this will set default activity as
     *                 fragmentMain.class when open app
     *
     */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    /**
     * class implementing logic for clicks of users on drawer item
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    /**
     *
     * @param position this variable will get value from
     *                 DrawerItem click listener, which
     *                 will be 0 or 1, that is Home or
     *                 favorites
     */
    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new FragmentMain();
                break;
            case 1:
                fragment = new FragmentFavorites();
                break;
            default:
                break;
        }
        /**
         * setting up content frame on item selection
         */
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            Log.e("MainActivity", getResources().getString(R.string.fragment_error));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     *
     * @param item this will contain value of selected item
     * @return and will return selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.help){
            final AlertDialog.Builder help = new AlertDialog.Builder(this);
            help.setTitle(getResources().getString(R.string.help));
            help.setMessage(getResources().getString(R.string.helpM));
            help.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
help.show();
        }else
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     *
     * @param title this will contain title of fragment
     *              we will use it to set title of action bar
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     *
     * @param savedInstanceState saves current state value
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * setting up toolbar
     */
    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * changing state of drawer icon on toggle
     */
    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

}