package com.example.piston

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavHostController
import com.example.myapplication.domain.model.QuizModel
import com.example.piston.ui.Quize.*
import com.example.piston.ui.Quize.ExamQuizPages.ElementaryResultName
import com.example.piston.ui.Quize.ExamQuizPages.ElementaryTestListName
import com.example.piston.ui.Quize.BackPressHandler


import com.google.accompanist.pager.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.random.Random


@ExperimentalPagerApi
@Composable
fun ExamTestPage(navController: NavHostController, quizList: List<QuizModel>) {
    var countDownTimer by remember {
        mutableStateOf<CountDownTimer?>(null)
    }
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
                stringResource(id = R.string.ExitExamTitle),
                R.drawable.ic_exit_img
            ),
            onCancel = { showDialog = false }, onConfirm = {
                navController.popBackStack(
                    route = ElementaryTestListName,
                    inclusive = false,
                    saveState = false
                )
            })


    var (selectedAnswerList, selectedAnswerOnChange) = remember {
        mutableStateOf(initSelectedList(30))
    }
    var correctAnswer by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.layout_background)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopLayout(
            Modifier
                .fillMaxWidth()
                .height(60.dp),
            onBackPress = {
                showDialog = true
            },
            onFinish = {
                countDownTimer?.cancel()
                val quizResult = QuizResult(selectedAnswerList.toList(), quizList.toList())
                val quizResultJson = Gson().toJson(quizResult, QuizResult::class.java)
                navController.navigate("${ElementaryResultName}/$quizResultJson")
            },
            onCancel = {
                countDownTimer = it
            }
        )
        TestLayout(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            quizList,
            correctAnswer,
            selectedAnswerList,
            selectedAnswerOnChange
        )
    }
}



@Composable
fun TopLayout(
    modifier: Modifier,
    onBackPress: () -> Unit,
    onFinish: () -> Unit,
    onCancel: (CountDownTimer) -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(Color.White)){
        Row(
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .size(30.dp)
                    .align(CenterVertically),
                shape = RoundedCornerShape(4.dp),
                backgroundColor = colorResource(id = R.color.light_blue)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clickable { onBackPress() }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(4f)
                    .align(CenterVertically)
                    .padding(horizontal = 8.dp, vertical = 2.dp), contentAlignment = Center
            ) {
                TimerLayout(
                    20 * 60 * 1000,
                    onCancel = onCancel,
                    onFinish = onFinish
                )
            }
            Card(
                modifier = Modifier
                    .width(80.dp)
                    .height(40.dp)
                    .align(CenterVertically), backgroundColor = colorResource(id = R.color.light_blue),
                shape = RoundedCornerShape(4.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable { onFinish() }) {
                    AutoSizeText(
                        text = "پایان", modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp), color = Color.White
                    )
                }
            }

        }
    }

}

fun <T> List<T>.copy(): ArrayList<T> {
    var list = ArrayList<T>()
    this.forEach {
        list.add(it)
    }
    return list
}





@Composable
fun TimerLayout(
    timeInMils: Long = 190000,
    onFinish: () -> Unit,
    onCancel: (CountDownTimer) -> Unit
) {

    var minute by remember {
        mutableStateOf(0)
    }
    var second by remember {
        mutableStateOf(0)
    }
    var timer = object : CountDownTimer(timeInMils, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var seconds = millisUntilFinished / 1000
            minute = (seconds / 60).toInt()
            second = (seconds % 60).toInt()
        }

        override fun onFinish() {
            onFinish()
        }
    }
    var coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = "one") {
        coroutineScope.launch {
            timer.start()
            onCancel(timer)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
            .padding(start = 4.dp, bottom = 4.dp, top = 4.dp, end = 4.dp),
    ) {
        AutoSizeText(
            text = minute.toString(), modifier = Modifier
                .weight(2f)
                .fillMaxHeight(), color = colorResource(id = R.color.textColor_deep_blue)
        )
        AutoSizeText(
            text = ":", modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), color = colorResource(id = R.color.textColor_deep_blue)
        )
        AutoSizeText(
            text = second.toString(), modifier = Modifier
                .weight(2f)
                .fillMaxHeight(), color = colorResource(id = R.color.textColor_deep_blue)
        )
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop

@SuppressLint("RestrictedApi")
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier,
    color: Color,
    gravity: Int = Gravity.CENTER,
    font: Int = R.font.shabnam,
    androidText: ((AppCompatTextView) -> Unit)? = null
) {
    AndroidView(factory = {
        AppCompatTextView(it)
    }, modifier = modifier, update = {
        it.setAutoSizeTextTypeUniformWithConfiguration(1, 100, 1, 1)
        it.text = text
        it.layoutParams = ViewGroup.LayoutParams(-1, -1)
        it.gravity = gravity
        it.setTextColor(color.toArgb())
        it.typeface = ResourcesCompat.getFont(it.context, font)
        androidText?.let { andriodText ->
            androidText(it)
        }
    })
}
