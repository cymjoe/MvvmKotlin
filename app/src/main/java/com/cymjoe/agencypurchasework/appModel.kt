package com.cymjoe.agencypurchasework


import com.cymjoe.agencypurchasework.main.MainViewModel
import com.cymjoe.agencypurchasework.splash.SplashViewModel
import com.cymjoe.moudle_home.home.HomeViewModel
import com.cymjoe.moudle_login.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { LoginViewModel() }
    viewModel { HomeViewModel() }


}

//当需要构建你的ViewModel对象的时候，就会在这个容器里进行检索
val appModule = listOf(viewModelModule)