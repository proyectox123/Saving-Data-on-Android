package com.raywenderlich.android.datadrop.viewmodel

import com.raywenderlich.android.datadrop.model.Drop

interface ClearDropListener {
    fun dropsCleared(drop: Drop);
}