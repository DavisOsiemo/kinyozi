package com.kinyozisms.bulk

import java.sql.DriverManager

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{FormData, HttpRequest, headers}

import scala.util.{Failure, Success}

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
