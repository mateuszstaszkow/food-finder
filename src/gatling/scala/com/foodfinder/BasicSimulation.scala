package com.foodfinder

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class BasicSimulation extends Simulation {

  val httpConf: HttpProtocolBuilder = http.baseURL("https://localhost:8443")

  val scn: ScenarioBuilder = scenario("Basic Simulation")
    .exec(http("get personal day")
      .get("/api/users/me/days/2018-01-08")
      .header("Authorization", "Basic dXNlckBmb29kZmluZGVyLmNvbTptb2tvdG93"))
    .pause(5)
    .exec(http("search food by name")
      .get("/api/products?name=ban")
      .header("Authorization", "Basic dXNlckBmb29kZmluZGVyLmNvbTptb2tvdG93"))

  setUp(
    scn.inject(
      rampUsers(6000) over (180 seconds)
    )
  ).protocols(httpConf)

  def generateRandomString() : String = {
    Random.alphanumeric take Random.nextInt(5) mkString ""
  }

}