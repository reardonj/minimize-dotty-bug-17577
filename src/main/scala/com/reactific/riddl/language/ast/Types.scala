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

  case class Optional(loc: At, typeExp: TypeExpression) extends Cardinality 

  case class ZeroOrMore(loc: At, typeExp: TypeExpression) extends Cardinality 

  case class OneOrMore(loc: At, typeExp: TypeExpression) extends Cardinality 

  case class SpecificRange(
    loc: At,
    typeExp: TypeExpression,
    min: Long,
    max: Long
  ) extends Cardinality 

  case class Enumerator(
    loc: At,
    id: Identifier,
    enumVal: Option[Long] = None,
    brief: Option[LiteralString] = Option.empty[LiteralString],
    description: Option[Description] = None
  ) extends LeafDefinition
      with TypeDefinition 

  case class Enumeration(loc: At, enumerators: Seq[Enumerator]) extends IntegerTypeExpression 

  case class Alternation(loc: At, of: Seq[AliasedTypeExpression]) extends TypeExpression 

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

  case class Sequence(loc: At, of: TypeExpression) extends TypeExpression 

  case class Mapping(loc: At, from: TypeExpression, to: TypeExpression) extends TypeExpression 

  case class Set(loc: At, of: TypeExpression) extends TypeExpression 

  case class EntityReferenceTypeExpression(loc: At, entity: PathIdentifier) extends TypeExpression 

  case class Pattern(loc: At, pattern: Seq[LiteralString]) extends PredefinedType 

  case class UniqueId(loc: At, entityPath: PathIdentifier) extends PredefinedType 

  case class AggregateUseCaseTypeExpression(
    loc: At,
    usecase: AggregateUseCase,
    fields: Seq[Field] = Seq.empty[Field]
  ) extends AggregateTypeExpression 

  abstract class PredefinedType extends TypeExpression 

  object PredefinedType 

  case class Strng(loc: At, min: Option[Long] = None, max: Option[Long] = None) extends PredefinedType 

  case class Currency(loc: At, country: String) extends PredefinedType 

  case class Abstract(loc: At) extends PredefinedType 

  case class Bool(loc: At) extends PredefinedType with IntegerTypeExpression 

  case class Number(loc: At) extends PredefinedType with IntegerTypeExpression with RealTypeExpression 

  case class Integer(loc: At) extends PredefinedType with IntegerTypeExpression 

  case class Whole(loc: At) extends PredefinedType with IntegerTypeExpression 

  case class Natural(loc: At) extends PredefinedType with IntegerTypeExpression 

  case class RangeType(loc: At, min: Long, max: Long) extends IntegerTypeExpression 

  case class Decimal(loc: At, whole: Long, fractional: Long) extends RealTypeExpression 

  case class Real(loc: At) extends PredefinedType with RealTypeExpression 

  case class Current(loc: At) extends PredefinedType with RealTypeExpression 

  case class Length(loc: At) extends PredefinedType with RealTypeExpression 

  case class Luminosity(loc: At) extends PredefinedType with RealTypeExpression 

  case class Mass(loc: At) extends PredefinedType with RealTypeExpression 

  case class Mole(loc: At) extends PredefinedType with RealTypeExpression 

  case class Temperature(loc: At) extends PredefinedType with RealTypeExpression 

  sealed trait TimeType extends PredefinedType

  case class Date(loc: At) extends TimeType 

  case class Time(loc: At) extends TimeType 

  case class DateTime(loc: At) extends TimeType 

  case class TimeStamp(loc: At) extends TimeType 

  case class Duration(loc: At) extends TimeType 

  case class UUID(loc: At) extends PredefinedType 

  case class URL(loc: At, scheme: Option[LiteralString] = None) extends PredefinedType 

  case class Location(loc: At) extends PredefinedType 

  case class Nothing(loc: At) extends PredefinedType 

}
