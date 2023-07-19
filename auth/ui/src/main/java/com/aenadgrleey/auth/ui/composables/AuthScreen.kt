package com.aenadgrleey.auth.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aenadgrleey.auth.domain.AuthNavigator
import com.aenadgrleey.auth.ui.models.UiAction
import com.aenadgrleey.auth.ui.models.UiEvent
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import com.aenadgrleey.resources.R as CommonR

@Composable
fun AuthScreen(
    uiEvents: Flow<UiEvent>,
    onUiAction: (UiAction) -> Unit,
    navigator: AuthNavigator,
) {
    val context = LocalContext.current

    val authSdk = remember(Unit) {
        YandexAuthSdk(context, YandexAuthOptions(context, true))
    }
    val authIntent = remember(Unit) {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        authSdk.createLoginIntent(loginOptionsBuilder.build())
    }

    val authLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val yandexAuthToken = try {
            authSdk.extractToken(result.resultCode, result.data)
        } catch (_: Exception) {
            null
        }
        yandexAuthToken?.let { it1 -> onUiAction(UiAction.OnSignIn(it1.value)) }
    }

    LaunchedEffect(Unit) {
        uiEvents.collectLatest {
            when (it) {
                UiEvent.AuthSuccess -> navigator.onSuccessAuth()
                UiEvent.AuthRequest -> authLauncher.launch(authIntent)
            }
        }
    }
    AuthScreen(onUiAction)
}

@Composable
fun AuthScreen(
    onUiAction: (UiAction) -> Unit,
) {
    if (LocalConfiguration.current.run { screenWidthDp < screenHeightDp })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                Modifier
                    .padding(horizontal = 48.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = CommonR.drawable.app_icon),
                    contentDescription = null
                )
                Text(
                    text = LocalContext.current.resources.getString(CommonR.string.app_name),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 0.dp, bottom = 48.dp),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                AuthScreenSignInButton(Modifier, onUiAction)
            }
        }
    else
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(48.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = CommonR.drawable.app_icon),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .width(IntrinsicSize.Max)
                    .padding(horizontal = 48.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = LocalContext.current.resources.getString(CommonR.string.app_name),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(36.dp))
                AuthScreenSignInButton(Modifier, onUiAction)
            }
        }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun Preview2() {
    AuthScreen {}
}

@Preview
@Composable
fun Preview1() {
    AuthScreen {}
}

