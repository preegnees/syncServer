package ru.radmir.synServer.synServer.encryption

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.security.spec.KeySpec
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.*
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@Component
class Cryptographer() {
    @Autowired
    private lateinit var startReadPasswordFromConfig: ReadPasswordFromJson
    private lateinit var password: PasswordServer

    @PostConstruct
    fun postConstruct(){
        password = startReadPasswordFromConfig.start()
    }

    fun encrypt(input: String): String {
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword())
        val cipherText: ByteArray = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder()
            .encodeToString(cipherText)
    }
    fun decrypt(cipherText: String?): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword())
        val plainText = cipher.doFinal(
            Base64.getDecoder()
                .decode(cipherText)
        )
        return String(plainText)
    }

    fun encryptFile(inputFile: File): String {
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword())
        val cipherText: ByteArray = cipher.doFinal(inputFile.readBytes())
        return Base64.getEncoder()
            .encodeToString(cipherText)
    }
    fun decryptFile(cipherFile: File): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword())
        val plainText = cipher.doFinal(
            Base64.getDecoder()
                .decode(cipherFile.readBytes())
        )
        return String(plainText)
    }

    private fun getKeyFromPassword(): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.getPassword().toCharArray(), "salt".toByteArray(), 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }
}