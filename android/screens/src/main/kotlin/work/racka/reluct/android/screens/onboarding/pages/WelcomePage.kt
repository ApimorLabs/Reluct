package work.racka.reluct.android.screens.onboarding.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R

@Composable
internal fun WelcomePage(
    modifier: Modifier = Modifier
) {

    val drawableSize = 400.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize)
                    .padding(Dimens.MediumPadding.size),
                painter = painterResource(id = R.drawable.welcome_mobile),
                contentDescription = null
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.welcome_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}