package com.example.jettip.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 2.dp),
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean=true,
    keyboardType: KeyboardType=KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions=KeyboardActions.Default
    ){
    OutlinedTextField(value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(labelId) },
        enabled = enabled,
        leadingIcon = { Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription="Money Icon") },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize=18.sp, color= MaterialTheme.colors.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        modifier = modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
    )
}

@Composable
fun RoundedButton(
    modifier: Modifier=Modifier,
    imageVector: ImageVector,
    onClick: ()->Unit,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp
){
    Card(
        modifier = modifier
            .padding(all = 4.dp)
            .clickable { onClick.invoke() }
            .then(Modifier.size(40.dp)),
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = elevation,
    ) {
        Icon(imageVector = imageVector, contentDescription = "Button Icon", tint=tint)
    }
}