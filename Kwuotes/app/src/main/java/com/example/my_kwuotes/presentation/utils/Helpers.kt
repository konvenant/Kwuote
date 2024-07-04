package com.example.my_kwuotes.presentation.utils

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.getSystemService
import com.example.kwuotes.R
import com.example.my_kwuotes.data.local.models.TagsItemEntity
import com.example.my_kwuotes.data.models.Quote
import com.example.my_kwuotes.presentation.ui.MainActivity
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID
import kotlin.coroutines.resume

fun getError(err: String?) :String {
    val jsonObj = JSONObject(err!!)
    val error = jsonObj.getString("message")
    val errorMessage = "An Error Occurred: $error"
    return errorMessage
}

fun encodeToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun decodeBase64(input: String): Bitmap {
    val decodedByte = Base64.decode(input, 0)
    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
}

fun getRandomColor(colors: List<Color>): Color {
   return colors.random()
}

fun getContrastingColor(color: Color) : Color{
    val redComponent = color.red * 255
    val blueComponent = color.blue * 255
    val greenComponent = color.blue * 255

    val brightness  = (redComponent  * 0.299 + greenComponent  * 0.587 + blueComponent * 0.114) / 255
    return if(brightness > 0.5) Color.Black else Color.White
}



fun getDummyQuotes(): List<Quote> {
    return listOf(
        Quote(
            id = 1,
            quoteId = "sjsjsjgsjg",
            author = "Albert Einstein",
            authorSlug = "albert-einstein",
            content = "Life is like riding a bicycle. To keep your balance you must keep moving.",
            dateAdded = "2024-01-01",
            dateModified = "2024-01-02",
            length = 67,
            tags = listOf("life", "balance", "inspirational"),
            category = "love"
        ),
        Quote(
            id = 2,
            quoteId = "sjsjsjgsjgxx",
            author = "Isaac Newton",
            authorSlug = "isaac-newton",
            content = "If I have seen further it is by standing on the shoulders of Giants.",
            dateAdded = "2024-01-03",
            dateModified = "2024-01-04",
            length = 64,
            tags = listOf("science", "knowledge", "humility"),
            category = "love"
        ),
        Quote(
            id = 3,
            quoteId = "kddkdjd",
            author = "Yoda",
            authorSlug = "yoda",
            content = "Do, or do not. There is no try.",
            dateAdded = "2024-01-05",
            dateModified = "2024-01-06",
            length = 21,
            tags = listOf("motivation", "action"),
            category = "love"
        ),
        Quote(
            id = 4,
            quoteId = "sw2jsjsjgsjg",
            author = "Nelson Mandela",
            authorSlug = "nelson-mandela",
            content = "It always seems impossible until it’s done.",
            dateAdded = "2024-01-07",
            dateModified = "2024-01-08",
            length = 39,
            tags = listOf("perseverance", "inspiration", "success"),
            category = "love"
        )
    )
}


fun getDummyCategoryItem(): List<FakeCategoryItem> {val adventPro = FontFamily(
    fonts = listOf(
        Font(R.font.adventpro_italic, weight = FontWeight.Normal),
        Font(R.font.adventpro_variable, weight = FontWeight.Normal)
    )
)


    val dancingScript = FontFamily(
        fonts = listOf(
            Font(R.font.dancingscript_variable, weight = FontWeight.Normal)
        )
    )

    val handlee = FontFamily(
        fonts = listOf(
            Font(R.font.handlee_regular, weight = FontWeight.Normal)
        )
    )

    val wellfleet = FontFamily(
        fonts = listOf(
            Font(R.font.wellfleet_regular, weight = FontWeight.Normal)
        )
    )

    return listOf(
        FakeCategoryItem(
            "Love",
            22,
            true
        ),
        FakeCategoryItem(
            "Happiness",
            221,
            false
        ),
        FakeCategoryItem(
            "Sad",
            90,
            false
        ),
        FakeCategoryItem(
            "Movie",
            10,
            false
        ),
        FakeCategoryItem(
            "Generosity",
            2,
            false
        ),
    )
}

