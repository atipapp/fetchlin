package codes.ati.fetchlin.service

interface NotificationSender {

    fun sendSimpleText(to: String, subject: String, text: String)

}