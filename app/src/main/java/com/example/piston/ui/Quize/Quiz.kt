package com.example.piston.ui.Quize


import android.view.Gravity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.domain.model.QuizModel
import com.example.myapplication.domain.model.TestPercentEntity
import com.example.piston.*
import com.example.piston.R
import com.example.piston.ui.Quize.ExamQuizPages.AdvanceTestListName
import com.example.piston.ui.Quize.ExamQuizPages.AdvanceTestsName
import com.example.piston.ui.Quize.ExamQuizPages.ElementaryResultName
import com.example.piston.ui.Quize.ExamQuizPages.ElementaryTestListName
import com.example.piston.ui.Quize.ExamQuizPages.ElementaryTestsName
import com.example.piston.ui.Quize.ExamQuizPages.FirstTestPageName
import com.example.piston.ui.Quize.ExamQuizPages.ShowTrueAnswersName
import com.example.piston.ui.theme.textColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers

object ExamQuizPages {
    var FirstTestPageName = "first_test_page"
    var ElementaryTestListName = "elementary_list_page"
    var ElementaryTestsName = "elementary_test_page"
    var ShowTrueAnswersName = "show_true_answers_page"
    var ElementaryResultName = "elementary_result_page"
    var AdvanceTestListName = "advanced_page_list"
    var AdvanceTestsName = "advanced_test_page"
    var AdvanceResultName = "advanced_result_page"
}

data class QuizResult(var answers: List<Int>, var quizList: List<QuizModel>)

@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun QuizPageManger(showBottom: (Boolean) -> Unit) {
    var navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    if (navBackStackEntry?.destination?.route != FirstTestPageName) showBottom(false)
    else showBottom(true)
    NavHost(
        navController = navController,
        startDestination = FirstTestPageName
    ) {
        composable(route = FirstTestPageName) {
            FirstTestPage(navController)
        }
        composable(route = ElementaryTestListName) {
            ElementaryTestList(navController) {
                navController.navigate("$ElementaryTestsName/$it")
            }
        }
        composable(route = AdvanceTestListName) {
            AdvancedTestList(navController) {
                navController.navigate("$AdvanceTestsName/$it")
            }
        }
        composable(
            route = "$ElementaryTestsName/{number}",
            arguments = listOf(navArgument("number") {
                type = NavType.IntType
            })
        ) {
            ElementaryTestsPage(navController, it.arguments?.getInt("number")!!)
        }
        composable(AdvanceTestsName) {

        }
        composable(
            route = "$ElementaryResultName/{result}",
            arguments = listOf(navArgument("result") {
                type = NavType.StringType
            })
        ) {
            var resultString = it.arguments?.getString("result", "") ?: ""
            var gson = Gson()
            var type = object : TypeToken<QuizResult>() {}.type
            var quizResult = gson.fromJson<QuizResult>(resultString, type)
            ElementaryTestResult(navController, quizResult)
        }
        composable(route = "$ShowTrueAnswersName/{result}",
            arguments = listOf(
                navArgument("result") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            var resultString = it.arguments?.getString("result", "") ?: ""
            var gson = Gson()
            var type = object : TypeToken<QuizResult>() {}.type
            var quizResult = gson.fromJson<QuizResult>(resultString, type)
            TestLayout(
                modifier = Modifier.fillMaxSize(),
                quizList = quizResult.quizList,
                showCorrectAnswer = true,
                selectedAnswerList = quizResult.answers,
                selectedAnswerOnChange = {

                },
                selectable = false
            )
        }
    }
}

//@Composable
//@ReadOnlyComposable
//fun dimensionResource(@DimenRes id: Int): Dp {
//    val context = LocalContext.current
//    val density = LocalDensity.current
//    val pxValue = context.resources.getDimension(id)
//    return Dp(pxValue / density.density)
//}

@Composable
fun FirstTestPage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(color = colorResource(id = R.color.layout_background))
            .verticalScroll(state = rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(60.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = R.string.TestCategoryTitle_txt),
                modifier = Modifier
                    .align(Alignment.Center),
                color = colorResource(id = R.color.textColor_deep_blue),
                fontSize = dimensionResource(id = R.dimen.font_header_2).value.sp,
                fontFamily = FontFamily(Font(R.font.shabnam))
            )
        }
        ElementaryTestBanner {
            navController.navigate(route = ExamQuizPages.ElementaryTestListName)
        }
        AdvancedTestBanner {
            navController.navigate(route = ExamQuizPages.AdvanceTestListName)
        }
        Spacer(modifier = Modifier.padding(vertical = 30.dp))
    }
}

@Composable
fun ElementaryTestBanner(onPageChange: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
                onPageChange()
            }) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(3f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.BaseTestTitle),
                        fontFamily = FontFamily(Font(R.font.shabnam)),
                        fontSize = dimensionResource(id = R.dimen.font_header_2).value.sp,
                        color = colorResource(id = R.color.textColor_deep_blue),
                        modifier = Modifier.align(Alignment.End)
                    )

                    AutoSizeText(
                        text = stringResource(id = R.string.course_quiz_body_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        color = colorResource(id = R.color.textColor_deep_blue),
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.base_test_img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2.5f),
                    contentScale = ContentScale.Crop
                )

            }
        }

    }
}

