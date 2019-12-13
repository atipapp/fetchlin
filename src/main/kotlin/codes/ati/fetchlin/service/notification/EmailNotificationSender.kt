package codes.ati.fetchlin.service.notification

import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailNotificationSender(val emailSender: JavaMailSender) : NotificationSender {

    private val log = LoggerFactory.getLogger(EmailNotificationSender::class.java)

    @Async
    override fun sendSimpleText(to: String, subject: String, text: String) {
        val message = SimpleMailMessage().apply {
            setTo(to)
            setSubject(subject)
            setText(text)
        }

        try {
            emailSender.send(message)
        } catch (e: MailException) {
            log.warn("Failed to send email. Exception occurred: $e");
        }
    }

}