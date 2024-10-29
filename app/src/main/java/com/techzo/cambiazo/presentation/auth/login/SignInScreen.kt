package com.techzo.cambiazo.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.techzo.cambiazo.R
import com.techzo.cambiazo.common.components.ButtonApp
import com.techzo.cambiazo.common.components.CustomInput
import com.techzo.cambiazo.common.components.LoginGoogleApp
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.common.components.TextLink

@Composable
fun SignInScreen(openRegister: () -> Unit = {},
                 openApp: () -> Unit = {},
                 openForgotPassword: () -> Unit = {},
                 viewModel: SignInViewModel = hiltViewModel()){


    val state = viewModel.state.value
    val errorUsername = viewModel.errorUsername.value
    val errorPassword = viewModel.errorPassword.value
    val username = viewModel.username.value
    val password = viewModel.password.value

    MainScaffoldApp(
        paddingCard = PaddingValues(start = 40.dp , end = 40.dp,top = 70.dp),
        contentsHeader = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
                verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(R.drawable.cambiazo_logo_name),
                    contentDescription = "logo gmail",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                )
            }

        }
    ) {

        Text(
            text = "Iniciar Sesión",
            fontSize = 35.sp,
            modifier = Modifier
                .padding(bottom =35.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif),
        )


        CustomInput(
            value = username,
            placeHolder = "Correo electrónico",
            type = "Email",
            isError = errorUsername.data ?: false,
            messageError = errorUsername.message
        ) {
            viewModel.onUsernameChange(it)
        }
        Spacer(modifier = Modifier.height(15.dp))
        CustomInput(
            value = password,
            placeHolder = "Contraseña",
            type = "Password",
            isError = errorPassword.data ?: false,
            messageError = errorPassword.message
        ) {
            viewModel.onPasswordChange(it)
        }

        state.message.let{Text(text = state.message, color = Color.Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth())}
        TextLink("","Olvidé mi contraseña", clickable = {openForgotPassword()},Arrangement.End)


        state.data?.let {
            openApp()
        }
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 31.dp)
            ) {
                LinearProgressIndicator(
                    color = Color(0xFFFFD146),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
            }
        }else{
            ButtonApp("Iniciar Sesion", onClick = {
                viewModel.signIn()
            })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, top = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "o Inicia Sesion con",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif)
            )
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )

        }

        LoginGoogleApp()

        TextLink("¿Todavía no tienes cuenta?"," Regístrate", clickable = {openRegister()})

    }
}



