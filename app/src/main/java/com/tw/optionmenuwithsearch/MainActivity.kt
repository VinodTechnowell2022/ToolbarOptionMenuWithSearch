package com.tw.optionmenuwithsearch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tw.optionmenuwithsearch.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    val TAG :String = "MainActivity"
    var dataList: MutableList<StateTable> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title= "Home Screen"
        getData(dataList)

    }

    private fun getData(dataList: MutableList<StateTable>) {
        dataList.clear()
        dataList.add(StateTable(1, "Haryana", "HR"))
        dataList.add(StateTable(2, "Rajasthan", "RJ"))
        dataList.add(StateTable(3, "Punjab", "PN"))
        dataList.add(StateTable(4, "Gujrat", "Gj"))
        dataList.add(StateTable(5, "Uttar Pradesh", "UP"))
        dataList.add(StateTable(6, "Madhya Pradesh", "MP"))

        resetAdapter( dataList )

    }

    //here we are supply data to adapter
    private fun resetAdapter(dataList: MutableList<StateTable>) {
        mBinding.homeItemsGrid.removeAllViews()
        val adapter = StatesListAdapter(dataList)
        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false )
        mBinding.homeItemsGrid.layoutManager = layoutManager
        mBinding.homeItemsGrid.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        menuInflater.inflate(R.menu.new_search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView

        val searchHint = getString(R.string.search)
        searchView.queryHint = searchHint

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e(TAG, "onQueryTextSubmit: $query" )
                if (query.toString().length>2){
                    if (dataList.size > 0){
                        filterList(query.toString())
                    }else{
                        Toast.makeText(this@MainActivity, resources.getString(R.string.no_data), Toast.LENGTH_SHORT).show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(searchText: String): Boolean {
                Log.e(TAG, "onQueryTextChange: $searchText" )
                if (dataList.size>0){
                    filterList(searchText)
                }
                return false
            }
        })


        return super.onCreateOptionsMenu(menu)
    }

    private fun filterList(text: String) {
        //temp list
        val filteredList: ArrayList<StateTable> = ArrayList()

        if (dataList.size > 0){

            for (item in dataList) {
                if (item.name.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                } else if (item.code.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                }
            }
            resetAdapter(filteredList)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            finish()
           // logoutConformationAlert(this@MainActivity, resources.getString(R.string.logout_confirmation))
        }else if (item.itemId == R.id.action_logout){
            logoutConformationAlert(this@MainActivity, resources.getString(R.string.logout_confirmation))
        }else if (item.itemId == R.id.action_share){
            shareMyApp()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun shareMyApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
            var shareMessage = "\nHey!! Let me recommend you this application\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }

    private fun logoutConformationAlert(context: Activity, message: String){
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.app_name))
        builder.setIcon(ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
        builder.setMessage(message)
        builder.setCancelable(false)

        builder.setPositiveButton(resources.getString(R.string._ok)) { dialog, _ ->

            //clear your login session and database tables here
            dialog.dismiss()

//            logoutNav(context)
        }
        builder.setNegativeButton(resources.getString(R.string._cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

//    private fun logoutNav(context: Activity) {
//        val intent = Intent(context, LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        context.startActivity(intent)
//        context.finish()
//    }

}

data class StateTable( var id:Int, var name: String, var code:String)