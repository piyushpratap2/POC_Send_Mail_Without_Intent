package com.example.ecgtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ecgtest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.Properties
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

import javax.activation.DataSource
import javax.mail.util.ByteArrayDataSource


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.btn.setOnClickListener {
            val senderEmail = "notifications@sunfox.in" // Your email address
            val senderPassword = "vimxbyplkocficgx" // Your email password
            val toEmail = "piyushpratap121@gmail.com"


            val properties = Properties()
            properties["mail.smtp.auth"] = "true"
            properties["mail.smtp.starttls.enable"] = "true"
            properties["mail.smtp.host"] = "smtp.gmail.com" // Use Gmail SMTP server
            properties["mail.smtp.port"] = "587"
            properties["mail.transport.protocol"] = "smtp"

            val elements: ArrayList<String> = ArrayList()
            elements.add("piyushpratap121@gmail.com")
            elements.add("suraj.dubey@sunfox.in")
            elements.add("ashish.yadav@sunfox.in")

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            try {

//                val message = MimeMessage(session)
//                message.setFrom(InternetAddress(senderEmail))
//                message.addRecipient(
//                    Message.RecipientType.TO,
//                    InternetAddress("piyushpratap121@gmail.com")
//                )
//                message.subject = "Hello from your app"
//                message.setText("This is a test email from my Android app.")
////
//                // Create the message body part
//                val messageBodyPart = MimeBodyPart()
//                messageBodyPart.setText("Sample body...")

//                // Create the attachment body part
//                val attachmentBodyPart = MimeBodyPart()
////
//////                attachmentBodyPart.attachFile("/normal.pdf")
////                attachmentBodyPart.attachFile(File("raw/normal.pdf"))
//                attachmentBodyPart.attachFile(File("raw/normal.pdf"))
////
////                // Create a multipart messageES
//                val multipart = MimeMultipart()
//                multipart.addBodyPart(messageBodyPart)
//                multipart.addBodyPart(attachmentBodyPart)
//
////
////                // Set the message content to the multipart
//                message.setContent(multipart)
//

                val message = MimeMessage(session)
                message.setFrom(InternetAddress(senderEmail))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("piyushpratap121@gmail.com")
                )
                message.subject = "Subject of the Email"

                // Create a Multipart to hold both the text and the attachment
                val multipart = MimeMultipart()

                // Create a MimeBodyPart for the email text content and add it to the Multipart
                val textPart = MimeBodyPart()
                textPart.setText("Hello, this is the text content of the email.")
                multipart.addBodyPart(textPart)

                // Access the file from the "raw" folder as a resource
                val resources = resources
                val resourceId = R.raw.normal
                val rawResource = resources.openRawResource(resourceId)

                // Create a MimeBodyPart for the attachment and add it to the Multipart
                val attachmentPart = MimeBodyPart()
                val attachmentFileName = "attachment.pdf"  // Replace with your desired file name
                val attachmentDataSource: DataSource = ByteArrayDataSource(
                    readBytesFromInputStream(rawResource),
                    "application/octet-stream"
                )
                attachmentPart.dataHandler = DataHandler(attachmentDataSource)
                attachmentPart.fileName = attachmentFileName
                multipart.addBodyPart(attachmentPart)

                // Set the Multipart as the content of the MimeMessage
                message.setContent(multipart)

                CoroutineScope(Dispatchers.IO)
                    .launch {
                        Transport.send(message)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w("TEST_TAG", "onCreate: ${e.message}")
            }
        }


    }

    fun readBytesFromInputStream(inputStream: InputStream): ByteArray {
        val buffer = ByteArray(8192)
        val output = ByteArrayOutputStream()
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
        return output.toByteArray()
    }
//    private fun uriToFilePath(context: Context, uri: Uri): String {
//        val cursor = context.contentResolver.query(uri, null, null, null, null)
//        cursor?.use {
//            it.moveToFirst()
//            val filePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            return it.getString(filePathColumn)
//        }
//        return ""
//    }

}