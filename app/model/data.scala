package model

/**
 * An episode consists of an id, a title, a description, a timestamp,
 * and media types..
 */
case class Episode(
    id: String, 
    title: String, 
    desc: String, 
    timestamp: java.util.Date,
    attachments: Seq[MediaAttachment]) {
  def hasAudio: Boolean = attachments exists (_.isInstanceOf[Audio])
  def hasVideo: Boolean = attachments exists (_.isInstanceOf[Video])
}


/** Represents a media attachment to an episode. */
sealed trait MediaAttachment {
  def location: String
}

/** 
 *  Represents an audio attachment to an episode.
 *  TODO - More info on what can be stored.
 */
case class Audio(location: String) extends MediaAttachment

/** Represents a video attachment to an episode.
 *  
 */
case class Video(location: String) extends MediaAttachment