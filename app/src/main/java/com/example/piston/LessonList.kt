package com.example.piston

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.domain.LectureList
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun LessonsList(
    navController: NavController,
    item_name: String?,
    list: List<LectureList>
) {

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "$item_name",
                        textAlign = TextAlign.End,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.textColor_deep_blue)

                    )
                }
            }
        }) {

        LazyColumn {
            items(list) { item ->
                if (item.id == list.size) {
                    SubMenu(
                        item,
                        navController,
                        10.dp,
                        list.size -1,
                    )
                } else {
                    SubMenu(
                        item,
                        navController,
                        0.dp,
                        list.size -1,
                    )
                }
            }
        }
    }
}


@Composable
fun SubMenu(
    item: LectureList,
    navController: NavController,
    padding: Dp,
    type: Int,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("reading_page/${item.id - 1}/${type}")
            }
            .padding(4.dp, 4.dp, 4.dp, padding)
            .height(100.dp),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.2F)
                    .fillMaxHeight()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(0.6F)
                        .height(80.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_silver_medal),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = "کوییز | دروس", modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 7.dp)
                        .width(20.dp)
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.7F)
                    .align(Alignment.CenterVertically)
                    .height(30.dp),
                text = item.title,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = colorResource(id = R.color.textColor_deep_blue),
            )

            GlideImage(modifier = Modifier.fillMaxWidth(0.9F)
                .padding(top = 15.dp)
                .height(70.dp),
                imageModel = item.image,
                contentScale = ContentScale.FillBounds
            )


            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1.0F)

                    .background(
                        colorResource(id = R.color.recyclerEdgeBlueColor)
                    ),

                )


        }

    }
}