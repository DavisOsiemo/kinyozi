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

trait generic_data {
  //Actor initialization
  implicit val system           = ActorSystem()
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  //API params
  val url         = "https://api.sandbox.africastalking.com/version1/messaging"
  val apiKey      = "909d5aab594b12d63f96d3d0f36b9bd2fab9de911e4f2c78e9f1b1a97521dd12"
  val username    = "sandbox"
  val from        = "13324"
  val blkmode     = "1"

  //Connect to db on port 3306
  val dburl       = "jdbc:mysql://localhost:3306/kinyozi?useSSL=false"
  val driver      = "com.mysql.jdbc.Driver"
  val dbusername  = "root"
  val dbpassword  = "root"

  var connection:Connection = null
}

class RegularSender extends generic_data {
  try {
    Class.forName(driver)
    connection      = DriverManager.getConnection(dburl, dbusername, dbpassword)
    val statement   = connection.createStatement
    val query       = statement.executeQuery(
      "SELECT `phone_number` FROM regular"
    )
    while (query.next) {
      val phone_number  = query.getString("phone_number")
      val formData      = FormData(
        "username"    -> username,
        "to"          -> phone_number,
        "from"        -> from,
        "message"     -> "REGULAR KINYOZI USERS",
        "bulkSMSMode" -> blkmode
      )
      val req = HttpRequest(
        POST,
        uri     = url,
        entity  = formData.toEntity(`UTF-8`),
        headers = List(headers.RawHeader("apikey", apiKey))
      )
      Http().singleRequest(req)
        .onComplete {
          case Success(res) => println(res)
          case Failure(_)   => sys.error("request failed")
        }
    }
  } catch {
    case e: Exception => e.printStackTrace
  }
}

class LoyalSender extends generic_data {
  //Method to insert loyal customers to a loyalties table. Data gotten from UI
  //Method to select loyal customers. Once selected, send a message to the selected numbers
  try {
    Class.forName(driver)
    connection    = DriverManager.getConnection(dburl, dbusername, dbpassword)
    val statement = connection.createStatement
    val query     = statement.executeQuery(
      "SELECT `phone_number` FROM loyal"
    )
    while (query.next) {
      val phone_number  = query.getString("phone_number")
      //println(Map(phone_number -> customer_name))
      val formData  = FormData(
          "username"    -> username,
          "to"          -> phone_number,
          "from"        -> from,
          "message"     -> "LOYALTY MESSAGE",
          "bulkSMSMode" -> blkmode
        )
      val req = HttpRequest(
          POST,
          uri     = url,
          entity  = formData.toEntity(`UTF-8`), 
          headers = List(headers.RawHeader("apiKey", apiKey))
        )
        Http().singleRequest(req)
          .onComplete {
            case Success (res)  => println(res)
            case Failure (_)    => sys.error("request failed")
          }
    }
  } catch {
    case e: Exception => e.printStackTrace
  }
}

class VisitorSender extends generic_data {
  try {
    Class.forName(driver)
    connection    = DriverManager.getConnection(dburl, dbusername, dbpassword)
    val statement = connection.createStatement
    val query     = statement.executeQuery(
      "SELECT `phone_number` FROM visitor"  
    )
    while (query.next) {
      val phone_number  = query.getString("phone_number")
      val formData      = FormData(
        "username"    -> username,
        "to"          -> phone_number,
        "from"        -> from,
        "message"     -> "VISITORS MESSAGE", 
        "bulkSMSMode" -> blkmode
      )
      val req = HttpRequest(
          POST,
          uri     = url, 
          entity  = formData.toEntity(`UTF-8`),
          headers = List(headers.RawHeader("apiKey", apiKey))
        )
        Http().singleRequest(req)
          .onComplete {
            case Success (req)  => println(req)
            case Failure (_)    => sys.error("Request failed")
          }
    }
  } catch {
    case e: Exception => e.printStackTrace
  }
}

class HighSpenderSender extends generic_data {
  try {
    Class.forName(driver)
    connection    = DriverManager.getConnection(dburl, dbusername, dbpassword)
    val statement = connection.createStatement
    val query     = statement.executeQuery(
      "SELECT `phone_number` FROM high_spending"  
    )
    while (query.next) {
      val phone_number  = query.getString("phone_number")
      val formData      = FormData(
        "username"    -> username,
        "to"          -> phone_number, 
        "from"        -> from, 
        "message"     -> "HIGH SPENDERS MESSAGE", 
        "bulkSMSMode" -> blkmode
      )
      val req = HttpRequest(
        POST,
        uri     = url,
        entity  = formData.toEntity(`UTF-8`), 
        headers = List(headers.RawHeader("apiKey", apiKey))
      )
      Http().singleRequest(req)
        .onComplete {
          case Success (req)  => println(req)
          case Failure (_)    => println("Request failed")
        }
    }
  } catch {
    case e: Exception  => e.printStackTrace
  }
} 
