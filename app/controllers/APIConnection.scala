package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import models.{SegmentEffort, Athlete}
import play.api.libs.ws.WS

import scala.concurrent.Future


case class Connection(
                       accessToken: String,
                       athlete: Athlete) {

  val authString = "Bearer " + accessToken

  def updateAthlete(parameters: Map): Future[Athlete] = {
    WS.url("https://www.strava.com/api/v3/athlete")
      .withHeaders("Authorization" -> authString)
      .put(parameters)
      .map { response =>
      response.json.validate[Athlete].get
    }
  }

  def findAthlete(id: Int): Future[Option[Athlete]] = {
    WS.url(s"https://www.strava.com/api/v3/athlete/$id")
      .get()
      .map { response =>
      response.json.validate[Athlete].asOpt
    }
  }

  def listAthleteKOMs(id: Int, page: Int, resultsPerPage: Int): Future[List[SegmentEffort]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/koms")
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      response.json.validate[List[SegmentEffort]].fold(
        errors => throw new RuntimeException("Could not parse list of KOMs"),
        koms => koms
      )
    }
  }
}
