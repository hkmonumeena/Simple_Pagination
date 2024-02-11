package com.ruchitech.simplepagination

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruchitech.simplepagination.ui.theme.SimplePaginationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initAppDatabase(this)

        setContent {
            SimplePaginationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserUi(viewModel)
                }
            }
        }
    }

}

@Composable
fun UserUi(viewModel: UserViewModel) {
    val data by viewModel.user.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn {
            item {
                Button(onClick = { viewModel.insertDummyUsers() }) {
                    Text(text = "Insert 500 Users")
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            itemsIndexed(data ?: emptyList()) { index, item ->
                UserHandle(item)
                if (index == (data?.size?.minus(1) ?: 0)) {
                    viewModel.loadMoreUsers()
                }
                if (isLoading && index == (data?.size?.minus(1) ?: 0)) {
                    LoaderItem()
                }
            }
        }
    }
}

@Composable
fun LoaderItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun UserHandle(item: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color.Red)
    ) {
        Text(text = item.name, fontSize = 18.sp, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Age: ${item.age}", fontSize = 12.sp, color = Color.White)
    }
}

