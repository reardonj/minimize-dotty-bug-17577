/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

import com.reactific.riddl.language.parsing.Terminals.*

trait Types extends AbstractDefinitions {

///////////////////////////////////////////////////////////// TYPES

  // We need "Expression" sealed trait from Expression.scala but it
  // depends on TypeExpression.scala so we make Expression derive from
  // this forward declaration so we can use it here.
  trait ForwardDeclaredExpression extends RiddlNode

  sealed trait TypeDefinition extends Definition

  sealed trait TypeExpression extends RiddlValue {
    def isAssignmentCompatible(other: TypeExpression): Boolean = {
      (other == this) || (other.getClass == this.getClass) ||
      (other.getClass == classOf[Abstract]) ||
      (this.getClass == classOf[Abstract])
    }
  }

  sealed trait NumericType extends TypeExpression {

    override def isAssignmentCompatible(other: TypeExpression): Boolean = {
      super.isAssignmentCompatible(other) || other.isInstanceOf[NumericType]
    }
  }

  sealed trait IntegerTypeExpression extends NumericType with TypeExpression
  sealed trait RealTypeExpression extends NumericType

  case class AliasedTypeExpression(loc: At, pathId: PathIdentifier) extends TypeExpression 

  // //////////////////////////////////////////////////////////////////////// TYPES

  sealed trait AggregateUseCase

  case object CommandCase extends AggregateUseCase

  case object EventCase extends AggregateUseCase

  case object QueryCase extends AggregateUseCase

  case object ResultCase extends AggregateUseCase 

  case object RecordCase extends AggregateUseCase 

  sealed trait Cardinality extends TypeExpression 


  case class Field(
    loc: At,
    id: Identifier,
    typeEx: TypeExpression,
    default: Option[ForwardDeclaredExpression] = None,
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends LeafDefinition
      with AlwaysEmpty
      with TypeDefinition
      with SagaDefinition
      with StateDefinition
      with FunctionDefinition
      with ProjectorDefinition 

  trait AggregateTypeExpression extends TypeExpression with Container[Field] 

  case class Aggregation(loc: At, fields: Seq[Field] = Seq.empty[Field]) extends AggregateTypeExpression

  object Aggregation {
    def empty(loc: At = At.empty): Aggregation = { Aggregation(loc) }
  }


  case class AggregateUseCaseTypeExpression(
    loc: At,
    usecase: AggregateUseCase,
    fields: Seq[Field] = Seq.empty[Field]
  ) extends AggregateTypeExpression 

  abstract class PredefinedType extends TypeExpression 

  case class Abstract(loc: At) extends PredefinedType 

}
