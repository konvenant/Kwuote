package com.example.my_kwuotes.presentation.ui.components

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import coil.compose.AsyncImage
import com.example.kwuotes.R
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.data.models.SearchResult
import com.example.my_kwuotes.data.remote.models.QuoteDto
import com.example.my_kwuotes.presentation.utils.UserPrefManager
import com.example.my_kwuotes.presentation.utils.getContrastingColor
import com.example.my_kwuotes.presentation.utils.getRandomColor
import com.example.my_kwuotes.presentation.utils.translateText
import com.example.my_kwuotes.ui.theme.Blue
import com.example.my_kwuotes.ui.theme.Blue02
import com.example.my_kwuotes.ui.theme.Brown01
import com.example.my_kwuotes.ui.theme.Cream02
import com.example.my_kwuotes.ui.theme.Pink40
import com.example.my_kwuotes.ui.theme.Purple40
import com.example.my_kwuotes.ui.theme.Yellow40

@Composable
fun QuoteItem(
    quote: Quote,
    listState: LazyListState,
    onClick: (Quote) -> Unit
){
    val context = LocalContext.current
    val userPrefManager = UserPrefManager(context)
    val currentLanguage = userPrefManager.getUserSettings().currentLanguage
    val targetLanguage = when(currentLanguage){
        context.resources.getString(R.string.english) -> "en"
        context.resources.getString(R.string.french) -> "fr"
        context.resources.getString(R.string.spanish) -> "es"
        else -> "en"
    }
      val colorList = listOf(
          Blue,
          Blue02,
          Purple40,
          Brown01,
          Pink40,
          Yellow40,
          Cream02 ,
      )
    val color = getRandomColor(colorList)

    val textColor = getContrastingColor(color)

    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )
    val currentLocale = LocalContext.current.resources.configuration.locales
    val resources = LocalContext.current.resources

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                onClick(quote)
            }
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
//            .graphicsLayer {
//                alpha = 1 -listState.layoutInfo.normalizedItemPosition(quote.id).absoluteValue
//            }
            .then(AnimatedGradientBackgroundModifier(color1 = color, color2 = Color.Black))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_2),
            contentDescription = "author image",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.content,
            style = TextStyle.Default.copy(
                fontSize = 18.sp,
                fontFamily = font,
                color = textColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.author,
            style = TextStyle.Default.copy(
                fontSize = 14.sp,
                fontFamily = font2,
                color = textColor
            )
            ,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )
    }
}


@Composable
fun MyQuoteItem(
    quote: Quote,
    listState: LazyListState,
    onClick: (Quote) -> Unit,
    onDelete: (Quote) -> Unit
){
    val colorList = listOf(
        Blue,
        Blue02,
        Purple40,
        Brown01,
        Pink40,
        Yellow40,
        Cream02 ,
    )
    val color = getRandomColor(colorList)

    val textColor = getContrastingColor(color)

    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )

    val context = LocalContext.current

    val userPref = UserPrefManager(context)

    val user = userPref.getImageFromPreferences(context)

    val currentLocale = LocalContext.current.resources.configuration.locales
    val resources = LocalContext.current.resources

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                onClick(quote)
            }
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
//            .graphicsLayer {
//                alpha = 1 -listState.layoutInfo.normalizedItemPosition(quote.id).absoluteValue
//            }
            .then(AnimatedGradientBackgroundModifier(color1 = color, color2 = Color.Black))
    ) {
       Row (
           modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp),
           verticalAlignment = Alignment.CenterVertically,
           ) {
          Box(modifier = Modifier.fillMaxWidth() ){
              AsyncImage(
                  model = user.image,
                  contentDescription = "author image",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                      .size(40.dp)
                      .clip(CircleShape)
                      .border(1.dp, Color.White, CircleShape)
                      .align(Alignment.Center)
              )

//              Spacer(modifier = Modifier.width(16.dp))
              IconButton(
                  onClick = { onDelete(quote) },
                  modifier = Modifier.align(Alignment.CenterEnd)
              ) {
                  Icon(
                      imageVector = Icons.Filled.Delete,
                      contentDescription = null,
                      tint = Color.White
                  )
              }
          }
       }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.content,
            style = TextStyle.Default.copy(
                fontSize = 18.sp,
                fontFamily = font,
                color = textColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.author,
            style = TextStyle.Default.copy(
                fontSize = 14.sp,
                fontFamily = font2,
                color = textColor
            )
            ,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )
    }
}

@Composable
fun SearchedQuoteItem(
    quote: SearchResult,
    onClick: (SearchResult) -> Unit
){
    val colorList = listOf(
        Blue,
        Blue02,
        Purple40,
        Pink40,
        Yellow40,
        Cream02 ,
    )
    val color = getRandomColor(colorList)

    val textColor = getContrastingColor(color)

    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick(quote) }
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(AnimatedGradientBackgroundModifier(color1 = color, color2 = Color.Black))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_2),
            contentDescription = "author image",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.content,
            style = TextStyle.Default.copy(
                fontSize = 18.sp,
                fontFamily = font,
                color = textColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.author,
            style = TextStyle.Default.copy(
                fontSize = 14.sp,
                fontFamily = font2,
                color = textColor
            )
            ,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            items(quote.tags){ tag ->
                Text(
                    text = tag,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Brown01, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    fontFamily = font3,
                    color = Color.White
                )
            }
        }
    }



}

@Composable
fun SearchedQuoteByAuthorItem(
    quote: QuoteDto,
    onClick: (QuoteDto) -> Unit
){
    val colorList = listOf(
        Blue,
        Blue02,
        Purple40,
        Pink40,
        Yellow40,
        Cream02 ,
    )
    val color = getRandomColor(colorList)

    val textColor = getContrastingColor(color)

    val font = FontFamily(
        Font(R.font.wellfleet_regular, FontWeight.Bold)
    )

    val font2 = FontFamily(
        Font(R.font.handlee_regular, FontWeight.Normal)
    )

    val font3 = FontFamily(
        Font(R.font.adventpro_variable, FontWeight.Normal)
    )
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick(quote) }
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(AnimatedGradientBackgroundModifier(color1 = color, color2 = Color.Black))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_2),
            contentDescription = "author image",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.content,
            style = TextStyle.Default.copy(
                fontSize = 18.sp,
                fontFamily = font,
                color = textColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = quote.author,
            style = TextStyle.Default.copy(
                fontSize = 14.sp,
                fontFamily = font2,
                color = textColor
            )
            ,
            modifier = Modifier
                .padding(16.dp,8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            items(quote.tags){ tag ->
                Text(
                    text = tag,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Brown01, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    fontFamily = font3,
                    color = Color.White
                )
            }

        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Preview
@Composable
fun QuoteItemPreview(){
  val search  =  SearchResult(
        _id = "1",
        author = "Albert Einstein",
        authorId = "abc123",
        authorSlug = "albert-einstein",
        content = "Everything is relative.",
        dateAdded = "2023-01-01",
        dateModified = "2023-01-01",
        length = 23,
        tags = listOf("science", "philosophy")
    )

//    SearchedQuoteItem(search)
}

// Returns the normalized center item offset (-1,1)
fun LazyListLayoutInfo.normalizedItemPosition(key: Any) : Float =
    visibleItemsInfo
        .firstOrNull { it.key == key }
        ?.let {
            val center = (viewportEndOffset + viewportStartOffset - it.size) / 2F
            (it.offset.toFloat() - center) / center
        }
        ?: 0F




