package com.mirkamol.pinterestclonemyproject.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mirkamol.pinterestclonemyproject.R
import com.mirkamol.pinterestclonemyproject.adapter.searchAdapter
import com.mirkamol.pinterestclonemyproject.model.search.Topic
import com.mirkamol.pinterestclonemyproject.networking.ApiClient
import com.mirkamol.pinterestclonemyproject.networking.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(R.layout.fragment_search), searchAdapter.OnItemClickListener {
    lateinit var adapter: searchAdapter
    private lateinit var list: ArrayList<Topic>
    lateinit var recyclerView: RecyclerView
    lateinit var rvPopular: RecyclerView
    private lateinit var apiService: ApiService
    lateinit var editText: EditText
    var page = 0
    var per_page = 8
    private lateinit var navController: NavController
    private val TAG = "search Fragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiClient(requireContext()).createServiceWithAuth(ApiService::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_ideas)
        rvPopular = view.findViewById(R.id.rv_popular)
        initViews()
        editText = view.findViewById(R.id.et_search)
        navController = Navigation.findNavController(view)

        editText.setOnClickListener {
            navController.navigate(R.id.action_searchFragment2_to_searchResultFragment)
        }

    }

    private fun initViews() {

        list = ArrayList()



        adapter = searchAdapter(requireContext(), list, this)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = adapter
        rvPopular.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvPopular.adapter = adapter
        apiTopicsList()
    }

    private fun apiTopicsList() {

   apiService.getTopics(page, per_page)
            .enqueue(object : Callback<List<Topic>> {
                override fun onResponse(
                    call: Call<List<Topic>>,
                    response: Response<List<Topic>>
                ) {
                    list.addAll(response.body() as ArrayList<Topic>)
                    adapter.notifyDataSetChanged()
                    Log.d("TAG", response.body().toString())
                }

                override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                    Log.d("error", t.localizedMessage)
                }
            })

    }

    override fun onItemClicked(text: String) {

       // navController.navigate(R.id.action_searchFragment2_to_imagePresenterFragment)
        val action = SearchFragmentDirections.actionSearchFragment2ToImagePresenterFragment(text)
        findNavController().navigate(action)

//        findNavController().navigate(R.id.action_searchFragment2_to_imagePresenterFragment, Bundle().apply {
//            putString("ImageName", text)
//        })
    }


}