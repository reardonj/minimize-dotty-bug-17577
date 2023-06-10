/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

import com.reactific.riddl.language.parsing.Terminals.*

import scala.collection.immutable.ListMap

/** A trait for inclusion into AST object in AST.scala */
trait Expressions extends Types {

//////////////////////////////////////////////////////////// VALUE EXPRESSIONS

  /** Base trait of all expressions
    */
  sealed trait Expression extends RiddlValue with ForwardDeclaredExpression 

  /** Base trait for expressions that yield a boolean value (a condition)
    */
  sealed abstract class Condition(loc: At) extends Expression 

  /** Base trait for expressions that yield a numeric value */
  sealed abstract class NumericExpression(loc: At) extends Expression 

  sealed abstract class StringExpression(loc: At) extends Expression 

  /** Represents the use of an arithmetic operator or well-known function call.
    * The operator can be simple like addition or subtraction or complicated
    * like pow, sqrt, etc. There is no limit on the number of operands but
    * defining them shouldn't be necessary as they are pre-determined by use of
    * the name of the operator (e.g. pow takes two floating point numbers, sqrt
    * takes one.
    * @param loc
    *   The location of the operator
    * @param operator
    *   The name of the operator (+, -, sqrt, ...)
    * @param operands
    *   A list of expressions that correspond to the required operands for the
    *   operator
    */

  case class ArithmeticOperator(
    loc: At,
    operator: String,
    operands: Seq[Expression]
  ) extends NumericExpression(loc)

  /** Represents an opoerator that is merely a reference to some value,
    * presumably an entity state value but could also be a projector or
    * repository value.
    *
    * @param loc
    *   The location of this expression
    * @param path
    *   The path to the value for this expression
    */
  case class ValueOperator(loc: At, path: PathIdentifier) extends Expression

  /** Represents a expression that will be specified later and uses the ???
    * syntax to represent that condition.
    *
    * @param loc
    *   The location of the undefined condition
    */
  case class UndefinedOperator(loc: At) extends Expression 

  /** The arguments of a [[FunctionCallExpression]] and
    * [[AggregateConstructionExpression]] is a mapping between an argument name
    * and the expression that provides the value for that argument.
    *
    * @param args
    *   A mapping of Identifier to Expression to provide the arguments for the
    *   function call.
    */
  case class ArgList(
    args: ListMap[Identifier, Expression] = ListMap
      .empty[Identifier, Expression]
  ) extends RiddlNode 
  /** A helper class for creating aggregates and messages that represents the
    * construction of the message or aggregate value from parameters
    *
    * @param msg
    *   A message reference that specifies the specific type of message to
    *   construct
    * @param args
    *   An argument list that should correspond to teh fields of the message
    */
  case class AggregateConstructionExpression(
    loc: At,
    msg: PathIdentifier,
    args: ArgList = ArgList()
  ) extends Expression 

  /** A helper class for creating expressions that represent the creation of a
    * new entity identifier for a specific kind of entity.
    *
    * @param loc
    *   The location of the expression in the source
    * @param entityId
    *   The [[PathIdentifier]] of the entity type for with the Id is created
    */
  case class NewEntityIdOperator(loc: At, entityId: PathIdentifier)
      extends Expression 

  /** A RIDDL Function call. The only callable thing here is a function
    * identified by its path identifier with a matching set of arguments
    *
    * @param loc
    *   The location of the function call expression
    * @param name
    *   The path identifier of the RIDDL Function being called
    * @param arguments
    *   An [[ArgList]] to pass to the function.
    */
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

  /** A syntactic convenience for grouping a list of expressions.
    *
    * @param loc
    *   The location of the expression group
    * @param expressions
    *   The expressions that are grouped
    */
  case class GroupExpression(loc: At, expressions: Seq[Expression])
      extends Expression 

  /** Ternary operator to accept a conditional and two expressions and choose
    * one of the expressions as the resulting value based on the conditional.
    *
    * @param loc
    *   The location of the ternary operator
    * @param condition
    *   The conditional expression that determines the result
    * @param expr1
    *   An expression for the result if the condition is true
    * @param expr2
    *   An expression for the result if the condition is false
    */
  case class Ternary(
    loc: At,
    condition: Condition,
    expr1: Expression,
    expr2: Expression
  ) extends Expression 

  /** An expression that is a literal constant integer value
    *
    * @param loc
    *   The location of the integer value
    * @param n
    *   The number to use as the value of the expression
    */
  case class IntegerValue(loc: At, n: BigInt) extends NumericExpression(loc) 

