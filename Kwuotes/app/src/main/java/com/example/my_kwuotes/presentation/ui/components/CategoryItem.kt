package com.example.my_kwuotes.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Cream01
import com.example.my_kwuotes.ui.theme.Cream02
import com.example.my_kwuotes.ui.theme.Purple40
import com.example.my_kwuotes.ui.theme.Purple80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(
    name: String,
    count: Int,
    isSelected: Boolean,
    onClick: (String) -> Unit
){
   OutlinedButton(
       onClick = {onClick(name)},
       modifier = Modifier
           .padding(8.dp),
       colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Color.White else Blue
       ),
       border = ButtonDefaults.outlinedButtonBorder.copy(
           width = 1.dp,
           brush = Brush.horizontalGradient(listOf(Blue, Blue02))
       )
   ) {
       Text(
           text = name,
           color = if (isSelected) Blue else Color.White,
           fontSize = 16.sp
       )
       Spacer(modifier = Modifier.width(8.dp))

       Badge(
           containerColor = if (isSelected) Purple40 else Cream01
       ){
           Text(
               text = count.toString(),
               color = if (isSelected) Color.White else MaterialTheme.colorScheme.secondary,
               modifier = Modifier
                   .padding(3.dp)
           )
       }
   }
}


@Preview
@Composable
fun PrevCategoryItem(){
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
  ) {
      CategoryItem(name = "Love", count = 32, isSelected = true) {

      }

      CategoryItem(name = "Happiness", count = 8, isSelected = false) {

      }
  }
}