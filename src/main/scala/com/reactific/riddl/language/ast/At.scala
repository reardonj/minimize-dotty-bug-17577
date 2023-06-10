/*
 * Copyright 2019 Ossum, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.reactific.riddl.language.ast

import com.reactific.riddl.language.parsing.RiddlParserInput

import scala.language.implicitConversions

case class At( source: String, offset: Int = 0)

object At {
  val empty: At = At("")
  def empty(input: RiddlParserInput): At = { At("") }

}
