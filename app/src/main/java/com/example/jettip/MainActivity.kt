package com.example.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettip.components.InputField
import com.example.jettip.components.RoundedButton
import com.example.jettip.ui.theme.JetTipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipApp {
                val totalAmountState = remember { mutableStateOf("") }
                val totalPerPersonState = remember { mutableStateOf(0f) }
                TopHeader(totalPerPersonState.value)
                SplitContent(
                    {totalPerPersonState.value = it},
                    totalAmountState,
                    {totalAmountState.value = it}
                )
            }
        }
    }
}

@Composable
fun JetTipApp(content: @Composable ()->Unit){
    JetTipTheme {
        Surface() {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)) {
                content()
            }

        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Float){
    Surface(modifier= Modifier
        .height(150.dp)
        .padding(20.dp)
        .fillMaxWidth()
        .clip(CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total Per Person",style = MaterialTheme.typography.h5)
            Text("$${totalPerPerson}",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold)
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SplitContent(onTotalPerPersonState: (Float)->Unit, totalAmountState: MutableState<String>, onTotalAmountStateChange: (String)->Unit){
    val splitNumber = remember {
        mutableStateOf(1)
    }
    var sliderState = remember {
        mutableStateOf(0f)
    }
    val validState = remember (totalAmountState.value){ totalAmountState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(modifier= Modifier
        .fillMaxWidth(),
        shape = CircleShape.copy(all = CornerSize(8.dp)),
        border= BorderStroke(1.dp, Color.LightGray),
    ) {
        Column {
            InputField(valueState = totalAmountState,
                labelId = "Enter Bill",
                enabled=true,
                onAction = KeyboardActions{
                    if (!validState) return@KeyboardActions
                    onTotalAmountStateChange(totalAmountState.value.trim())
                    keyboardController?.hide()
                    onTotalPerPersonState.invoke(totalAmountState.value.trim().toFloat())
                }
            )
            if (validState) {
                SplitButtons(totalAmountState,sliderState.value, splitNumber.value, {splitNumber.value = it}, {onTotalPerPersonState.invoke(it)})
                Spacer(modifier = Modifier.height(10.dp))
                TipAmount(sliderState.value, totalAmountState.value.trim().toFloat())
                Spacer(modifier = Modifier.height(10.dp))
                TipControl(
                    sliderState.value,
                    { sliderState.value = it },
                    splitNumber.value,
                    totalAmountState.value.toFloat(),
                    { onTotalPerPersonState.invoke(it) })
            }
        }
    }
}

@Composable
fun SplitButtons(totalAmountState: MutableState<String>,sliderStateValue: Float, splitNumber: Int, onSplitNumberChanged: (Int)->Unit, onTotalPerPersonState: (Float)->Unit){

    Row(modifier = Modifier.padding(start = 10.dp)) {
        Text("Split",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(150.dp))
        RoundedButton(imageVector = Icons.Default.Remove, onClick = {
            if (splitNumber > 1){
                onSplitNumberChanged(splitNumber-1)
                onTotalPerPersonState(totalAmountState.value.trim().toFloat() * (1+sliderStateValue)/(splitNumber-1))
            }
        })
        Text("$splitNumber",
            modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 9.dp)
        )
        RoundedButton(imageVector = Icons.Default.Add, onClick = {
            onSplitNumberChanged(splitNumber+1)
            onTotalPerPersonState(totalAmountState.value.trim().toFloat() * (1+sliderStateValue)/(splitNumber+1))
        })
    }
}
@Composable
fun TipAmount(sliderStateValue: Float, totalAmountStateValue: Float){
    Row(modifier = Modifier.padding(start = 10.dp)) {
        Text("Tip")
        Spacer(modifier = Modifier.width(200.dp))
        Text("${totalAmountStateValue * sliderStateValue}")
    }
}

@Composable
fun TipControl(sliderStateValue: Float, onSliderStateChanged: (Float)->Unit, splitNumber: Int,totalAmountStateValue: Float, onTotalPerPersonStateChanged: (Float)->Unit){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${(sliderStateValue * 100).toInt()}%")
        Spacer(modifier = Modifier.height(10.dp))
        Slider(value = sliderStateValue,
            steps = 5,
            modifier = Modifier.padding(horizontal = 16.dp),
            onValueChange = {
                onSliderStateChanged.invoke(it)
                val totalAmount = totalAmountStateValue * (1 + it)
                onTotalPerPersonStateChanged.invoke(totalAmount/splitNumber)
            })
    }
}