  /** An expression that is a liberal constant decimal value
    * @param loc
    *   The location of the decimal value
    * @param d
    *   The decimal number to use as the value of the expression
    */
  case class DecimalValue(loc: At, d: BigDecimal)
      extends NumericExpression(loc) 

  /** Represents a literal string parsed between quote characters in the input
    *
    * @param loc
    *   The location in the input of the opening quote character
    * @param s
    *   The parsed value of the string content
    */
  case class StringValue(loc: At, s: String) extends StringExpression(loc) 

///////////////////////////////////////////////////////////// Conditional Expressions

  /** A condition value for "true"
    * @param loc
    *   The location of this expression value
    */
  case class True(loc: At) extends Condition(loc) 

  /** A condition value for "false"
    * @param loc
    *   The location of this expression value
    */
  case class False(loc: At) extends Condition(loc) 

  /** Represents an arbitrary condition that is specified merely with a literal
    * string. This can't be easily processed downstream but provides the author
    * with the ability to include arbitrary ideas/concepts into an condition
    * expression. For example in a when condition:
    * {{{
    *   example foo { when "the timer has expired" }
    * }}}
    * shows the use of an arbitrary condition for the "when" part of a Gherkin
    * example.
    *
    * @param cond
    *   The arbitrary condition provided as a quoted string
    */
  case class ArbitraryCondition(loc: At, cond: LiteralString)
      extends Condition(loc) 

  /** Represents a condition that is merely a reference to some Boolean value,
    * presumably an entity state value or parameter.
    *
    * @param loc
    *   The location of this condition
    * @param path
    *   The path to the value for this condition
    */
  case class ValueCondition(loc: At, path: PathIdentifier)
      extends Condition(loc) 

  /** A RIDDL Function call to the function identified by its path identifier
    * with a matching set of arguments. This function must return a boolean
    * since it is defined as a Condition.
    *
    * @param loc
    *   The location of the function call expression
    * @param name
    *   The path identifier of the RIDDL Function being called
    * @param arguments
    *   An [[ArgList]] to pass to the function.
    */
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

  /** Represents one of the six comparison operators
    *
    * @param loc
    *   Location of the comparison
    * @param op
    *   The comparison operator
    * @param expr1
    *   The first operand in the comparison
    * @param expr2
    *   The second operand in the comparison
    */
  case class Comparison(
    loc: At,
    op: Comparator,
    expr1: Expression,
    expr2: Expression
  ) extends Condition(loc) 

  /** Not condition
    *
    * @param loc
    *   Location of the not condition
    * @param cond1
    *   The condition being negated
    */
  case class NotCondition(loc: At, cond1: Condition) extends Condition(loc) 

  /** Base class for conditions with two operands
    */
  abstract class MultiCondition(loc: At) extends Condition(loc) 

  /** And condition
    *
    * @param loc
    *   Location of the and condition
    * @param conditions
    *   The conditions (minimum 2) that must all be true for "and" to be true
    */
  case class AndCondition(loc: At, conditions: Seq[Condition])
      extends MultiCondition(loc) 

  /** Or condition
    *
    * @param loc
    *   Location of the `or` condition
    * @param conditions
    *   The conditions (minimum 2), any one of which must be true for "Or" to be
    *   true
    */
  case class OrCondition(loc: At, conditions: Seq[Condition])
      extends MultiCondition(loc) 

  /** Xor condition
    * @param loc
    *   Location of the `xor` condition
    * @param conditions
    *   The conditions (minimum 2), only one of which may be true for "xor" to
    *   be true.
    */
  case class XorCondition(loc: At, conditions: Seq[Condition])
      extends MultiCondition(loc) 

  /** An arbitrary expression provided by a LiteralString Arbitrary expressions
    * conform to the type based on the context in which they are found. Another
    * way to think of it is that arbitrary expressions are assignment compatible
    * with any other type For example, in an arithmetic expression like this
    * {{{
    *   +(42,"number of widgets in a wack-a-mole")
    * }}}
    * the arbitrary expression given by the string conforms to a numeric type
    * since the context is the addition of 42 and the arbitrary expression
    */
  case class ArbitraryExpression(cond: LiteralString) extends Expression 

  // /////////////////////////////////////////////////////////// Time Operators

  /** Base trait for expressions that yield a TimeStamp value */
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
