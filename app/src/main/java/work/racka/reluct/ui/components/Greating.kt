package work.racka.reluct.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import work.racka.reluct.ui.theme.ComposeAndroidTemplateTheme

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeAndroidTemplateTheme {
        Greeting("Android")
    }
}