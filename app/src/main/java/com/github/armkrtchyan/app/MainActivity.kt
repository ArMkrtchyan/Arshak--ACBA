package com.github.armkrtchyan.app

import com.github.armkrtchyan.common.base.BaseActivityWithViewModel
import com.github.armkrtchyan.common.shared.extensions.getByResourceId
import com.github.armkrtchyan.app.databinding.ActivityMainBinding
import com.github.armkrtchyan.domain.models.RatesDomainModel
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.work.*
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MainActivity : BaseActivityWithViewModel<ActivityMainBinding, MainViewModel>() {

    override val mViewModel: MainViewModel
        get() = stateViewModel<MainViewModel>().value

    override val inflate: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            validate.setOnClickListener {
                validate()
                Log.i("ClickTag", "Clicked")
                mViewModel.getRates()
                mViewModel.getRates2()
                mViewModel.getRates3()
            }
        }
        mBinding.validate.apply {
            text = getByResourceId(R.string.app_name)
            backgroundTintList = getByResourceId(R.color.colorAccentLight)
            setTextColor(getByResourceId<ColorStateList>(R.color.colorPrimaryLight))
        }
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val getRatesRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<GetRatesWorker>().setConstraints(constraint).build()
        WorkManager.getInstance(this).enqueue(getRatesRequest)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(getRatesRequest.id).observe(this) {
            when (it.state) {
                WorkInfo.State.RUNNING -> {
                    Log.d("WorkManager", "RUNNING")
                }
                WorkInfo.State.ENQUEUED -> {
                    Log.d("WorkManager", "ENQUEUED")
                }
                WorkInfo.State.SUCCEEDED -> {
                    Log.d("WorkManager", "SUCCEEDED")
                    val res = Gson().fromJson(it.outputData.getString("result"), RatesDomainModel::class.java)
                    Log.d("WorkManager", "$res")
                }
                WorkInfo.State.FAILED -> {
                    Log.d("WorkManager", "FAILED")
                }
                WorkInfo.State.BLOCKED -> {
                    Log.d("WorkManager", "BLOCKED")
                }
                WorkInfo.State.CANCELLED -> {
                    Log.d("WorkManager", "CANCELLED")
                }
            }
        }
    }
}