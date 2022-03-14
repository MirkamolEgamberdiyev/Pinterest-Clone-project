package com.mirkamol.pinterestclonemyproject.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.Person.fromBundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.media.AudioAttributesCompat.fromBundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinterest.helper.EndlessRecyclerViewScrollListener
import com.mirkamol.pinterestclonemyproject.R
import com.mirkamol.pinterestclonemyproject.adapter.SearchPhotoAdapter
import com.mirkamol.pinterestclonemyproject.model.search.Detail
import com.mirkamol.pinterestclonemyproject.model.search.ResponseSearch
import com.mirkamol.pinterestclonemyproject.networking.ApiClient
import com.mirkamol.pinterestclonemyproject.networking.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagePresenterFragment : Fragment(R.layout.fragment_image_presenter) {

    private lateinit var edtSearch: EditText
    lateinit var ivCancel: ImageView
    val args: ImagePresenterFragmentArgs by navArgs()
    private lateinit var navController: NavController
    lateinit var list: ArrayList<Detail>
    lateinit var rvSearchPhotos: RecyclerView
    var searchPhotoAdapter = SearchPhotoAdapter()

    lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    companion object {
        private var PAGE = 1
        private var PER_PAGE = 20
    }

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = ApiClient(requireContext()).createServiceWithAuth(ApiService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initViews(view)


    }

    private fun initViews(view: View) {
        ivCancel = view.findViewById(R.id.ivCancel)

        edtSearch = view.findViewById(R.id.edtSearch)
        edtSearch.setText(args.name)
        edtSearch.post {
            edtSearch.requestFocus()
            edtSearch.setSelection(edtSearch.text.toString().length)
        }
        getSearchResults(args.name)

        navController = Navigation.findNavController(view)
        rvSearchPhotos = view.findViewById(R.id.rvSearchPhotos)


        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvSearchPhotos.layoutManager = staggeredGridLayoutManager

        list = ArrayList()

        edtSearch.addTextChangedListener(textWatcher)

        rvSearchPhotos.adapter = searchPhotoAdapter

        addLoadingMore()

        controlClick()

        doCancelAction()

    }

    private fun controlClick() {
        searchPhotoAdapter.photoClick = {
            navController.navigate(
                R.id.action_imagePresenterFragment_to_datailFragment,
                bundleOf(
                    "photoID" to it.id,
                    "photoUrl" to it.urls.regular,
                    "description" to it.description
                )
            )
        }

    }


    private fun addLoadingMore() {
        val scrollListener = object : EndlessRecyclerViewScrollListener(
            staggeredGridLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getSearchResults(queryText ?: "")
            }
        }
        rvSearchPhotos.addOnScrollListener(scrollListener)

    }

    private fun hideKeyboard(activity: Activity, viewToHide: View) {
        val imm = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewToHide.windowToken, 0)
    }

    private fun doCancelAction() {
        ivCancel.setOnClickListener {
            navController.navigate(R.id.action_imagePresenterFragment_to_searchFragment2)
        }
    }

    private fun getSearchResults(query: String) {
        apiService.searchPhotos(query, PAGE++, PER_PAGE)
            .enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    searchPhotoAdapter.submitData(response.body()!!.results)
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                }
            })
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            queryText = p0.toString()
            getSearchResults(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    private var queryText: String? = null


}