package com.example.piston

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.data.entities.Constants
import com.example.data.entities.savedCourse
import com.example.myapplication.domain.LectureList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@ExperimentalPagerApi
@Composable
fun ReadingPage(navController: NavController, index: Int, list: List<LectureList>, savedPages: List<savedCourse>) {

    var page by remember {
        mutableStateOf(0)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 0.dp
            ) {
                PageHeader(navController = navController, page = page)
            }
        }
    ) {

        HorizontalPager(state = rememberPagerState(pageCount = 8)) {
            page = this.currentPage
            Column {
                if (page == 3) {
                    QuestionTab(
                        list[index].quiz_title1,
                        list[index].image,
                        list[index].quiz1_answer1,
                        list[index].quiz1_answer2,
                        list[index].quiz1_answer3,
                        list[index].quiz1_answer4,
                        list[index].quiz1_true_answer
                    )
                }
                if (page == 6) {
                    QuestionTab(
                        list[index].quiz_title2,
                        list[index].image,
                        list[index].quiz2_answer1,
                        list[index].quiz2_answer2,
                        list[index].quiz2_answer3,
                        list[index].quiz2_answer4,
                        list[index].quiz2_true_answer
                    )
                } else {
                    LectureTab(index, list, page, savedPages)
                }
            }

        }
    }
}


@Composable
fun PageHeader(navController: NavController, page: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            onClick = {
                navController.navigateUp()
            },

            ) {
            Icon(
                modifier = Modifier
                    .width(25.dp)
                    .padding(start = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = colorResource(id = R.color.courcesBlue))
                    .height(25.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                contentDescription = "",
                tint = Color.Unspecified
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .padding(8.dp)
                .height(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            var color: Color = colorResource(id = R.color.isDoneGreen)
            var size: Dp
            Constants.reading_list.forEach { index ->
                if (page + 1 == index) {
                    color = Color(145, 178, 255)
                    size = 20.dp
                } else {
                    size = 15.dp
                }
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(start = 7.dp)
                        .width(size)
                        .height(size)
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            modifier = Modifier
                                .background(color)
                                .width(size)
                                .height(size),
                            text = when (index) {
                                4 -> "?"
                                7 -> "?"
                                else -> index.toString()
                            },
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = colorResource(id = R.color.white),
                            fontFamily = FontFamily(Font(R.font.shabnam))
                        )
                    }
                }
            }

        }
        if (page == 7) {
            IconButton(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                onClick = {
                    navController.navigateUp()
                },

                ) {
                Icon(
                    modifier = Modifier
                        .width(25.dp)
                        .padding(start = 2.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = colorResource(id = R.color.courcesBlue))
                        .height(25.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_check),
                    contentDescription = "",
                    tint = Color.Unspecified
                )

            }

        } else {
            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
            )
        }
    }
}

@DelicateCoroutinesApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LectureTab(index: Int, list: List<LectureList>, page: Int, savedPages: List<savedCourse>) {
    val viewModel: ViewModel = viewModel()
    var isSaved by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Card(elevation = 4.dp) {

            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                imageModel = list[index].image,
                contentScale = ContentScale.Inside
            )
        }

        Row(modifier = Modifier.padding(vertical = 20.dp)) {
            savedPages.let {
                it.forEach {
                    if (it.id + 1 == list[index].id && it.type == list[index].type && it.page == page) {
                        isSaved = true
                        Log.d("TAG", "LectureTabPage: $page")
                    }
                }
            }
            if (isSaved == true) {
                IconButton(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(10.dp, 0.dp, 0.dp, 0.dp)
                        .width(65.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(colorResource(id = R.color.recyclerEdgeBlueColor)),
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            viewModel.deleteSaved(list[index].id,list[index].type,page)
                        }
                        isSaved = false
                    },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = "??????",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.shabnam))
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_save),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )

                    }

                }
            }
            if(isSaved == false) {
                IconButton(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(10.dp, 0.dp, 0.dp, 0.dp)
                        .width(65.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(colorResource(id = R.color.recyclerEdgeBlueColor)),
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            viewModel.save(list[index].id,list[index].type,page)
                        }
                        isSaved = true
                    },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = "????????????",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.shabnam))
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_unsave),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )

                    }

                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth(1.0F)
                    .padding(end = 10.dp)
                    .height(30.dp),
                text = list[index].title,
                textAlign = TextAlign.End,
                fontSize = 19.sp,
                color = colorResource(id = R.color.textColors),
                fontFamily = FontFamily(Font(R.font.shabnam))
            )
        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState(0))
                    .padding(start = 10.dp)
                    .fillMaxHeight(1.0F),
                fontFamily = FontFamily(Font(R.font.shabnam)),
                text = when (page) {
                    0 -> list[index].page1
                    1 -> list[index].page2
                    2 -> list[index].page3
                    4 -> list[index].page4
                    5 -> list[index].page5
                    7 -> list[index].page6

                    else -> list[index].page1
                },
                textAlign = TextAlign.Start,
                fontSize = 15.sp,
                color = colorResource(id = R.color.textColors)
            )
        }
    }
}


@Composable
fun QuestionTab(
    title: String,
    image: Bitmap,
    answer1: String,
    answer2: String,
    answer3: String,
    answer4: String,
    true_answer: Int
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End) {
        Card(elevation = 4.dp) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                imageModel = image,
                contentScale = ContentScale.Inside
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp),
            text = title,
            color = colorResource(id = R.color.textColors),
            fontSize = 20.sp,
            textAlign = TextAlign.End,
            fontFamily = FontFamily(Font(R.font.shabnam))

        )

        var selectable by remember {
            mutableStateOf(true)
        }
        var trueColor by remember {
            mutableStateOf(R.color.white_deep)
        }
        var textColor by remember {
            mutableStateOf(R.color.textColor_deep_blue)
        }
        var textColorTrue by remember {
            mutableStateOf(R.color.textColor_deep_blue)
        }
        listOf(1, 2, 3, 4).forEachIndexed { index, item ->
            var color by remember {
                mutableStateOf(R.color.white_deep)
            }
            var selected by remember {
                mutableStateOf(false)
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 4.dp, end = 20.dp, bottom = 4.dp)
                    .border(
                        4.dp, when (item - 1) {
                            true_answer -> colorResource(id = trueColor)
                            else -> colorResource(id = color)
                        }, shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                elevation = 4.dp
            ) {
                Row(

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .layoutId(item - 1),
                        selected = selected,
                        onClick = {
                            if (index == true_answer) {
                                selected = true
                                color = R.color.isDoneGreen
                                selectable = false
                                trueColor = R.color.isDoneGreen
                                textColorTrue = R.color.isDoneGreen
                            } else {
                                selected = true
                                color = R.color.trikyRed
                                selectable = false
                                trueColor = R.color.isDoneGreen
                                textColorTrue = R.color.isDoneGreen
                            }
                        },
                        enabled = selectable,
                        colors = RadioButtonDefaults.colors(colorResource(id = color))
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(0.95F),
                        text = when (item) {
                            1 -> answer1
                            2 -> answer2
                            3 -> answer3
                            4 -> answer4

                            else -> answer1
                        },
                        color = when (item - 1) {
                            true_answer -> colorResource(id = textColorTrue)
                            else -> colorResource(id = textColor)
                        },
                        textAlign = TextAlign.End,
                        fontFamily = FontFamily(Font(R.font.shabnam))

                    )

                }
            }
        }
    }
}










