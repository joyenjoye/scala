// https://docs.scala-lang.org/tour/pattern-matching.html
import scala.util.Random

val x: Int = Random.nextInt(10)

x match
  case 0 => "zero"
  case 1 => "one"
  case 2 => "two"
  case _ => "other"

def matchTest(x: Int): String = x match
  case 1 => "one"
  case 2 => "two"
  case _ => "other"

matchTest(3)  
matchTest(1)  

trait Notification
case class Email(sender: String, title: String, body: String) extends Notification
case class SMS(caller: String, message: String) extends Notification
case class VoiceRecording(contactName: String, link: String) extends Notification


def showNotification(notification: Notification): String =
  notification match
    case Email(sender, title, _) =>
      s"You got an email from $sender with title: $title"
    case SMS(number, message) =>
      s"You got an SMS from $number! Message: $message"
    case VoiceRecording(name, link) =>
      s"You received a Voice Recording from $name! Click the link to hear it: $link"

val someSms = SMS("12345", "Are you there?")
val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")

println(showNotification(someSms))  

println(showNotification(someVoiceRecording))


def showImportantNotification(notification: Notification, importantPeopleInfo: Seq[String]): String =
  notification match
    case Email(sender, _, _) if importantPeopleInfo.contains(sender) =>
      "You got an email from special someone!"
    case SMS(number, _) if importantPeopleInfo.contains(number) =>
      "You got an SMS from special someone!"
    case other =>
      showNotification(other) // nothing special, delegate to our original showNotification function

val importantPeopleInfo = Seq("867-5309", "jenny@gmail.com")

val importantEmail = Email("jenny@gmail.com", "Drinks tonight?", "I'm free after 5!")
val importantSms = SMS("867-5309", "I'm here! Where are you?")

println(showImportantNotification(someSms, importantPeopleInfo)) 
println(showImportantNotification(someVoiceRecording, importantPeopleInfo)) 
println(showImportantNotification(importantEmail, importantPeopleInfo)) 

println(showImportantNotification(importantSms, importantPeopleInfo)) 