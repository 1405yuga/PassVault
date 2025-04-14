package com.example.passvault.ui.screens.loader

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passvault.R
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.ui.state.ScreenState

@Composable
fun LoaderScreen(
    toLoginScreen: () -> Unit,
    toCreateMasterKeyScreen:()-> Unit,
    toEnterMasterKeyScreen: () -> Unit,
    loaderViewModel: LoaderViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = loaderViewModel.screenState.collectAsState().value
    LaunchedEffect(true) {
        loaderViewModel.checkSession()
    }
    LaunchedEffect(state) {
        when (state) {
            is ScreenState.Loaded -> {
                Log.d("LoaderScreen", "Screen state result : ${state.result}")
                when (state.result) {
                    UserState.NOT_LOGGED_IN -> toLoginScreen()
                    UserState.DONT_HAVE_MASTER_KEY -> toCreateMasterKeyScreen()
                    UserState.HAVE_MASTER_KEY -> toEnterMasterKeyScreen()
                }
            }

            is ScreenState.Error -> {
                Toast.makeText(context, "Unable to load", Toast.LENGTH_LONG).show()
            }

            is ScreenState.Loading, is ScreenState.PreLoad -> {}
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ScreenState.Loaded, is ScreenState.PreLoad, is ScreenState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ScreenState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "todo: some message")
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))
                    Button(onClick = {}) {
                        Text("Retry")
                    }
                }

            }
        }
    }
}

@Composable
@Preview
fun LoaderScreenPreview() {
    Surface {
        LoaderScreen(
            loaderViewModel = LoaderViewModel(AuthRepository(SupabaseModule.mockClient)),
            toLoginScreen = {},
            toCreateMasterKeyScreen = {},
            toEnterMasterKeyScreen = {}
        )
    }
}

