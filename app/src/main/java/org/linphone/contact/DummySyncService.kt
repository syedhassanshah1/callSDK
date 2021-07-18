
package org.linphone.contact

import android.accounts.Account
import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder

// Below classes are required to be able to use our own contact MIME type entry...
class DummySyncAdapter(context: Context, autoInit: Boolean) : AbstractThreadedSyncAdapter(context, autoInit) {
    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) { }
}

class DummySyncService : Service() {
    companion object {
        private val syncAdapterLock = Any()
        private var syncAdapter: DummySyncAdapter? = null
    }

    override fun onCreate() {
        synchronized(syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = DummySyncAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return syncAdapter?.syncAdapterBinder
    }
}
