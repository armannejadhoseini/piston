package com.example.piston


import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.data.entities.Constants
import com.example.myapplication.domain.BoardList
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun BoardTable(list: List<BoardList>, navController: NavController) {
    var boardList by remember {
        mutableStateOf(list)
    }
    var image by remember {
        mutableStateOf<Bitmap?>(null)
    }
    var text by remember {
        mutableStateOf("")
    }
    var testMode by remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .height(40.dp)
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
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    var topBarAction by remember {
                        mutableStateOf(1)
                    }
                    var textInput by remember {
                        mutableStateOf("")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9F)
                            .height(30.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        if (topBarAction == 2) {
                            BasicTextField(
                                modifier = Modifier
                                    .fillMaxWidth(0.9F)
                                    .height(30.dp)
                                    .padding(end = 2.dp, top = 2.dp),
                                value = textInput,
                                onValueChange = {
                                    boardList = list
                                    textInput = it
                                    boardList = searchTask(textInput, boardList)
                                },
                                cursorBrush = SolidColor(colorResource(id = R.color.textColor_deep_blue)),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(2.dp))
                                            .border(
                                                1.dp, colorResource(id = R.color.light_gray),
                                                RoundedCornerShape(1.dp)
                                            ),
                                        contentAlignment = Alignment.CenterStart,
                                    ) {

                                        innerTextField()
                                    }
                                }

                            )
                        }
                        if (topBarAction == 1) {
                            var showDropDown by remember {
                                mutableStateOf(false)
                            }
                            var textValue by remember {
                                mutableStateOf(Constants.boardCategory[0])
                            }

                            Text(modifier = Modifier
                                .clickable {
                                    showDropDown = showDropDown != true
                                }
                                .border(
                                    1.dp,
                                    Color.LightGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .fillMaxWidth(0.8F)
                                .padding(end = 2.dp, top = 2.dp)
                                .height(30.dp),
                                text = textValue,
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.textColor_deep_blue),
                                fontFamily = FontFamily(Font(R.font.shabnam))
                            )


                            DropdownMenu(
                                modifier = Modifier
                                    .clickable {
                                        showDropDown = true
                                    },
                                expanded = showDropDown,
                                onDismissRequest = {
                                    showDropDown = false
                                },
                            ) {
                                Constants.boardCategory.forEachIndexed { index, item ->
                                    DropdownMenuItem(onClick = {
                                        boardList = list
                                        textValue = item
                                        showDropDown = false
                                        boardList = categortTask(index, boardList)

                                    }) {
                                        Row(modifier = Modifier.fillMaxWidth(0.8F)) {
                                            Text(text = item)
                                        }
                                    }
                                }
                            }


                        }
                    }
                    var icon by remember {
                        mutableStateOf(R.drawable.ic_search)
                    }
                    IconButton(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),
                        onClick = {
                            if (topBarAction == 1) {
                                topBarAction = 2
                                icon = R.drawable.ic_close
                            } else {
                                topBarAction = 1
                                icon = R.drawable.ic_search
                            }
                            boardList = list
                        },
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(30.dp)
                                .padding(start = 2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .height(30.dp),
                            imageVector = ImageVector.vectorResource(id = icon),
                            contentDescription = "",
                            tint = colorResource(id = R.color.gray)
                        )

                    }

                }
            }
        }
    }) {
        Box() {
            LazyVerticalGrid(cells = GridCells.Adaptive(128.dp)) {
                items(boardList.size) { item ->

                    Column {
                        Card(
                            modifier = Modifier
                                .width(140.dp)
                                .height(140.dp)
                                .padding(10.dp)
                                .clickable {
                                    image = boardList[item].image
                                    text = boardList[item].title
                                },
                            elevation = 4.dp
                        ) {
                            GlideImage(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(120.dp)
                                    .padding(10.dp),
                                imageModel = boardList[item].image
                            )
                        }
                        if (testMode == false) {
                            Text(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(20.dp)
                                    .padding(horizontal = 10.dp),
                                text = boardList[item].title,
                                color = colorResource(id = R.color.textColor_deep_blue),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.shabnam))
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                var btnTxt by remember {
                    mutableStateOf(R.string.Testing_txt)
                }

                Column(
                    modifier = Modifier
                        .background(colorResource(id = R.color.light_blue).copy(alpha = 0.5F))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(modifier = Modifier
                        .padding(10.dp),
                        onClick = {
                            if (btnTxt.equals(R.string.Testing_txt)) {
                                btnTxt = R.string.Educational_txt
                                testMode = true
                            } else {
                                btnTxt = R.string.Testing_txt
                                testMode = false
                            }
                        },
                    colors = ButtonDefaults.buttonColors(Color.Blue)) {
                        Text(text = stringResource(id = btnTxt),color = colorResource(id = R.color.white),
                        fontFamily = FontFamily(Font(R.font.shabnam)))
                    }
                    if (testMode == true) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            image?.let { it1 ->
                                GlideImage(
                                    modifier = Modifier.size(100.dp),
                                    imageModel = it1,
                                    contentScale = ContentScale.Inside
                                )
                            }
                            Text(modifier = Modifier.padding(bottom = 10.dp), text = text,
                                color = colorResource(id = R.color.white),
                                fontFamily = FontFamily(Font(R.font.shabnam))
                            )
                        }
                    }
                }
            }
        }
    }


}


fun searchTask(text: String, list: List<BoardList>): List<BoardList> {
    val matchedItems = mutableListOf<BoardList>()
    list.forEach { item ->
        if (item.title.contains(text)) {
            matchedItems.add(item)
        }
    }
    return matchedItems
}

fun categortTask(type: Int, list: List<BoardList>): List<BoardList> {
    val matchedItems = mutableListOf<BoardList>()
    if (type == 0) {
        matchedItems.addAll(list)
    } else {
        list.forEach { item ->
            if (item.type == type) {
                matchedItems.add(item)
            }
        }
    }
    return matchedItems
}