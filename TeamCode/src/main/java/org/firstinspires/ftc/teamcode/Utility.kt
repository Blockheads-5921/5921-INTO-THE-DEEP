package org.firstinspires.ftc.teamcode

import java.lang.reflect.Field


inline fun <reified T : Any> callableIteration(
    obj: Any,
    method: (T) -> Unit,
    exclusions: List<String> = listOf()
) {
    val fields: Array<Field> = obj.javaClass.declaredFields
    for (field in fields) {
        if (field.type == T::class.java) {
            if (exclusions.contains(field.name)) continue
            field.isAccessible = true
            val result: T? = field.get(obj) as? T
            result?.let {
                method(result)
            }
        }
    }
}

class Button {
    enum class States {
        TAP,  // moment press down
        DOUBLE_TAP,  // pressed down in quick succession
        HELD,  // continued press down
        UP,  // moment of release
        OFF,  // continued release
        NOT_INITIALIZED
    }

    private val doubleTapIntervalMs: Int = 500
    private var state: States? = null
    private var lastTapped: Long = -1

    init {
        state = States.NOT_INITIALIZED
    }

    fun getState(): States? {
        return state
    }

    private fun doubleTapIntervalNotSet(): Boolean {
        return doubleTapIntervalMs == -1
    }

    fun update(buttonPressed: Boolean): States {
        if (buttonPressed) {
            if (state == States.OFF || state == States.UP || state == States.NOT_INITIALIZED) {
                if (System.currentTimeMillis() - lastTapped < doubleTapIntervalMs) state =
                    States.DOUBLE_TAP
                else {
                    lastTapped = System.currentTimeMillis()
                    state = States.TAP
                }
            } else {
                state = States.HELD
            }
        } else {
            state = if (state == States.HELD || state == States.TAP || state == States.DOUBLE_TAP) {
                States.UP
            } else {
                States.OFF
            }
        }
        return state as States
    }

    fun state(state: States): Boolean {
        return this.state == state
    }

    fun up(): Boolean {
        return this.state == States.UP
    }
}