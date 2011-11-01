package org.intrade.trading

import org.intrade.trading.Side._
import org.intrade.trading.TimeInForce._
import org.intrade.trading.OrderType._

object Requests {
  def getLogin(username: String, password: String) =
    <xmlrequest requestOp="getLogin">
      <membershipNumber>
        {username}
      </membershipNumber>
      <password>
        {password}
      </password>
    </xmlrequest>

  def getBalance = <xmlrequest requestOp="getBalance"/>

  def updateMultiOrder(orders: Seq[OrderRequest], cancelPrevious: Boolean = false, quickCancel: Boolean = false, tif: TimeInForce = TimeInForce.Good_Til_Cancel, timeToExpire: Long = 0) =
    <xmlrequest requestOp="updateMultiOrder">
      <timeInForce>
        {timeInForceToString(tif)}
      </timeInForce>{if (tif == Good_Til_Time)
      <timeToExpire>
        {timeToExpire}
      </timeToExpire>}{if (cancelPrevious) {
      if (quickCancel)
        <cancelPrevious>true</cancelPrevious> <quickCancel>true</quickCancel>
      else
        <cancelPrevious>true</cancelPrevious>
    }}{for (order <- orders) yield
        <order conID={order.conID.toString} limitprice={order.limitprice.toString()} quantity={order.quantity.toString} side={sideToString(order.side)}/>}
    </xmlrequest>

  def multiOrderRequest(orders: Seq[OrderRequest], cancelPrevious: Boolean = false, quickCancel: Boolean = false) =
    <xmlrequest requestOp="multiOrderRequest">
      {if (cancelPrevious) {
      if (quickCancel)
        <cancelPrevious>true</cancelPrevious> <quickCancel>true</quickCancel>
      else
        <cancelPrevious>true</cancelPrevious>
    }}{for (order <- orders) yield
      <order>
        {orderToOrderString(order)}
      </order>}
    </xmlrequest>

  def orderToOrderString(order: OrderRequest) =
    "conID=%s,side=%s,quantity=%s,limitPrice=%s,%s%s%s" format(
      order.conID, sideToString(order.side), order.quantity, order.limitprice, timeInForce(order), orderType(order), userReference(order.userReference))

  def sideToString(side: Side) = side match {
    case Buy => "B"
    case Sell => "S"
  }

  def userReference(ref: String) = ref match {
    case "" => ""
    case _ => ",userReference=%s" format (ref)
  }

  def timeInForce(order: OrderRequest) = order.timeInForce match {
    case Good_Til_Time => "timeInForce=GTT,timeToExpire=%s" format order.timeToExpire
    case x => "timeInForce=%s" format (timeInForceToString(order.timeInForce))
  }

  def timeInForceToString(tif: TimeInForce) = tif match {
    case Good_For_Session => "GFS"
    case Good_Til_Cancel => "GTC"
    case Good_Til_Time => "GTT"
  }

  def orderType(order: OrderRequest) = order.orderType match {
    case Touch => ",orderType=T,touchPrice=%s" format order.touchPrice
    case Fill_Or_Kill => ",orderType=F"
    case x => ""
  }

  def cancelMultipleOrdersForUser(orderIDs: Seq[Int]) =
    <xmlrequest requestOp="cancelMultipleOrdersForUser">
      {for (orderID <- orderIDs) yield
      <orderID>
        {orderID}
      </orderID>}
    </xmlrequest>

  def cancelAllInContract(contractID: Int) =
    <xmlrequest requestOp="cancelAllInContract">
      <contractID>
        {contractID}
      </contractID>
    </xmlrequest>

  def cancelAllBids(contractID: Int) =
    <xmlrequest requestOp="cancelAllBids">
      <contractID>
        {contractID}
      </contractID>
    </xmlrequest>

  def cancelAllOffers(contractID: Int) =
    <xmlrequest requestOp="cancelAllOffers">
      <contractID>
        {contractID}
      </contractID>
    </xmlrequest>

  def cancelAllInEvent(eventID: Int) =
    <xmlrequest requestOp="cancelAllInEvent">
      <eventID>
        {eventID}
      </eventID>
    </xmlrequest>

  def cancelAllOrdersForUser =
      <xmlrequest requestOp="cancelAllOrdersForUser"/>

  def getPosForUser(contractID: Int = 0) =
    <xmlrequest requestOp="getPosForUser">
      {if (contractID > 0)
      <contractID>
        {contractID}
      </contractID>}
    </xmlrequest>

  def getOpenOrders(contractID: Int = 0) =
    <xmlrequest requestOp="getOpenOrders">
      {if (contractID > 0)
      <contractID>
        {contractID}
      </contractID>}
    </xmlrequest>

  def getOrdersForUser(orderIDs: Seq[Int]) =
    <xmlrequest requestOp="getOrdersForUser">
      {for (orderID <- orderIDs) yield
      <orderID>
        {orderID}
      </orderID>}
    </xmlrequest>

  def getUserMessages(timestamp: Long = 0) =
    <xmlrequest requestOp="getUserMessages">
      {if (timestamp > 0)
      <timestamp>
        {timestamp}
      </timestamp>}
    </xmlrequest>

  def setAsRead(notificationIDs: Seq[Int]) =
    <xmlrequest requestOp="setAsRead">
      {for (notificationID <- notificationIDs) yield
      <userNotificationID>
        {notificationID}
      </userNotificationID>}
    </xmlrequest>

  def getTradesForUser =
    throw new RuntimeException("not implemented")

  def getGSXToday =
    <xmlrequest requestOp="getGSXToday">
      <checkMessages>true</checkMessages>
    </xmlrequest>
}