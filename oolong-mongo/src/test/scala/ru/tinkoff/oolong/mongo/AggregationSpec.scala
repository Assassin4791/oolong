package ru.tinkoff.oolong.mongo

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.regex.Pattern

import org.mongodb.scala.bson.BsonArray
import org.mongodb.scala.bson.BsonBoolean
import org.mongodb.scala.bson.BsonDateTime
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.BsonDouble
import org.mongodb.scala.bson.BsonInt32
import org.mongodb.scala.bson.BsonInt64
import org.mongodb.scala.bson.BsonString
import org.mongodb.scala.bson.BsonValue
import org.scalatest.funsuite.AnyFunSuite

import ru.tinkoff.oolong.bson.BsonEncoder
import ru.tinkoff.oolong.bson.given
import ru.tinkoff.oolong.dsl.*

class AggregationSpec extends AnyFunSuite {

  trait TestClassAncestor {
    def intField: Int
  }

  case class TestClass(
      intField: Int,
      stringField: String,
      dateField: LocalDate,
      innerClassField: InnerClass,
      optionField: Option[Long],
      optionInnerClassField: Option[InnerClass],
      listField: List[Double]
  ) extends TestClassAncestor

  case class InnerClass(
      fieldOne: String,
      fieldTwo: Int
  ) derives BsonEncoder

  test("test") {

    val q2 = aggregation[TestClass](
      x => x
        .`match`(_.intField == 2)
        .`match`(_.intField > 1)
        .`match`(_.intField < 3)

      
//        .`match`(
//          query[TestClass](_.intField > 1)
//        )
//        .`match`(
//          query[TestClass](_.intField < 3)
//        )
    )


    val q = query[TestClass](_.intField == 2)

    assert(q == BsonDocument("intField" -> BsonInt32(2)))
  }
}
