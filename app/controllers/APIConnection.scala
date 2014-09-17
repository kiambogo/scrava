package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import java.util.concurrent.TimeUnit
import models.Athlete
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration


case class Connection(
                       accessToken: String,
                       athlete: Athlete) {

  def updateAthlete(parameters: Map): Athlete = {
    val authString = "Bearer " + accessToken
    val future = WS.url("https://www.strava.com/api/v3/athlete")
      .withHeaders("Authorization" -> authString)
      .put(parameters)
      .map { response =>
      response.json.validate[Athlete].get
    }
    Await.result(future, Duration(3, TimeUnit.SECONDS))
  }

  def findAthlete(id: Int): Option[Athlete] = {
    val future_athlete = WS.url("https://www.strava.com/api/v3/athlete/" + id)
      .get()
      .map( response =>
      response.json.validate[Athlete].asOpt
      )
    Await.result(future_athlete, Duration(3, TimeUnit.SECONDS))
  }
}
