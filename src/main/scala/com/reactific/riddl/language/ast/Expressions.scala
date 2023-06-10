/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

import com.reactific.riddl.language.parsing.Terminals.*

import scala.collection.immutable.ListMap

trait Expressions extends Types {

//////////////////////////////////////////////////////////// VALUE EXPRESSIONS

  sealed trait Expression extends RiddlValue with ForwardDeclaredExpression 

  sealed abstract class Condition(loc: At) extends Expression 

  sealed abstract class NumericExpression(loc: At) extends Expression 

  sealed abstract class StringExpression(loc: At) extends Expression 

  case class ArithmeticOperator(
    loc: At,
    operator: String,
    operands: Seq[Expression]
  ) extends NumericExpression(loc)

  case class ValueOperator(loc: At, path: PathIdentifier) extends Expression

  case class UndefinedOperator(loc: At) extends Expression 

  case class ArgList(
    args: ListMap[Identifier, Expression] = ListMap
      .empty[Identifier, Expression]
  ) extends RiddlNode 
  
  
  case class AggregateConstructionExpression(
    loc: At,
    msg: PathIdentifier,
    args: ArgList = ArgList()
  ) extends Expression 

  
  case class NewEntityIdOperator(loc: At, entityId: PathIdentifier)
      extends Expression 

      
  case class FunctionCallExpression(
    loc: At,
    name: PathIdentifier,
    arguments: ArgList
  ) extends Expression 

  case class ArbitraryOperator(
    loc: At,
    opName: LiteralString,
    arguments: ArgList
  ) extends Expression 

  case class GroupExpression(loc: At, expressions: Seq[Expression])
      extends Expression 

  case class Ternary(
    loc: At,
    condition: Condition,
    expr1: Expression,
    expr2: Expression
  ) extends Expression 

  case class IntegerValue(loc: At, n: BigInt) extends NumericExpression(loc) 

  case class DecimalValue(loc: At, d: BigDecimal)
      extends NumericExpression(loc) 

  case class StringValue(loc: At, s: String) extends StringExpression(loc) 

///////////////////////////////////////////////////////////// Conditional Expressions

  case class True(loc: At) extends Condition(loc) 

  case class False(loc: At) extends Condition(loc) 

  case class ArbitraryCondition(loc: At, cond: LiteralString)
      extends Condition(loc) 

  case class ValueCondition(loc: At, path: PathIdentifier)
      extends Condition(loc) 

  case class FunctionCallCondition(
    loc: At,
    name: PathIdentifier,
    arguments: ArgList
  ) extends Condition(loc) 

  sealed trait Comparator extends RiddlNode

  case object lt extends Comparator 

  case object gt extends Comparator

  case object le extends Comparator 

  case object ge extends Comparator 

  case object eq extends Comparator 

  case object ne extends Comparator 

  case class Comparison(
    loc: At,
    op: Comparator,
    expr1: Expression,
    expr2: Expression
  ) extends Condition(loc) 

  case class NotCondition(loc: At, cond1: Condition) extends Condition(loc) 

  abstract class MultiCondition(loc: At) extends Condition(loc) 

  case class AndCondition(loc: At, conditions: Seq[Condition])
      extends MultiCondition(loc) 

  case class OrCondition(loc: At, conditions: Seq[Condition])
      extends MultiCondition(loc) 

  case class XorCondition(loc: At, conditions: Seq[Condition])
      extends MultiCondition(loc) 

  case class ArbitraryExpression(cond: LiteralString) extends Expression 

  // /////////////////////////////////////////////////////////// Time Operators

  trait ValueFunctionExpression extends Expression 

  case class TimeStampFunction(
    loc: At,
    name: String,
    args: Seq[Expression] = Seq.empty[Expression]
  ) extends ValueFunctionExpression 
  case class DateFunction(
    loc: At,
    name: String,
    args: Seq[Expression] = Seq.empty[Expression]
  ) extends ValueFunctionExpression 

  case class NumberFunction(
    loc: At,
    name: String,
    args: Seq[Expression] = Seq.empty[Expression]
  ) extends NumericExpression(loc)
      with ValueFunctionExpression 

  case class StringFunction(
    loc: At,
    name: String,
    args: Seq[Expression] = Seq.empty[Expression]
  ) extends StringExpression(loc)
      with ValueFunctionExpression 
}
