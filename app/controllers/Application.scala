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
    client.listCurrentAthleteActivities(Some("1382676569"), None, Some("1"), None).map(activities => Ok(write(activities)))
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

  def timeStream(id: String) = Action.async {
    client.getTimeStream(id).map(timeStream => Ok(write(timeStream)))
  }

  def latlngStream(id: String) = Action.async {
    client.getLatLngStream(id).map(latlngStream => Ok(write(latlngStream)))
  }

  def altitudeStream(id: String) = Action.async {
    client.getAltitudeStream(id).map(altitudeStream => Ok(write(altitudeStream)))
  }

  def velocityStream(id: String) = Action.async {
    client.getVelocityStream(id).map(velocityStream => Ok(write(velocityStream)))
  }

  def heartrateStream(id: String) = Action.async {
    client.getHeartRateStream(id).map(heartrateStream => Ok(write(heartrateStream)))
  }

  def cadenceStream(id: String) = Action.async {
    client.getCadenceStream(id).map(cadenceStream => Ok(write(cadenceStream)))
  }

  def wattsStream(id: String) = Action.async {
    client.getWattsStream(id).map(wattsStream => Ok(write(wattsStream)))
  }

  def tempStream(id: String) = Action.async {
    client.getTempStream(id).map(wattsStream => Ok(write(wattsStream)))
  }

  def movingStream(id: String) = Action.async {
    client.getMovingStream(id).map(wattsStream => Ok(write(wattsStream)))
  }

  def gradeStream(id: String) = Action.async {
    client.getGradeStream(id).map(wattsStream => Ok(write(wattsStream)))
  }

}