data class FakeCategoryItem(
    val name: String,
    val count: Int,
    val isSelected: Boolean
)

data class BottomNavigationItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNew: Boolean,
    val badgeCount: Int? = null,
    val route: String
)


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat() ,
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5)
            ),
            start = Offset(startOffsetX,0f),
            end = Offset(startOffsetX + size.width.toFloat(),size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}



fun generateDummyTagsItems(): List<TagsItemEntity> {
    return listOf(
        TagsItemEntity(
            id = 0,
            tagId = UUID.randomUUID().toString(),
            dateAdded = "2023-01-01",
            dateModified = "2023-01-02",
            name = "Inspiration",
            quoteCount = 150,
            slug = "inspiration"
        ),
        TagsItemEntity(
            id = 1,
            tagId = UUID.randomUUID().toString(),
            dateAdded = "2023-02-01",
            dateModified = "2023-02-02",
            name = "Life",
            quoteCount = 200,
            slug = "life"
        ),
        TagsItemEntity(
            id = 2,
            tagId = UUID.randomUUID().toString(),
            dateAdded = "2023-03-01",
            dateModified = "2023-03-02",
            name = "Motivation",
            quoteCount = 250,
            slug = "motivation"
        ),
        TagsItemEntity(
            id = 3,
            tagId = UUID.randomUUID().toString(),
            dateAdded = "2023-04-01",
            dateModified = "2023-04-02",
            name = "Happiness",
            quoteCount = 300,
            slug = "happiness"
        ),
        TagsItemEntity(
            id = 4,
            tagId = UUID.randomUUID().toString(),
            dateAdded = "2023-05-01",
            dateModified = "2023-05-02",
            name = "Wisdom",
            quoteCount = 350,
            slug = "wisdom"
        ),
        TagsItemEntity(
            id = 5,
            tagId = UUID.randomUUID().toString(),
            dateAdded = "2023-06-01",
            dateModified = "2023-06-02",
            name = "Love",
            quoteCount = 400,
            slug = "love"
        )
    )
}



@RequiresApi(Build.VERSION_CODES.O)
fun showNotification(context: Context, quote: Quote) {
     val CHANNEL_ID = "quote_channel"
     val NOTIFICATION_ID = 1
     val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    val intent = Intent(context, MainActivity::class.java).apply {
        putExtra("quote_id", quote.quoteId)
        putExtra("category", quote.category)
    }
    
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


    val notification = Notification.Builder(context, CHANNEL_ID)
        .setContentTitle(quote.author)
        .setContentText(quote.content)
        .setSmallIcon(R.drawable.baseline_menu_book_24)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(NOTIFICATION_ID, notification)
}


suspend fun translateText(text: String, targetLanguage: String): String {
    return withContext(Dispatchers.IO) {
        val translate: Translate = TranslateOptions.getDefaultInstance().service
        val translation = translate.translate(
            text,
            Translate.TranslateOption.targetLanguage(targetLanguage)
        )
        translation.translatedText
    }
}

 fun createBitmapFromPicture(picture: Picture): Bitmap {
    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawPicture(picture)
    return bitmap
}

 suspend fun Bitmap.saveToDisk(context: Context): Uri {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "screenshot-${System.currentTimeMillis()}.png"
    )

    file.writeBitmap(this, Bitmap.CompressFormat.PNG, 100)

    return scanFilePath(context, file.path) ?: throw Exception("File could not be saved")
}


 suspend fun scanFilePath(context: Context, filePath: String): Uri? {
    return suspendCancellableCoroutine { continuation ->
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath),
            arrayOf("image/png")
        ) { _, scannedUri ->
            if (scannedUri == null) {
                continuation.cancel(Exception("File $filePath could not be scanned"))
            } else {
                continuation.resume(scannedUri)
            }
        }
    }
}

 fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

 fun shareBitmap(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(context, createChooser(intent, "Share your Quote"), null)
}