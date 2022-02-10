package com.gsw.mvvmjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.gsw.mvvmjetpackcompose.ui.theme.MVVMJetpackComposeTheme
import com.gsw.mvvmjetpackcompose.viewmodel.UserViewModel
import com.valentinilk.shimmer.shimmer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVMJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App(
    viewModel: UserViewModel = hiltViewModel()
) {

    val users by viewModel.users.observeAsState(arrayListOf())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Random User")
            }, actions = {
                IconButton(onClick = { viewModel.addUser() }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            })
    }) {
        LazyColumn() {
            var itemCount = users.size
            if (isLoading) itemCount++

            items(count = itemCount) { index ->
                var auxIndex = index
                if (isLoading) {
                    if (auxIndex == 0)
                        return@items LoadingCard()
                    auxIndex--
                }
                val user = users[auxIndex]
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = rememberImagePainter(data = user.thumbnail,
                                builder = {
                                    placeholder(R.drawable.ic_round_person)
                                    error(R.drawable.ic_round_person)
                                }),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight
                        )
                        Space()
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "${user.name} ${user.lastName}")
                            Text(text = user.city)
                        }
                        Space()
                        IconButton(onClick = { viewModel.deleteUser(toDelete = user) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Remove")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .testTag("loadingCard")
    ) {

        Row(modifier = Modifier.padding(8.dp)) {
            ImageLoading()
            Space()
            Column {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .height(15.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                )
                Space()
                Box(
                    modifier = Modifier
                        .shimmer()
                        .height(15.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                )
            }
        }

    }
}

@Composable
fun ImageLoading() {
    Box(modifier = Modifier.shimmer()) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray)
        ) {

        }
    }
}

@Composable
fun Space(size: Int = 8) = Spacer(modifier = Modifier.size(size.dp))