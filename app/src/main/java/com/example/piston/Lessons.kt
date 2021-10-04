package com.example.piston

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.entities.lessons_item


val lessons_list = listOf(
    lessons_item(
        0,
        "اموزش تئوری",
        "لیست دوره های تئوری",
        R.string.course_theory_body_text,
        R.drawable.theory_course_img
    ),
    lessons_item(
        1,
        "آموزش عملی",
        "لیست دوره های عملی",
        R.string.course_practical_body_text,
        R.drawable.practical_course_img
    ),
    lessons_item(
        2,
        "تابلو ها و خطوط راهنما",
        "لیست دروس",
        R.string.course_board_body_text,
        R.drawable.board_course_img
    )
)


@Composable
fun Lessons(navController: NavController) {

    Scaffold(Modifier.fillMaxHeight(),
        topBar = {
            TopAppBar(backgroundColor = Color.White,
            elevation = 0.dp) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.CourseCategoryTitle_txt),
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.textColor_deep_blue),
                    fontSize = 20.sp

                )
            }
        }
    ) {

        LazyColumn {
            items(lessons_list) { item ->
                if (item.id == 2) {
                    ItemsColumn(item = item, 80.dp, navController)
                } else {
                    ItemsColumn(item = item, 0.dp, navController)
                }
            }
        }


    }
}
@Composable
fun ItemsColumn(item: lessons_item, padding: Dp, navController: NavController) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp, 15.dp, 7.dp, padding)
            .height(180.dp)
            .clickable {
                navController.navigate("Lessons_menu/${item.cat}")
            },
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),

        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            Image(
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = item.vector)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, top = 10.dp)
                        .height(40.dp), text = item.title,
                    textAlign = TextAlign.End,
                    color = colorResource(id = R.color.textColor_deep_blue),
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .height(140.dp), text = stringResource(id = item.body),
                    textAlign = TextAlign.End,
                    color = colorResource(id = R.color.textColor_deep_blue),
                    fontSize = 12.sp
                )
            }
        }
    }
}
@Composable
fun ItemsColumn2(item: lessons_item, padding: Dp, navController: NavController) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp, 7.dp, 7.dp, padding)
            .height(180.dp)
            .clickable {
                navController.navigate("Lessons_menu/${item.cat}")
            },
        elevation = 10.dp,
        shape = RoundedCornerShape(30.dp),

        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            Image(
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = item.vector)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp), text = item.title,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.textColor_deep_blue),
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .height(140.dp), text = stringResource(id = item.body),
                    textAlign = TextAlign.End,
                    color = colorResource(id = R.color.textColor_deep_blue),
                    fontSize = 10.sp
                )
            }
        }
    }
}

