package com.mirkamol.pinterestclonemyproject.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pinterest.helper.EndlessRecyclerViewScrollListener
import com.mirkamol.pinterestclonemyproject.R
import com.mirkamol.pinterestclonemyproject.adapter.HomePhotoAdapter
import com.mirkamol.pinterestclonemyproject.model.homephoto.HomePhotoItem
import com.mirkamol.pinterestclonemyproject.networking.ApiClient
import com.mirkamol.pinterestclonemyproject.networking.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var homePhotoAdapter: HomePhotoAdapter
    private lateinit var rvHomePhotos: RecyclerView
    private lateinit var apiService: ApiService
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var navController: NavController
    lateinit var swipe: SwipeRefreshLayout

    private var PAGE = 1
    private var PER_PAGE = 20

    private lateinit var list: ArrayList<HomePhotoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = ApiClient(requireContext()).createServiceWithAuth(ApiService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe = view.findViewById(R.id.swipe)

        swipe.setOnRefreshListener(this)
        swipe.isRefreshing = true
        rvHomePhotos = view.findViewById(R.id.rvHomePhotos)
        navController = Navigation.findNavController(view)

        initViews()
    }

    private fun initViews() {

        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvHomePhotos.layoutManager = staggeredGridLayoutManager


        list = ArrayList()


        getPhotos()
        refreshAdapter(list)

        val scrollListener = object : EndlessRecyclerViewScrollListener(
            staggeredGridLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getPhotos()
            }
        }
        rvHomePhotos.addOnScrollListener(scrollListener)

        controlItemClick()
    }

    private fun controlItemClick() {
        homePhotoAdapter.photoClick = {
            navController.navigate(
                R.id.action_homeFragment_to_datailFragment  ,
                bundleOf("photoID" to it.id, "photoUrl" to it.urls.regular)
            )
        }
    }

    private fun refreshAdapter(list: ArrayList<HomePhotoItem>) {
        homePhotoAdapter = HomePhotoAdapter(list)
        rvHomePhotos.adapter = homePhotoAdapter
    }

    private fun getPhotos() {
        apiService.getPhotos(PAGE++, PER_PAGE).enqueue(object : Callback<List<HomePhotoItem>> {
            override fun onResponse(
                call: Call<List<HomePhotoItem>>,
                response: Response<List<HomePhotoItem>>
            ) {

                Log.d("responce", response.body().toString())
                list.addAll(response.body()!!)
                homePhotoAdapter.notifyDataSetChanged()
                swipe.isRefreshing = false
            }

            override fun onFailure(call: Call<List<HomePhotoItem>>, t: Throwable) {

                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun onRefresh() {
        initViews()
    }

}