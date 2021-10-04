package com.example.piston.ui.Quize

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.domain.model.QuizModel
import com.example.piston.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.random.Random
import com.example.piston.R
import com.example.piston.ui.theme.textColor

@ExperimentalPagerApi
@Composable
fun TestLayout(
    modifier: Modifier,
    quizList: List<QuizModel>,
    showCorrectAnswer: Boolean,
    selectedAnswerList: List<Int>,
    selectedAnswerOnChange: (ArrayList<Int>) -> Unit,
    selectable: Boolean = true
) {
    val state = rememberPagerState(pageCount = quizList.size)
    Column(modifier = modifier) {

        var choose by remember {
            mutableStateOf(0)
        }

        var scope = rememberCoroutineScope()
        PagerLayout(
            modifier = Modifier
                .fillMaxWidth()
                .weight(12f),
            state = state,
            quizList,
            showCorrectAnswer,
            selectedAnswerList,
            selectedAnswerOnChange,
            selectable,
        ) {
            choose = it
        }
        QuestionList(modifier = Modifier.height(60.dp), onChoose = {
            scope.launch {
                state.animateScrollToPage(it)
            }
            choose = it
        }, choose, 30, quizList, selectedAnswerList, showCorrectAnswer)
    }

}

fun initSelectedList(size: Int): ArrayList<Int> {
    var list = ArrayList<Int>()
    (0 until size).forEach {
        list.add(-1)
    }
    return list
}

@ExperimentalPagerApi
@Composable
fun PagerLayout(
    modifier: Modifier,
    state: PagerState,
    list: List<QuizModel>,
    showCorrectAnswer: Boolean,
    selectedAnswerList: List<Int>,
    selectedAnswerOnChange: (ArrayList<Int>) -> Unit,
    selectable: Boolean,
    onPageScroll: (Int) -> Unit,
) {

    HorizontalPager(
        state,
        modifier = modifier,
    ) { index ->
        onPageScroll(this.currentPage)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
//                    val pageOffset = calculateCurrentOffsetForPage(index).absoluteValue
//                    alpha = lerp(
//                        start = 0f,
//                        stop = 1f,
//                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                    )
//                    lerp(
//                        start = 0.85f,
//                        stop = 1f,
//                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                    ).also {
//                        this.scaleX = it
//                        this.scaleY = it
//                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
            ) {
                QuestionLayout(
                    index,
                    list[index],
                    selectedAnswerList[index],
                    showCorrectAnswer,
                    selectable
                ) { answerIndex, pageIndex ->
                    val tempList = selectedAnswerList.copy()
                    tempList[pageIndex] = answerIndex
                    selectedAnswerOnChange(tempList)
                }
            }

        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun QuestionLayout(
    index: Int,
    page: QuizModel,
    selectedAnswer: Int,
    showCorrectAnswer: Boolean,
    selectable: Boolean,
    onSelectAnswer: (index: Int, pageIndex: Int) -> Unit
) {
    var imageList = listOf(
        R.drawable.image1,
        R.drawable.image10,
        R.drawable.image12,
        R.drawable.image14,
        R.drawable.image15,
        R.drawable.image17,
        R.drawable.image18,
        R.drawable.image19,
        R.drawable.image20,
        R.drawable.image22,
    )

    fun randomImages(): List<Int> {
        var list = List(30) {
            imageList[Random(Calendar.getInstance().timeInMillis).nextInt(from = 0, until = 9)]
        }
        return list
    }

    var context = LocalContext.current
    var answers = listOf(page.answer1, page.answer2, page.answer3, page.answer4)
    var images by remember {
        mutableStateOf(randomImages())
    }
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 8.dp,
                bottomEnd = 8.dp
            )
        ) {
            ComposeImageView(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), updateImage = { image ->
                page.image?.let {
                    image.setImageBitmap(it)
                } ?: let {
                    val drawableId = images[index]
                    val drawable =
                        ResourcesCompat.getDrawable(context.resources, drawableId, null)
                    image.setImageDrawable(drawable)
                }
            })

        }

        fun List<String>.findMaxSize(): Int {
            var size = 0
            forEach {
                if (it.length > size)
                    size = it.length
            }
            return size
        }

        fun List<String>.findMinSize(): Int {
            var size = first().length
            forEach {
                if (it.length < size)
                    size = it.length
            }
            return size
        }

        fun Float.coerceToWeight(weight: Float = 0.7f, max: Int): Float {
            return this.coerceIn(max * weight, max.toFloat()) / max.toFloat()
        }

        var maxLength = answers.findMaxSize()
        var minLength = answers.findMinSize()
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = page.title,
                        textAlign = TextAlign.End,
                        color = colorResource(id = R.color.textColors),
                        fontSize = dimensionResource(id = R.dimen.font_header_2).value.sp,
                        fontFamily = FontFamily(Font(R.font.shabnam)),
                    )
                }
