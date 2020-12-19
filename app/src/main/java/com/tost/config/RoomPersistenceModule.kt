package com.tost.config

import android.content.Context
import androidx.room.Room
import com.tost.data.dao.RecordsDao
import com.tost.data.db.TostDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created By Malibin
 * on 12월 13, 2020
 */

@Module
@InstallIn(ApplicationComponent::class)
object RoomPersistenceModule {

    @Singleton
    @Provides
    fun provideRecordsDao(tostDataBase: TostDataBase): RecordsDao {
        return tostDataBase.recordsDao()
    }

    @Singleton
    @Provides
    fun provideTostDataBase(@ApplicationContext context: Context): TostDataBase {
        return Room.databaseBuilder(context, TostDataBase::class.java, "tostDatabase").build()
    }
}
