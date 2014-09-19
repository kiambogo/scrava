package controllers

/**
 * Created by christopher on 2014-09-15.
 */

import models.{AthleteSummary, SegmentEffort, Athlete}
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

  def listAthleteKOMs(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[SegmentEffort]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/koms")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .withHeaders("Authorization" -> authString)
      .get()
      .map { response =>
      response.json.validate[List[SegmentEffort]].fold(
        errors => throw new RuntimeException("Could not parse list of KOMs"),
        koms => koms
      )
    }
  }

  def listCurrentAthleteFriends(page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url("https://www.strava.com/api/v3/athlete/friends")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of friends (AthleteSummary)"),
        friends => friends
      )
    }
  }

  def listAthleteFriends(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/friends")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of friends (AthleteSummary)"),
        friends => friends
      )
    }
  }

  def listCurrentAthleteFollowers(page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url("https://www.strava.com/api/v3/athlete/followers")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of followers (AthleteSummary)"),
        followers => followers
      )
    }
  }

  def listAthleteFollowers(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/followers")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of followers (AthleteSummary)"),
        followers => followers
      )
    }
  }

  def listMutualFollowing(id: Int, page: Option[Int], resultsPerPage: Option[Int]): Future[List[AthleteSummary]] = {
    WS.url(s"https://www.strava.com/api/v3/athletes/$id/both-following")
      .withQueryString("page" -> page.iterator.next().toString)
      .withQueryString("per_page" -> resultsPerPage.iterator.next().toString)
      .get()
      .map { response =>
      response.json.validate[List[AthleteSummary]].fold(
        errors => throw new RuntimeException("Could not parse list of followers (AthleteSummary)"),
        followers => followers
      )
    }
  }
}
