package com.example.piston


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.piston.ui.Quize.*

import com.example.piston.ui.theme.textColor
import com.google.gson.Gson
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun ExamResultPage(
    navController: NavHostController,
    correctAnswerCount: Int,
    percent: Float,
    quizResult: QuizResult
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    BackPressHandler {
        showDialog = true
    }
    if (showDialog)
        ExitDialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(dismissOnClickOutside = false),
            dataProperties = ExitDialogProperties(
                stringResource(id = R.string.Exit_btn),
                stringResource(id = R.string.NeverMind_txt),
                stringResource(id = R.string.BackToCourseListTitle),
                R.drawable.ic_exit_img
            ),
            onCancel = { showDialog = false }, onConfirm = {
                navController.popBackStack(
                    route = ExamQuizPages.ElementaryTestListName,
                    inclusive = false,
                    saveState = false
                )
            })
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                modifier = Modifier
                    .height(60.dp)
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    ),
                stringResource(id = R.string.TestResulteTitle)
            ) {
                showDialog = true
            }
            Body(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                correctAnswerCount, percent, navController, quizResult
            )
        }
    }
}

@Composable
fun ShowPercent(percent: Float, title: String, modifier: Modifier, color: Int) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.fillMaxSize(1f),
            color = Color(240, 240, 255),
            strokeWidth = 8.dp
        )
        CircularProgressIndicator(
            progress = (percent / 100f).coerceIn(0f, 1f),
            modifier = Modifier.fillMaxSize(1f),
            color = colorResource(id = color),
            strokeWidth = 7.dp
        )
        Column(
            modifier = Modifier
                .fillMaxSize(sqrt(2f) / 2)
                .align(Alignment.Center)
        ) {
            AutoSizeText(
                text = "${(percent.coerceIn(0f, 100f)).roundToInt()}%", modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f), color = colorResource(
                    id = color
                )
            ) {
                it.setLines(1)
            }
            AutoSizeText(
                text = title, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), color = colorResource(
                    id = color
                )
            ) {
                it.setLines(1)
            }

        }
    }
}


@Composable
fun TextIcon(
    modifier: Modifier,
    backColor: Color,
    text: String,
    textColor: Color,
    clickable: () -> Unit,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    Card(modifier = modifier, backgroundColor = backColor, shape = shape) {
        Box(modifier = Modifier.fillMaxSize()) {
            AutoSizeText(
                text = text, modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clickable { clickable() },
                color = textColor
            )
        }
    }
}

@Composable
fun ImageIcon(
    modifier: Modifier,
    backColor: Color,
    image: Int,
    clickable: () -> Unit,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    Card(modifier = modifier, backgroundColor = backColor, shape = shape) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clickable {
                        clickable()
                    }
            )
        }
    }
}


//@Composable
//fun TopBar(modifier: Modifier, text: String, onBackPress: () -> Unit) {
//    Row(modifier = modifier.fillMaxWidth()) {
//        ImageIcon(
//            modifier = Modifier
//                .size(40.dp)
//                .align(Alignment.CenterVertically),
//            backColor = Color.Blue,
//            image = R.drawable.ic_back,
//            clickable = onBackPress
//        )
//        Spacer(
//            modifier = Modifier
//                .fillMaxHeight()
//                .weight(1f)
//        )
//        AutoSizeText(
//            text = text,
//            modifier = Modifier
//                .fillMaxHeight(0.8f)
//                .weight(2f),
//            color = textColor
//        )
//
//    }
//}

data class Result(val color: Int, val title1: Int, val title2: Int, val bodyText: Int)

@Composable
fun Body(
    modifier: Modifier,
    correctAnswerCount: Int,
    percent: Float,
    navController: NavHostController,
    quizResult: QuizResult
) {
    var result = when (percent) {
        in 0f..50f -> {
            Result(
                R.color.trikyRed,
                R.string.test_result_title_weak,
                R.string.test_result_title2_weak,
                R.string.test_result_body_weak
            )
        }
        in 51f..79f -> {
            Result(
                R.color.golden,
                R.string.test_result_title_medium,
                R.string.test_result_title2_medium,
                R.string.test_result_body_medium
            )
        }
        in 80f..100f -> {
            Result(
                R.color.light_green,
                R.string.test_result_title_good,
                R.string.test_result_title2_good,
                R.string.test_result_body_good
            )
        }
        else -> null
    }
    Column(modifier = modifier) {
        ShowPercent(
            percent = percent, title = "$correctAnswerCount" + " درست از 30",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .align(CenterHorizontally),
            result!!.color
        )
        ShowResultText(
            Modifier
                .fillMaxWidth()
                .weight(3f)
                .padding(start = 16.dp, end = 16.dp),
            result
        )
        CustomButton(
            title = stringResource(id = R.string.TryAgainTest_btn),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 4.dp),
            shape = RoundedCornerShape(8.dp),
            backColor = colorResource(
                id = R.color.textColors
            ),
            textColor = Color.White,
            onClick = {
                navController.popBackStack()
            }
        )
        CustomButton(
            title = stringResource(id = R.string.ShowTrueAnswer_txt), modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 4.dp, bottom = 16.dp)
                .weight(1f), shape = RoundedCornerShape(8.dp), backColor = colorResource(
                id = R.color.textColor_deep_blue
            ), textColor = Color.White
        ) {
            var quizResultJson = Gson().toJson(quizResult)
            navController.navigate(route = "${ExamQuizPages.ShowTrueAnswersName}/$quizResultJson")
        }
    }
}

@Composable
fun CustomButton(
    title: String,
    modifier: Modifier,
    shape: Shape,
    backColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(1f)
                .clickable {
                    onClick()
                },
            contentAlignment = Center
        ) {
            Text(
                text = title,
                modifier = Modifier,
                color = textColor,
                fontFamily = FontFamily(Font(R.font.shabnam)),
                fontSize = dimensionResource(id = R.dimen.bodyText).value.sp
            )
//            AutoSizeText(
//                text = title,
//                modifier = Modifier
//                    .fillMaxWidth(1f)
//                    .fillMaxHeight()
//                    .clickable {
//                        onClick()
//                    },
//                color = textColor
//            )
        }

    }
}

@Composable
fun ShowResultText(modifier: Modifier, result: Result) {
    Column(modifier = modifier) {
        R.string.test_result_title2_weak
        AutoSizeText(
            text = stringResource(id = result.title1), modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), color = colorResource(
                id = result.color
            )
        ) {
            it.setLines(1)
        }
        AutoSizeText(
            text = stringResource(id = result.title2), modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), color = colorResource(
                id = R.color.textColor_deep_blue
            )
        ) {
            it.setLines(1)
        }
        AutoSizeText(
            text = stringResource(
                id = result.bodyText
            ), modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), color = colorResource(
                id = R.color.textColors
            )
        )
    }
}



