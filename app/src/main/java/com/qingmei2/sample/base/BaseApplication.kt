package com.qingmei2.sample.base

import android.app.Application
import com.facebook.stetho.Stetho
import com.qingmei2.architecture.core.logger.initLogger
import com.qingmei2.sample.BuildConfig
import com.squareup.leakcanary.LeakCanary
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        initLogger(BuildConfig.DEBUG)
        initStetho()
        initLeakCanary()

        val imqaOption = io.imqa.core.IMQAOption()
        imqaOption.setBuildType(false)
        imqaOption.setUploadPeriod(true)
        imqaOption.setKeepFileAtUploadFail(false)
        imqaOption.setDumpInterval(10000)
        imqaOption.setFileInterval(5)
        imqaOption.setHttpTracing(true)
        imqaOption.setCrashCollect(true)
        imqaOption.setPrintLog(true)
        imqaOption.setServerUrl("https://stg-collector.imqa.io/")
        imqaOption.setCrashServerUrl("https://stg-collector.imqa.io/")
        io.imqa.mpm.IMQAMpmAgent.getInstance()
            .setOption(imqaOption) // MPM 의 동작 방식을 정하는 옵션을 설정합니다.
            .setContext(this, "") // Application Context 를 초기화 합니다.
            .setProjectKey("\$2b\$05\$Qo5vFOSaCIfyI2HoDs.g1.10btXQAb2l3UvwVpC5S3yq4/v6mDvBO^#1U8389ATPE/szowZGlK27A==") // IMQA MPM Client 의 Project Key 를 설정합니다.
            .init()
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
    }
}
