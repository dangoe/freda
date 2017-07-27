package de.dangoe.freda.anorm

import java.time.Instant

/**
  * @author Daniel Götten <daniel.goetten@fashionid.de>
  * @since 27.07.17
  */
case class Account(user: Option[Long], password: String, createdAt: Instant)
