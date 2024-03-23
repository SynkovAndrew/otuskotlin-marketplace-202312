package com.otus.otuskotlin.lesson

import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class Hw1Sql {

    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where { "col_a" eq "id" }
        }

        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where {
                "col_a" nonEq 0
            }
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null)"

        val real = query {
            from("table")
            where {
                "col_a" eq 4
                "col_b" nonEq null
            }
        }

        checkSQL(expected, real)
    }
}

@QueryDsl
class SqlSelectBuilder {
    private val columns: MutableList<String> = mutableListOf()
    private var table: String? = null
    private var where: String? = null

    fun select(vararg columns: String) {
        this.columns.addAll(columns)
    }

    fun from(table: String) {
        this.table = table
    }

    fun where(whereBuilder: WhereBuilder.() -> Unit) {
        where = WhereBuilder().apply(whereBuilder).compose()
    }

    fun build(): String {
        requireNotNull(table)

        return buildString {
            append("select ${columns.takeIf { it.isNotEmpty() }?.joinToString() ?: "*"}")
            append(" from $table")
            if (where != null) append(" $where")
        }
    }

    class WhereBuilder {
        private val criteria: MutableList<Triple<String, String, Any?>> = mutableListOf()

        infix fun String.eq(value: Any?) {
            criteria.add(
                Triple(
                    this,
                    if (value == null) "is" else "=",
                    mapValue(value)
                )
            )
        }

        infix fun String.nonEq(value: Any?) {
            criteria.add(
                Triple(
                    this,
                    if (value == null) "!is" else "!=",
                    mapValue(value)
                )
            )
        }

        fun compose(): String {
            return buildString {
                append("where")
                when (criteria.size) {
                    0 -> throw IllegalArgumentException("Should be criteria")
                    1 -> append(" ${criteria[0].first} ${criteria[0].second} ${criteria[0].third}")
                    else -> append(criteria.joinToString(
                        prefix = " (",
                        postfix = ")",
                        separator = " or "
                    ) { "${it.first} ${it.second} ${it.third}" })
                }
            }
        }

        private fun mapValue(value: Any?): Any? {
            return when (value) {
                is Number -> value
                is String -> "'$value'"
                else -> null
            }
        }
    }
}

fun query(block: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
    return SqlSelectBuilder().apply(block)
}

@DslMarker
annotation class QueryDsl