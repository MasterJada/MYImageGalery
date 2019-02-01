package com.envionsoftware.upworktest


import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import com.envionsoftware.upworktest.adapters.MainPagerAdapter
import com.envionsoftware.upworktest.mvp.MainModel
import com.envionsoftware.upworktest.mvp.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){






    private val model = MainModel()
    private val presenter = MainPresenter(model)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pagerAdapter = MainPagerAdapter(supportFragmentManager, this)
        main_pager.adapter = pagerAdapter
        main_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                when (p0) {
                    0 -> fab.show()
                    2 -> fab.hide()
                }
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(page: Int) {
                when (page) {
                    0 -> {
                        fab.setImageResource(R.drawable.ic_photo_camera)
                        fab.setOnClickListener { presenter.takePhoto() }
                    }
                    else -> {
                        fab.setImageResource(R.drawable.ic_add)
                        fab.setOnClickListener { presenter.addAlbum() }
                    }
                }
            }

        })
        fab.setOnClickListener { presenter.takePhoto() }
        presenter.attachView(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        menu?.findItem(R.id.search_bar)?.let {menuItem ->
            (menuItem.actionView as? SearchView)?.let {searchView ->
            searchView.setOnQueryTextFocusChangeListener(presenter)
            searchView.setOnQueryTextListener(presenter)
            }
        }

        return true
    }


    fun getPresenter(): MainPresenter {
        return presenter
    }
    override fun onStart() {
        super.onStart()
        presenter.viewReady()
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter.onRequestPermissions(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }




    fun changeFragment(pos: Int) {
        main_pager.setCurrentItem(1, true)
    }


}
