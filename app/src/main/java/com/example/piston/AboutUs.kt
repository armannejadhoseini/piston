package com.example.piston

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun aboutUs(navController: NavController){
    val context = LocalContext.current
    val columnScroll = rememberScrollState()
    Scaffold() {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(columnScroll)

        ) {
            TopAppBar(modifier = Modifier
                .fillMaxWidth()
                , backgroundColor = White) {
                Row() {
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
                            tint = Unspecified

                        )
                    }
                    Text(modifier = Modifier.fillMaxWidth(),text = stringResource(id = R.string.about_us),
                        color = colorResource(id = R.color.textColor_deep_blue),
                        textAlign = TextAlign.End)
                }
            }
            Image(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),contentScale = ContentScale.FillBounds,painter = painterResource(id = R.drawable.about_us_image) , contentDescription ="" )
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),color  = colorResource(id = R.color.textColor_deep_blue),fontSize = 10.sp,textAlign = TextAlign.Center,text = stringResource(
                id = R.string.about_us_body1
            ) +"\n"+ "\n"+ stringResource(
                id = R.string.about_us_body2
            ) +"\n"+ "\n" + stringResource(
                id = R.string.about_us_body3
            ) )
            IconButton(modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp)
                .align(CenterHorizontally)
                .padding(vertical = 10.dp)
                .background(
                    colorResource(
                        id = R.color.light_blue
                    )
                ),onClick = {

                val url = "http://pistoon.iranswan.ir/"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context.startActivity(i)

            }) {
                Row() {
                    Text(modifier = Modifier.padding(horizontal = 10.dp),color = colorResource(id = R.color.white),fontSize = 15.sp,text = stringResource(
                        id = R.string.WebSite_txt
                    ))
                    Icon(modifier = Modifier.size(30.dp),painter = painterResource(id = R.drawable.website),tint = Unspecified, contentDescription = "")
                }

            }
            IconButton(modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp)
                .padding(vertical = 10.dp)
                .align(CenterHorizontally)
                .background(
                    color = androidx.compose.ui.graphics.Color.Yellow
                ),onClick = { /*TODO*/ }) {
                Row() {
                    Text(modifier = Modifier.padding(horizontal = 10.dp),color = colorResource(id = R.color.white),fontSize = 15.sp,text = stringResource(
                        id = R.string.instagram_icon
                    ))
                    Icon(modifier = Modifier.size(40.dp),painter = painterResource(id = R.drawable.instagram),tint = Unspecified, contentDescription = "")
                }


            }
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),color  = colorResource(id = R.color.textColor_deep_blue),fontSize = 10.sp,text = stringResource(
                id = R.string.DevelopTeam_txt
            ) + "\n \n" + stringResource(
                id = R.string.DeveloperName_txt
            ) + "\n \n" + stringResource(
                id = R.string.UITeam_txt
            ) + "\n \n" + stringResource(
                id = R.string.UIName_txt
            ) ,textAlign = TextAlign.Center)
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),color  = colorResource(id = R.color.green),fontSize = 10.sp,text = stringResource(
                id = R.string.UniversityName_txt),textAlign =  TextAlign.Center)

        }

    }
}