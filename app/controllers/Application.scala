package controllers

import net.liftweb.json.Serialization.write
import net.liftweb.json.{NoTypeHints, Serialization}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {
  implicit val formats = Serialization.formats(NoTypeHints)

  val client = new ScravaClient("")

  def index = Action { request =>
    Ok(views.html.index())
  }

  def athlete = Action.async {
    client.findAthlete("1271201").map(athlete => Ok(write(athlete)))
  }

  def koms = Action.async {
    client.listAthleteKOMs(1271201,None,None).map(koms => Ok(write(koms)))
  }

  def friends = Action.async {
    client.listCurrentAthleteFriends(None,None).map(friends => Ok(write(friends)))
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
