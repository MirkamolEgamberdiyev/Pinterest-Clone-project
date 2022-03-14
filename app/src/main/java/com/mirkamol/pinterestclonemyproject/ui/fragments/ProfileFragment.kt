package com.mirkamol.pinterestclonemyproject.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.mirkamol.pinterestclonemyproject.R
import com.mirkamol.pinterestclonemyproject.adapter.SavedPhotoAdapter
import com.mirkamol.pinterestclonemyproject.database.SavedDatabase
import com.mirkamol.pinterestclonemyproject.model.profile.User
import com.mirkamol.pinterestclonemyproject.networking.ApiClient
import com.mirkamol.pinterestclonemyproject.networking.service.ApiService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var apiService: ApiService
    private lateinit var ivProfile: ShapeableImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvFollow: TextView

    private lateinit var rvSaved: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var savedPhotoAdapter: SavedPhotoAdapter

    private lateinit var savedDatabase: SavedDatabase

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = ApiClient(requireContext()).createServiceWithAuth(ApiService::class.java)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

    }

    private fun initViews(view: View) {
        ivProfile = view.findViewById(R.id.ivProfile)
        getSave()

        tvFullName = view.findViewById(R.id.tvFullName)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvFollow = view.findViewById(R.id.tvFollow)

        rvSaved = view.findViewById(R.id.rvSaved)
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvSaved.layoutManager = staggeredGridLayoutManager
        savedPhotoAdapter = SavedPhotoAdapter()

        navController = Navigation.findNavController(view)

        savedDatabase = SavedDatabase.getInstance(requireContext())

        getSaved()

        controlClick()

    }

    private fun controlClick() {
        savedPhotoAdapter.photoClick = {
            navController.navigate(
                R.id.action_profileFragment2_to_datailFragment,
                bundleOf(
                    "photoID" to it.savedID,
                    "photoUrl" to it.url,
                    "description" to it.description
                )
            )
        }
    }

    private fun getSaved() {
        savedPhotoAdapter.submitData(savedDatabase.savedDao().getSaved())
        rvSaved.adapter = savedPhotoAdapter

    }

    private fun getSave() {
        apiService.getUser("mirkamol7907").enqueue(object:Callback<User>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<User>, response: Response<User>) {

                Log.d("Responce", response.body().toString())
                val user = response.body()!!

                Glide.with(requireContext()).load(user.profile_image.medium).into(ivProfile)
                tvFullName.text = user.name
                       tvUsername.text = "@${user.username}"
                       tvFollow.text = "${user.followers_count} followers · ${user.following_count} following"

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("error", t.localizedMessage)

            }

        })
    }
}