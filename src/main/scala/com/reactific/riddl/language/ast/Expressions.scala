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
  case class ArgList(
    args: ListMap[Identifier, Expression] = ListMap
      .empty[Identifier, Expression]
  ) extends RiddlNode 
}
