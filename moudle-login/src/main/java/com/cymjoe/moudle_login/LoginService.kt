package com.cymjoe.moudle_login

import com.cymjoe.lib_base.constant.Constant
import com.cymjoe.lib_http.BaseResp
import com.cymjoe.lib_module.LoginRequest
import com.cymjoe.lib_module.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(Constant.LOGIN)
    suspend fun login(@Body loginRequestEntity: LoginRequest): BaseResp<LoginResponse>
}