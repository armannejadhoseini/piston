package com.example.piston.ui.Quize

import android.content.res.ColorStateList
import android.view.Gravity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch

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
                .height(80.dp)
        ) {
            AutoSizeText(
                text = stringResource(id = R.string.TestCategoryTitle_txt),
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(
                        Alignment.Center
                    ),
                color = textColor,
                androidText = {
                    it.setLines(1)
                }
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
            .aspectRatio(1.8f)
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
                onPageChange()
            }) {
            Row(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.base_test_img),
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
                    AutoSizeText(
                        text = "آزمون مقدماتی",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.5f),
                        color = textColor,
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    )
                    AutoSizeText(
                        text = stringResource(id = R.string.course_quiz_body_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        color = textColor,
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    )
                }
            }
        }

    }
}

@Composable
fun AdvancedTestBanner(onPageChange: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.8f)
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
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(3f)
                        .padding(8.dp)
                ) {
                    AutoSizeText(
                        text = stringResource(id = R.string.ExamTestTitle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.5f),
                        color = textColor,
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    )
                    AutoSizeText(
                        text = stringResource(id = R.string.course_exam_body_string),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f),
                        color = textColor,
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.test_img),
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
            color = textColor
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
                            .aspectRatio(1f)
                            .padding(4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onPageChange(item.id)
                            }) {
                            AutoSizeText(
                                text = "آزمون مقدماتی :" + (index + 1).toString(),
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .weight(2f)
                                    .align(Alignment.CenterHorizontally),
                                color = textColor
                            ) {
                                it.setLines(1)
                            }
                            AutoSizeText(
                                text = "آخرین تلاش شما",
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .weight(1f)
                                    .align(Alignment.CenterHorizontally),
                                color = textColor
                            ) {
                                it.setLines(1)
                            }
                            AutoSizeText(
                                text = "${item.percent}%",
                                modifier = Modifier
                                    .fillMaxWidth(0.2f)
                                    .weight(1f)
                                    .align(Alignment.CenterHorizontally),
                                color = colorResource(id = color)
                            ) {
                                it.setLines(1)
                            }
                            LinearProgressIndicator(
                                progress = (item.percent.toFloat() / 100f).coerceIn(0f, 1f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.4f),
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
