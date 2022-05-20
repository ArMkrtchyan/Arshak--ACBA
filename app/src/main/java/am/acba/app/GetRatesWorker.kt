package am.acba.app

import am.acba.domain.repositories.RatesRepository
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

class GetRatesWorker(private val mRatesRepository: RatesRepository, context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        Log.d("WorkManager", "Work start")
        var res = Result.success()
        mRatesRepository.getRates().catch {
            res = Result.retry()
        }.collectLatest {
            res = Result.success(workDataOf("result" to Gson().toJson(it)))
        }
        Log.d("WorkManager", "Work finished")
        return res
    }
}