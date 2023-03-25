package kg.rkd.carstestapplication.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kg.rkd.carstestapplication.data.BillingRepository

class UnSubWorker(
    context: Context,
    params: WorkerParameters,
    private val billingRepository: BillingRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        billingRepository.unSub(BillingRepository.Products.SUBSCRIPTION)
        return Result.success()
    }
}