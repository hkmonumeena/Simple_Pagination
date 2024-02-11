package com.ruchitech.simplepagination

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<User>?>(emptyList())
    val user: StateFlow<List<User>?> = _users.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private lateinit var appDatabase: AppDatabase
    private val pageSize = 30
    var currentPage = mutableIntStateOf(0)
    var hasMoreData = true

    private val minimumTimeDifference: Long =
        3000L  // Set your desired minimum time difference in milliseconds
    private var lastEventTime: Long = 0


    fun initAppDatabase(context: Context) {
        appDatabase = DatabaseModule.provideAppDatabase(context)
    }

    init {
        android.os.Handler().postDelayed({
            fetchUsers()
        }, 1000)
    }


    fun insertDummyUsers() {
        viewModelScope.launch {
            val listTemp = arrayListOf<User>()
            for (i in 0 until 5000) {
                listTemp.add(User(name = "User $i", age = i + i))
            }
            appDatabase.userDao().insertAll(listTemp)
        }
    }

    fun loadMoreUsers() {
        if (hasMoreData) {
            val currentTimestamp = System.currentTimeMillis()
            // Check if enough time has passed since the last load
            if (currentTimestamp - lastEventTime >= minimumTimeDifference) {
                _loading.value = true
                currentPage.intValue = currentPage.intValue + 1
                lastEventTime = currentTimestamp
                fetchUsers()
            }

        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            val pageNum = currentPage.intValue * pageSize
            val getData = appDatabase.userDao().getUsersWithPagination(pageSize, pageNum)
            if (getData.isNotEmpty()) {
                if (getData.size < pageSize) {
                    hasMoreData = false
                }
                if (currentPage.intValue != 0) {
                    kotlinx.coroutines.delay(3000)
                }
                _loading.value = false
                _users.value = user.value?.plus(getData)
            }
        }
    }

}