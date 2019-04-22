package com.kinyozisms.bulk

import java.sql.Connection

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

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
