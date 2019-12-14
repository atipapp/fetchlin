package codes.ati.fetchlin.service.notification

interface NotificationSender {

    fun sendSimpleText(to: String, subject: String, text: String)

}