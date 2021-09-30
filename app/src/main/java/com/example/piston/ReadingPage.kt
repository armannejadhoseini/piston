package com.example.piston

import android.graphics.Bitmap
import android.util.Log
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.entities.Constants
import com.example.myapplication.domain.LectureList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage


@ExperimentalPagerApi
@Composable
fun ReadingPage(navController: NavController, index: Int, list: List<LectureList>) {

    HorizontalPager(state = rememberPagerState(pageCount = 8)) {
        val page = this.currentPage
        Log.d("TAG", "ReadingPage: $page")
        Column {
            PageHeader(navController, page)
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
                LectureTab(index, list, page)
            }
        }

    }
}


@Composable
fun PageHeader(navController: NavController, page: Int) {
    Row(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            onClick = {
                navController.navigateUp()
            },

            ) {
            Icon(
                modifier = Modifier
                    .width(20.dp)
                    .padding(start = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = colorResource(id = R.color.courcesBlue))
                    .height(20.dp),
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
                    color = colorResource(id = R.color.layout_background)
                    size = 20.dp
                } else {
                    size = 15.dp
                }

                Button(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(start = 7.dp)
                        .width(size)
                        .height(size),
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(color),
                )
                {
                    Text(
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp),
                        text = "$index",
                        fontSize = 1.sp,
                        color = colorResource(id = R.color.trikyRed)
                    )
                }
            }
        }
        if (page == 7) {
            IconButton(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),
                onClick = {
                    navController.navigateUp()
                },

                ) {
                Icon(
                    modifier = Modifier
                        .width(20.dp)
                        .padding(start = 2.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = colorResource(id = R.color.courcesBlue))
                        .height(20.dp),
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

@Composable
fun LectureTab(index: Int, list: List<LectureList>, page: Int) {
    Log.d("TAG", "LectureTab: $page")
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            imageModel = list[index].image,
            contentScale = ContentScale.Inside
        )

        Row(modifier = Modifier.padding(vertical = 20.dp)) {
            IconButton(
                modifier = Modifier
                    .height(30.dp)
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    .width(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(id = R.color.recyclerEdgeBlueColor)),
                onClick = { /*TODO*/ },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(40.dp),
                        text = "افزودن",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_unsave),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )

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
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState(0))
                .padding(end = 10.dp)
                .fillMaxHeight(1.0F),
            text = when (page) {
                0 -> list[index].page1
                1 -> list[index].page2
                2 -> list[index].page3
                4 -> list[index].page4
                5 -> list[index].page5
                7 -> list[index].page6

                else -> list[index].page1
            },
            textAlign = TextAlign.End,
            fontSize = 15.sp,
            color = colorResource(id = R.color.textColors)
        )
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
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            imageModel = image,
            contentScale = ContentScale.Inside
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp),
            text = title,
            color = colorResource(id = R.color.textColors),
            fontSize = 15.sp,
            textAlign = TextAlign.End
        )

        var selectable by remember {
            mutableStateOf(true)
        }
        var trueColor by remember {
            mutableStateOf(R.color.white)
        }
        listOf(1, 2, 3, 4).forEachIndexed { index, item ->
            var color by remember {
                mutableStateOf(R.color.white)
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
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(0.90F),
                        text = when (item) {
                            1 -> answer1
                            2 -> answer2
                            3 -> answer3
                            4 -> answer4

                            else -> answer1
                        },
                        color = colorResource(
                            id = R.color.textColors
                        ),
                        textAlign = TextAlign.End
                    )
                    RadioButton(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .layoutId(item - 1),
                        selected = selected,
                        onClick = {
                            if (index == true_answer) {
                                selected = true
                                color = R.color.isDoneGreen
                                selectable = false
                                trueColor = R.color.isDoneGreen
                            } else {
                                selected = true
                                color = R.color.trikyRed
                                selectable = false
                                trueColor = R.color.isDoneGreen

                            }
                        },
                        enabled = selectable
                    )

                }
            }
        }
    }
}










