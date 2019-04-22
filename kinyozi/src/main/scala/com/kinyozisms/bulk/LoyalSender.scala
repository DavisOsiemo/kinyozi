package com.kinyozisms.bulk

import java.sql.DriverManager

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{FormData, HttpRequest, headers}

import scala.util.{Failure, Success}

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

