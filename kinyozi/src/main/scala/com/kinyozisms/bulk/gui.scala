package com.kinyozisms.bulk

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{FormData, HttpRequest, headers}
import akka.stream.ActorMaterializer

import java.sql.{Connection, DriverManager}

import scala.swing._
import scala.swing.event._

import scala.util.{Failure, Success}


class gui extends MainFrame {

  def restrictHeight (s: Component): Unit = {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }

  val la  = new Label("Look at me!")
  val nameField       = new TextField{ columns = 32 }
  val likeScala       = new CheckBox("I like Scala")
  likeScala.selected  = true
  val status1         = new RadioButton("Visitor")
  val status2         = new RadioButton("Loyal")
  val status3         = new RadioButton("High-spending")
  status3.selected    = true

  val statusGroup     = new ButtonGroup(status1, status2, status3)

  val customers       = new ComboBox(List("regular", "visitor", "loyal", "high-spending"))
  val commentField    = new TextArea{ rows = 8; lineWrap = true ; wordWrap = true }
  val nibonyeze       = new ToggleButton("TOGGLE")
  nibonyeze.selected  = true

  restrictHeight(nameField)
  restrictHeight(customers)

  contents = new BoxPanel(Orientation.Vertical) {
    contents  += new BoxPanel(Orientation.Horizontal){
      contents  += new Label("My name")
      contents  += Swing.HStrut(5)
      contents  += nameField
    }
    contents  += Swing.VStrut(5)
    contents  += likeScala
    contents  += Swing.VStrut(5)
    contents  += new BoxPanel(Orientation.Horizontal) {
      contents  += status1
      contents  += Swing.HStrut(10)
      contents  += status2
      contents  += Swing.HStrut(10)
      contents  += status3
    }
    contents  += Swing.VStrut(5)
    contents  += new BoxPanel(Orientation.Horizontal) {
      contents  +=  new Label("Customer type")
      contents  += Swing.HStrut(20)
      contents  += customers
    }
    contents  += Swing.VStrut(5)
    contents  += new Label("Write message")
    contents  += Swing.VStrut(3)
    contents  += new ScrollPane(commentField)
    contents  += Swing.VStrut(5)
    contents  += new BoxPanel(Orientation.Horizontal) {
      contents  += nibonyeze
      contents  += Swing.HGlue
      contents  += Button("SEND") {
        val sender  = new RegularSender
      }
      contents  += Swing.HGlue
      contents  += Button("Close") { reportAndClose }
    }
    for (e <- contents)
      e.xLayoutAlignment = 0.0
    border  = Swing.EmptyBorder(10, 10, 10, 10)
  }

  listenTo(status1, status2, status3)
  listenTo(customers.selection)
  reactions += {
    case SelectionChanged (`customers`) =>
      if (customers.selection.item == "loyal"){
        println(customers.selection.item)
        val loyalSelector = new LoyalSender
        // case SelectionChanged (``)
      } else if (customers.selection.item == "high-spending") {
        println(customers.selection.item)
        val highSelector  = new HighSpenderSender
      } else if (customers.selection.item == "visitor") {
        println(customers.selection.item)
        val visitorSelector = new VisitorSender
      } else if (customers.selection.item == "regular") {
        println(customers.selection.item)
        val regularSelector = new RegularSender
      } else {
        println ("Invalid selection")
      }
  }

  def reportAndClose(): Unit = {
    sys.exit(1)
  }
}




