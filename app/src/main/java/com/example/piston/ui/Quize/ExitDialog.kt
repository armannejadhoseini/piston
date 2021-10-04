package com.example.piston.ui.Quize

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import com.example.piston.AutoSizeText
import com.example.piston.R

data class ExitDialogProperties(
    val confirm: String,
    val cancel: String,
    val title: String,
    val image: Int
)

@Composable
fun ExitDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties,
    dataProperties: ExitDialogProperties,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest, properties = properties
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(3 / 4f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                AndroidView(factory = {
                    AppCompatImageView(it)
                }, update = {
                    it.layoutParams = ViewGroup.LayoutParams(-1, -1)
                    it.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            it.resources,
                            dataProperties.image,
                            null
                        )
                    )
                    it.scaleType = ImageView.ScaleType.FIT_CENTER
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f)
                        .padding(vertical = 8.dp)
                )
                AutoSizeText(
                    text = dataProperties.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    color = colorResource(id = R.color.textColors)
                ) {
                    it.setLines(1)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onCancel, colors = ButtonDefaults
                            .textButtonColors(
                                backgroundColor = colorResource(
                                    id = R.color.light_green
                                )
                            ),
                        modifier = Modifier
                            .fillMaxHeight(1f)
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        AutoSizeText(
                            text = dataProperties.cancel,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.5f),
                            color = Color.White
                        ) {
                            it.setLines(1)
                        }
                    }
                    TextButton(
                        onClick = onConfirm, colors = ButtonDefaults.textButtonColors(
                            backgroundColor = colorResource(
                                id = R.color.boronz
                            )
                        ),
                        modifier = Modifier
                            .fillMaxHeight(1f)
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        AutoSizeText(
                            text = dataProperties.confirm,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.4f),
                            color = Color.White
                        ) {
                            it.setLines(1)
                        }
                    }
                }
            }
        }
    }
}