//                    AutoSizeText(
//                        text = page.title,
//                        modifier = Modifier.fillMaxSize(),
//                        color = Color.DarkGray,
//                        gravity = Gravity.START
//                    )
            }

            (0..3).forEach { answerIndex ->
                var color = if (showCorrectAnswer) {
                    when {
                        answerIndex == page.true_answer -> Color.Green
                        selectedAnswer == answerIndex -> Color.Red
                        else -> Color.Transparent
                    }
                } else {
                    if (answerIndex == selectedAnswer) {
                        Color.DarkGray
                    } else Color.Transparent
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 50.dp)
                        .padding(4.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(width = 2.dp, color = color)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(selectable) {
                                onSelectAnswer(answerIndex, index)
                            }, contentAlignment = Alignment.Center
                    ) {
                        var density = LocalDensity.current
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {

                            Box(
                                modifier = Modifier.weight(1f),contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = answers[answerIndex],
                                    fontFamily = FontFamily(Font(R.font.shabnam)),
                                    fontSize = dimensionResource(
                                        id = R.dimen.bodyText
                                    ).value.sp,
                                    color = colorResource(id = R.color.textColor_deep_blue),
                                    textAlign = TextAlign.Right
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .width(4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .width(30.dp)
                                    .align(alignment = Alignment.CenterVertically),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = (answerIndex + 1).toString(),
                                    fontSize = dimensionResource(id = R.dimen.bodyText).value.sp,
                                    color = Color.Gray,
                                    fontFamily = FontFamily(Font(R.font.shabnam))
                                )
                            }
                        }
                    }

                }
            }

        }
    }
}

@Composable
fun ComposeImageView(modifier: Modifier, updateImage: (ImageView) -> Unit) {
    AndroidView(factory = {
        ImageView(it)
    }, update = {
        it.layoutParams = ViewGroup.LayoutParams(-1, -1)
        updateImage(it)
    },
        modifier = modifier
    )
}

@Composable
fun QuestionList(
    modifier: Modifier,
    onChoose: (index: Int) -> Unit,
    _choose: Int,
    count: Int,
    quizList: List<QuizModel>,
    selectedAnswerList: List<Int>,
    correctAnswer: Boolean
) {
    var coroutineScope = rememberCoroutineScope()
    var state = rememberLazyListState()
    LazyRow(
        state = state, modifier = modifier
            .fillMaxWidth()
    ) {
        coroutineScope.launch {
            var firstVisibleItemIndex = state.firstVisibleItemIndex
            var visibleItemCount = state.layoutInfo.visibleItemsInfo.size
            if (_choose !in firstVisibleItemIndex + 1 until firstVisibleItemIndex + visibleItemCount - 1)
                state.animateScrollToItem(_choose)
        }
        items(count) { index ->
            val color = if (correctAnswer) {
                if (quizList[index].true_answer == selectedAnswerList[index]) {
                    if (index == _choose) {
                        colorResource(id = R.color.selectedGreen)
                    } else
                        colorResource(id = R.color.un_selectedGreen)
                } else if (selectedAnswerList[index] == -1) {
                    if (index == _choose) {
                        colorResource(id = R.color.selectedYellow)
                    } else
                        colorResource(id = R.color.un_selectedYellow)
                } else {
                    if (index == _choose) {
                        colorResource(id = R.color.selectedRed)
                    } else
                        colorResource(id = R.color.un_selectedRed)
                }
            } else {
                if (_choose == index) {
                    colorResource(id = R.color.selectedYellow)
                } else {
                    if (selectedAnswerList[index] != -1) {
                        colorResource(id = R.color.textColor_deep_blue)
                    } else {
                        colorResource(id = R.color.light_blue)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(3 / 4f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(4.dp),
                    backgroundColor = color
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onChoose(index)
                        }) {
                        AutoSizeText(
                            text = (index + 1).toString(), modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize(), color = Color.White
                        )
                    }
                }
                if (_choose == index)
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(1.dp)
                    ) {
                        drawCircle(textColor)
                    }
            }

        }
    }
}