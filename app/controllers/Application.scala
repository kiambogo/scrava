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

  def activity(id: Int) = Action.async {
    client.retrieveActivity(id).map(activity => Ok(write(activity.merge)))
  }

  def activityComments(id: Int) = Action.async {
    client.listActivityComments(id).map(comments => Ok(write(comments)))
  }

  def myActivities= Action.async {
    client.listAthleteActivities().map(activities => Ok(write(activities)))
  }

  def friendsActivities= Action.async {
    client.listFriendsActivities().map(activities => Ok(write(activities)))
  }

  def athlete = Action.async {
    client.retrieveAthlete(Some(5250729)).map(athlete => Ok(write(athlete.merge)))
  }

  def koms = Action.async {
    client.listAthleteKOMs(Some(5250729)).map(koms => Ok(write(koms)))
  }

  def friends() = Action.async {
    client.listAthleteFriends().map(friends => Ok(write(friends)))
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
