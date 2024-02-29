package com.otus.otuskotlin.marketplace

@JsModule("js-big-decimal")
@JsNonModule
open external class BigDecimal {
    open var value: Any

    constructor(number: Number = definedExternally)
    constructor()
    constructor(number: String = definedExternally)

    open fun getValue(): String
    open fun getPrettyValue(digits: Any, separator: Any): String
    open fun round(precision: Number = definedExternally, mode: RoundingModes = definedExternally): BigDecimal
    open fun floor(): BigDecimal
    open fun ceil(): BigDecimal
    open fun add(number: BigDecimal): BigDecimal
    open fun subtract(number: BigDecimal): BigDecimal
    open fun multiply(number: BigDecimal): BigDecimal
    open fun divide(number: BigDecimal, precision: Any): BigDecimal
    open fun modulus(number: BigDecimal): BigDecimal
    open fun compareTo(number: BigDecimal): dynamic /* 1 | 0 | "-1" */
    open fun negate(): BigDecimal

    companion object {
        var RoundingModes: Any
        var validate: Any
        fun getPrettyValue(number: Any, digits: Any, separator: Any): String
        fun round(number: Any, precision: Number = definedExternally, mode: RoundingModes = definedExternally): String
        fun floor(number: Any): Any
        fun ceil(number: Any): Any
        fun add(number1: Any, number2: Any): String
        fun subtract(number1: Any, number2: Any): String
        fun multiply(number1: Any, number2: Any): String
        fun divide(number1: Any, number2: Any, precision: Any): String
        fun modulus(number1: Any, number2: Any): String
        fun compareTo(number1: Any, number2: Any): dynamic /* 1 | 0 | "-1" */
        fun negate(number: Any): String
    }
}

@Suppress("ENUM_CLASS_IN_EXTERNAL_DECLARATION_WARNING") // temporary solution
external enum class RoundingModes {
    CEILING /* = 0 */,
    DOWN /* = 1 */,
    FLOOR /* = 2 */,
    HALF_DOWN /* = 3 */,
    HALF_EVEN /* = 4 */,
    HALF_UP /* = 5 */,
    UNNECESSARY /* = 6 */,
    UP /* = 7 */
}