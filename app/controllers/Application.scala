package controllers

import net.liftweb.json.Serialization.write
import net.liftweb.json.{NoTypeHints, Serialization}
import org.joda.time.DateTime
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {
  implicit val formats = Serialization.formats(NoTypeHints)

  val client = new ScravaClient("")

  def index = Action { request =>
    Ok(views.html.index())
  }

  def createActivity() = Action.async {
    client.createActivity("testActivity", "ride", DateTime.now(), 9000, Some("wow, such descrption"), Some(Float.box(90)))
      .map(activity => Ok(write(activity)))
  }

  def activity(id: String) = Action.async {
    client.retrieveActivity(id.toLong, None).map(activity => Ok(write(activity.merge)))
  }

  def myActivities= Action.async {
    client.listCurrentAthleteActivities(None, None, None, None).map(activities => Ok(write(activities)))
  }

  def friendsActivities= Action.async {
    client.listFriendsActivities(None, None).map(activities => Ok(write(activities)))
  }

  def athlete = Action.async {
    client.retreiveCurrentAthlete().map(athlete => Ok(write(athlete)))
  }

  def koms = Action.async {
    client.listAthleteKOMs(1271201,None,None).map(koms => Ok(write(koms)))
  }

  def currentFriends = Action.async {
    client.listCurrentAthleteFriends(None,None).map(friends => Ok(write(friends)))
  }

  def friends(id: String) = Action.async {
    client.listAthleteFriends(id, None, None).map(friends => Ok(write(friends)))
  }

  def activityStreams = Action.async {
    client.retrieveActivityStream("206688393", Some("time,latlng")).map(timeStream => Ok(write(timeStream)))
  }

  def segmentStreams = Action.async {
    client.retrieveSegmentStream("1089606", Some("time,latlng")).map(timeStream => Ok(write(timeStream)))
  }

  def effortStreams = Action.async {
    client.retrieveEffortStream("206688393", Some("time,latlng")).map(timeStream => Ok(write(timeStream)))
  }
}
