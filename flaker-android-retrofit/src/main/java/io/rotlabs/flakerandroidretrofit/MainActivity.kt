package io.rotlabs.flakerandroidretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.rotlabs.flakerandroidapp.ui.theme.FlakerAndroidTheme
import io.rotlabs.flakerretrofit.FlakerInterceptor
import io.rotlabs.flakerretrofit.data.FlakerPrefs
import io.rotlabs.flakerretrofit.data.FlakerPrefsImpl
import io.rotlabs.flakerretrofit.domain.FlakerFailResponse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlakerAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlakerAndroidTheme {
        Greeting("Android")
    }
}