package com.cymjoe.moudle_login

import com.cymjoe.lib_base.constant.Constant
import com.cymjoe.lib_http.BaseResp
import com.cymjoe.lib_module.LoginRequestEntity
import com.cymjoe.lib_module.LoginResponseEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(Constant.LOGIN)
    suspend fun login(@Body loginRequestEntity: LoginRequestEntity): BaseResp<LoginResponseEntity>
}