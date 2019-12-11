package codes.ati.fetchlin.service.notification

import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailNotificationSender(val emailSender: JavaMailSender) : NotificationSender {

    private val log = LoggerFactory.getLogger(EmailNotificationSender::class.java)

    override fun sendSimpleText(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)

        try {
            emailSender.send(message)
        } catch (e: MailException) {
            log.warn("Failed to send email. Exception occurred: ", e);
        }
    }

}