package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config

@Config
object Constants {
    @Config
    object Lifter {
        const val SAFE_MODE = 390
        const val HIGH_CHAMBER = 680
        const val HIGH_BASKET = 1121
        const val SUBMERSIBLE_MID = 370
        const val SUBMERSIBLE_DOWN = 219
        const val PICKUP_SPIKE = 200
        const val CLIMB_POSITION = 1425
    }

    @Config
    object Boom {
        const val SAFE_MODE = 38
        const val HIGH_CHAMBER = 106
        const val HIGH_BASKET = 948 // 868
        const val SUBMERSIBLE = 868
        const val PICKUP_SPIKE = 280
    }

    @Config
    object Claw {
        const val OPEN = 0.3
        const val CLOSED = 0.55
   }
    @Config
    object StageTwo {
        const val LOCK = 1300
    }
}