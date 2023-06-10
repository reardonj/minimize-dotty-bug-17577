/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

trait Actions extends Definitions {

  sealed trait ApplicationAction extends Action
  sealed trait AdaptorAction extends Action
  sealed trait ContextAction extends Action
  sealed trait EntityAction extends Action
  sealed trait FunctionAction extends Action
  sealed trait SagaAction extends Action
  sealed trait AnyAction extends Action

  case class ArbitraryAction(
    loc: At,
    what: LiteralString
  ) extends AnyAction

  case class ErrorAction(loc: At, message: LiteralString) extends AnyAction

  case class AssignAction(loc: At, target: PathIdentifier, value: Expression)
      extends EntityAction

  case class AppendAction(loc: At, value: Expression, target: PathIdentifier)
      extends EntityAction

  case class MessageConstructor(
    loc: At,
    msg: MessageRef,
    args: ArgList = ArgList()
  ) extends RiddlNode

  case class ReturnAction(loc: At, value: Expression) extends FunctionAction

  case class SendAction(
    loc: At,
    msg: MessageConstructor,
    portlet: PortletRef[Portlet]
  ) extends AnyAction

  case class FunctionCallAction(
    loc: At,
    function: PathIdentifier,
    arguments: ArgList
  ) extends AnyAction

  case class MorphAction(
    loc: At,
    entity: EntityRef,
    state: StateRef,
    newValue: Expression
  ) extends EntityAction

  case class BecomeAction(loc: At, entity: EntityRef, handler: HandlerRef)
      extends EntityAction

  case class TellAction(
    loc: At,
    msg: MessageConstructor,
    entityRef: ProcessorRef[Processor[?, ?]]
  ) extends EntityAction 

  case class CompoundAction(loc: At, actions: Seq[Action]) extends AnyAction

}
