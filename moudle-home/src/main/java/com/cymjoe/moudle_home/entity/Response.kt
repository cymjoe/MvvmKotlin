package com.cymjoe.moudle_home.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

data class HomeResponse(override val itemType: Int, val name: String) : MultiItemEntity
