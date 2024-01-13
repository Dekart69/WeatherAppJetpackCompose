package drawable.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maks_boss.jcwapp.R
import com.maks_boss.jcwapp.ui.theme.BlueLight
import kotlin.math.round

@Preview(showBackground = true)

@Composable
fun MainScreen() {
    Image(
        painter = painterResource(id = R.drawable.weath),
        contentDescription = "im1",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.85f),
        contentScale = ContentScale.FillBounds
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(BlueLight),
            colors=CardDefaults.cardColors(
                containerColor = BlueLight
            ),
            elevation=CardDefaults.cardElevation(
                5.dp
            ),
            shape= RoundedCornerShape(10.dp)
        ) {

            Text(text="Filled with color")
            Text(text="Filled with color")
            Text(text="Filled with color")
        }

    }


}