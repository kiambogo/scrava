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

  def timeStream = Action.async {
    client.getTimeStream("203252263").map(timeStream => Ok(write(timeStream)))
  }
}