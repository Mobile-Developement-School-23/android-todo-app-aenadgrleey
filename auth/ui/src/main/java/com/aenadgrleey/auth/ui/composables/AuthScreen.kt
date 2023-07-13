package com.aenadgrleey.auth.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.aenadgrleey.auth.ui.models.UiEvent
import com.aenadgrleey.resources.R
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    uiEvents: Flow<UiEvent>,
    onAuthRequest: () -> Unit,
    onAuthResponse: (String) -> Unit,
    onSuccess: () -> Unit,
) {
    val context = LocalContext.current

    val authSdk = remember(Unit) {
        YandexAuthSdk(context, YandexAuthOptions(context, true))
    }
    val authIntent = remember(Unit) {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        authSdk.createLoginIntent(loginOptionsBuilder.build())
    }

    val authLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val yandexAuthToken = try {
            authSdk.extractToken(it.resultCode, it.data)
        } catch (_: Exception) {
            null
        }
        yandexAuthToken?.let { it1 -> onAuthResponse(it1.value) }
    }

    LaunchedEffect(Unit) {
        uiEvents.collectLatest {
            when (it) {
                UiEvent.AuthSuccess -> onSuccess()
                UiEvent.AuthRequest -> authLauncher.launch(authIntent)
            }
        }
    }
    AuthScreen(onAuthRequest)
}

@Composable
fun AuthScreen(
    onAuthRequest: () -> Unit,
) {
    if (LocalConfiguration.current.run { screenWidthDp < screenHeightDp })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Text(
                text = "ToBeDone",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 0.dp, bottom = 48.dp),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onAuthRequest,
                shape = RoundedCornerShape(10.dp),
            ) {
                Row {
                    Image(
                        modifier = Modifier
                            .padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                            .size(36.dp),
                        painter = painterResource(R.drawable.yandex_logo),
                        contentDescription = null
                    )
                    Text(
                        text = "Login with Yandex",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
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
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 48.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "ToBeDone",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Row {
                        Image(
                            modifier = Modifier
                                .padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                                .size(36.dp),
                            painter = painterResource(R.drawable.yandex_logo),
                            contentDescription = null
                        )
                        Text(
                            text = "Login with Yandex",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
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