@Composable
fun AdvancedTestBanner(onPageChange: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { }) {
            Row(modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onPageChange()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.test_img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2.5f),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(3f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.ExamTestTitle),
                        fontFamily = FontFamily(Font(R.font.shabnam)),
                        fontSize = dimensionResource(id = R.dimen.font_header_2).value.sp,
                        color = colorResource(id = R.color.textColor_deep_blue),
                        modifier = Modifier.align(Alignment.End)
                    )
                    AutoSizeText(
                        text = stringResource(id = R.string.course_exam_body_string),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        color = colorResource(id = R.color.textColor_deep_blue),
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    )
                }


            }
        }

    }
}

@Composable
fun TopBar(modifier: Modifier, text: String, onBackPress: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ImageIcon(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically),
            backColor = colorResource(id = R.color.light_blue),
            image = R.drawable.ic_back,
            clickable = {
                onBackPress()
            },
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        AutoSizeText(
            text = text,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .weight(2f)
                .align(CenterVertically),
            color = colorResource(id = R.color.textColor_deep_blue)
        ) {
            it.gravity = Gravity.RIGHT
        }

    }
}

@ExperimentalFoundationApi
@Composable
fun TestListLayout(
    list: List<TestPercentEntity>,
    onPageChange: (Int) -> Unit,
    onBackPress: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.layout_background))
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(4.dp), text = stringResource(id = R.string.ExamListTitle_txt)
        ) {
            onBackPress()
        }
        LazyVerticalGrid(
            cells = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp), modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = colorResource(
                        id = R.color.layout_background
                    )
                )
        ) {
            list.forEachIndexed { index, item ->
                item {
                    val color = when (item.percent) {
                        0 -> android.R.color.darker_gray
                        in 1..50 -> {
                            R.color.trikyRed
                        }
                        in 51..79 -> {
                            R.color.golden
                        }
                        in 80..100 -> {
                            R.color.light_green
                        }
                        else -> R.color.light_green
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(vertical = 6.dp,horizontal = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onPageChange(item.id)
                            }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.name,
                                    fontFamily = FontFamily(Font(R.font.shabnam)),
                                    fontSize = dimensionResource(
                                        id = R.dimen.font_body
                                    ).value.sp,
                                    color = colorResource(id = R.color.textColors)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.LastYourTry_txt),
                                    fontFamily = FontFamily(Font(R.font.shabnam)),
                                    fontSize = dimensionResource(
                                        id = R.dimen.font_subtitle
                                    ).value.sp,
                                    color = colorResource(id = R.color.textColors)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = "${item.percent}%",
                                    fontFamily = FontFamily(Font(R.font.shabnam)),
                                    fontSize = dimensionResource(
                                        id = R.dimen.font_body
                                    ).value.sp,
                                    color = colorResource(id = color)
                                )
                            }
                            LinearProgressIndicator(
                                progress = (item.percent.toFloat() / 100f).coerceIn(0f, 1f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(13.dp),
                                color = colorResource(
                                    id = color
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}

@ExperimentalFoundationApi
@Composable
fun AdvancedTestList(
    navController: NavHostController,
    viewModel: ViewModel = viewModel(),
    onPageChange: (Int) -> Unit
) {
    val list by viewModel.examPercentList.collectAsState(initial = null, Dispatchers.IO)
    list?.let {
        TestListLayout(it, onPageChange, onBackPress = {

        })
    }
}

@ExperimentalFoundationApi
@Composable
fun ElementaryTestList(
    navController: NavHostController,
    viewModel: ViewModel = viewModel(),
    onPageChange: (Int) -> Unit
) {
    val list by viewModel.quizPercentList.collectAsState(initial = null, Dispatchers.IO)
    list?.let {
        TestListLayout(it, onPageChange, onBackPress = {
            navController.popBackStack()
        })
    }
}

@ExperimentalPagerApi
@Composable
fun ElementaryTestsPage(
    navController: NavHostController,
    number: Int,
    viewModel: ViewModel = viewModel()
) {
    var list: List<QuizModel>? by remember {
        mutableStateOf(null)
    }
    var lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = "start") {
        viewModel.getQuizList2(number).observe(lifeCycleOwner) {
            list = it
        }
    }
    list?.let {
        ExamTestPage(navController, it)
    }
}

@Composable
fun AdvancedTestsPage() {

}

@Composable
fun ElementaryTestResult(
    navController: NavHostController,
    quizResult: QuizResult,
    viewModel: ViewModel = viewModel()
) {
    var correctAnswerCount = 0
    var answers = quizResult.answers
    var testList = quizResult.quizList
    answers.forEachIndexed { index, item ->
        if (testList[index].true_answer == item) {
            correctAnswerCount++
        }
    }
    var percent = correctAnswerCount / answers.size.toFloat()
    percent *= 100
    viewModel.setQuizPercent(quizResult.quizList[0].test_number, percent.toInt())
    Box(modifier = Modifier.fillMaxSize()) {
        ExamResultPage(navController = navController, correctAnswerCount, percent, quizResult)
    }
}

@Composable
fun AdvancedTestResult() {
}
