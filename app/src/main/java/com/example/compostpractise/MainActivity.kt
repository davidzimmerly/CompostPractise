package com.example.compostpractise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compostpractise.destinations.VideoScreenDestination
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainTabScreen(navigator: DestinationsNavigator/*id: Int,onVideoOpen: () -> Unit*/) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        TabRow(
            // Our selected tab is our current page
            selectedTabIndex = pagerState.currentPage,
            // Override the indicator, using the provided pagerTabIndicatorOffset modifier
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            modifier = Modifier.width(300.dp)
        ) {
            // Add tabs for all of our pages
            listOf("home", "search", "settings").forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

//        val scrollState = ScrollState(initial = 0)
        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { page ->
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = when (page) {
                        0 -> "home"
                        1 -> "search"
                        2 -> "settings"
                        else -> ""
                    }, fontSize = 30.sp
                )
                if (page == 0) {
                    MockRow("Category 1", navigator = navigator)
                    MockRow("Category 2", navigator = navigator)
                    MockRow("Category 3", navigator = navigator)
                    MockRow("Category 4", navigator = navigator)
                }
                else if (page == 1) {
                    MockRow("search results", navigator)

                }
            }
        }
    }
}

@Destination
@Composable
fun VideoScreen(
    id: Int,
    groupName: String?, // <-- optional navigation argument
    isOwnUser: Boolean = false // <-- optional navigation argument
) {
    Text(text = "Playing a Video")
}

@Composable
fun MockRow(categoryName: String, navigator: DestinationsNavigator) {

    Column {
        Text("$categoryName:")
        LazyRow(userScrollEnabled = false) {
            items(2) {
                (0..6).forEach {
                    Card(elevation = 6.dp,
                        modifier = Modifier
                            .width(225.dp)
                            .height(150.dp)
                            .padding(10.dp)
                            .clickable {
                                navigator.navigate(VideoScreenDestination(id = 7, groupName = "row"))
                            }
                    ) {
                        GlideImage(
                            imageModel = { imageUrls[it] },
                            imageOptions = ImageOptions(),
                        )
                    }
                }
            }
        }
    }
}


val imageUrls = listOf(
    "https://i.picsum.photos/id/188/300/200.jpg?hmac=6ogItP5xnIupO6itVYaNUDYYlpojljA5tX4A862qF2U",

    "https://i.picsum.photos/id/201/300/200.jpg?hmac=WnKVl3Hz59wNpTuXl_9t_WPQWZqQj8ZNgsskBAhdwoI",

    "https://i.picsum.photos/id/113/300/200.jpg?hmac=AMx2fckFaKCQ2969_wSIUYuGAK249Kga3O3p66a8DQI",

    "https://i.picsum.photos/id/544/300/200.jpg?hmac=x4dvVlF_f4DjVQP54toIkasLnVFRHzDGmdcrQWwM3I4",

    "https://i.picsum.photos/id/504/300/200.jpg?hmac=ZWbvZLkpwmmjWwhlW4lUoYi9mPgTSVAg6dczQjiT11M",

    "https://i.picsum.photos/id/196/300/200.jpg?hmac=lNzCg_rdAUeQrWGjWvNaatB2KjYdkBLGSXmWw3SZy9Q",

    "https://i.picsum.photos/id/435/300/200.jpg?hmac=giBaPjTLuJINS0PaCpcR7bSsx89FxbmTtPZYKEaU7po"
)

