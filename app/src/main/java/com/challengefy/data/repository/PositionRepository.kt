package com.challengefy.data.repository

import com.challengefy.data.model.Position
import io.reactivex.Single

interface PositionRepository {
    fun getCurrentPosition(): Single<Position>
}
