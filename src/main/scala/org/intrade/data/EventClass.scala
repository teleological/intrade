package org.intrade.data

object EventClass {

}

trait EventClass {
  def id: String

  def name: String

  def displayOrder: Int

  def eventGroups: Seq[EventGroup]
}