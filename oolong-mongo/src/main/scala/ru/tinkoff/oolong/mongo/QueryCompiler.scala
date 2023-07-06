package ru.tinkoff.oolong.mongo

import scala.quoted.*

import org.mongodb.scala.bson.{BsonDocument, BsonInt64}
import ru.tinkoff.oolong.*
import ru.tinkoff.oolong.dsl.*
import ru.tinkoff.oolong.mongo.dsl.Aggregation

/**
 * Compile a BSON description of the update.
 * @param input
 *   Description of the update written in oolong DSL.
 */
inline def update[Doc](inline input: Updater[Doc] => Updater[Doc]): BsonDocument = ${ updateImpl('input) }

/**
 * Compile a BSON query.
 * @param input
 *   Scala code describing the query.
 */
inline def query[Doc](inline input: Doc => Boolean): BsonDocument = ${ queryImpl('input) }

inline def aggregation[DocIn](inline input: Aggregation[DocIn] => Aggregation[_]): BsonDocument = ${
  aggregationImpl('input)
}

private[oolong] def updateImpl[Doc: Type](
    input: Expr[Updater[Doc] => Updater[Doc]]
)(using quotes: Quotes): Expr[BsonDocument] = {
  import quotes.reflect.*
  import MongoUpdateCompiler.*

  val parser = new DefaultAstParser

  val ast = parser.parseUExpr(input)

  val optRepr   = opt(ast)
  val optimized = optimize(optRepr)

  report.info("AST:\n" + pprint(ast) + "\nGenerated Mongo query:\n" + render(optimized))

  target(optimized)
}

private[oolong] def queryImpl[Doc: Type](input: Expr[Doc => Boolean])(using quotes: Quotes): Expr[BsonDocument] = {
  import quotes.reflect.*
  import MongoQueryCompiler.*

  val parser = new DefaultAstParser

//  println("")
//  println(input.asTerm.show(using Printer.TreeStructure))
//  println("")

  val ast          = parser.parseQExpr(input)
  val optimizedAst = LogicalOptimizer.optimize(ast)

  val optRepr   = opt[Doc](optimizedAst)
  val optimized = optimize(optRepr)

  report.info("Optimized AST:\n" + pprint(optimizedAst) + "\nGenerated Mongo query:\n" + render(optimized))

  target(optimized)
}

private[oolong] def aggregationImpl[DocIn: Type](
    input: Expr[Aggregation[DocIn] => Aggregation[_]]
)(using quotes: Quotes): Expr[BsonDocument] = {
  import quotes.reflect.*
  import MongoQueryCompiler.*

  println("")
  println(input.asTerm.show(using Printer.TreeStructure))
  println("")

  val parser = new DefaultAstParser



  //val ast          = parser.parseQExpr(input)
//  val optimizedAst = LogicalOptimizer.optimize(ast)
//
//  val optRepr   = opt[Doc](optimizedAst)
//  val optimized = optimize(optRepr)
//
//  report.info("Optimized AST:\n" + pprint(optimizedAst) + "\nGenerated Mongo query:\n" + render(optimized))
//
//  target(optimized)

  '{ Tuple() }
  '{ BsonDocument() }
  //???
}
