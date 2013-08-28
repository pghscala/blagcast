package controllers

import play.api.mvc._
import play.api.libs.ws.{WS, Response}
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import Global._

object RestController extends Controller {
  
  
  def videos = Action(Async {
    for(videos <- client.Youtube.grabUserFeed(youtubeUser))
     yield {
      Ok(Json.toJson(videos))
    }
  })
  def audios = Action(Async {
    for(audios <- client.Libsyn.readFeed(audioFeed))
     yield {
      Ok(Json.toJson(audios))
    }
  })
}