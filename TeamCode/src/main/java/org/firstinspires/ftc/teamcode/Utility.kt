package org.firstinspires.ftc.teamcode

import java.lang.reflect.Field



inline fun <reified T: Any> callableIteration(obj: Any, method: (T) -> Unit) {
    val fields: Array<Field> = obj.javaClass.declaredFields
    for (field in fields) {
        if (field.type == T::class.java) {
            field.isAccessible = true
            val result: T? = field.get(obj) as? T
            result?.let {
                method(result)
            }
        }
    }
}