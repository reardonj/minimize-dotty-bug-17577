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

}
