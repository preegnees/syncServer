package ru.radmir.synServer.synServer.encryption

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.init.Vars
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

    fun encryptString(input: String): String {
        val cipher: Cipher = Cipher.getInstance(Vars.cryptoMethodCipher)
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword())
        val cipherText: ByteArray = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder()
            .encodeToString(cipherText)
    }
    fun decryptString(cipherText: String /*maybe base64*/): String {
        val cipher = Cipher.getInstance(Vars.cryptoMethodCipher)
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword())
        val plainText = cipher.doFinal(
            Base64.getDecoder()
                .decode(cipherText)
        )
        return String(plainText)
    }

    fun encryptFile(inputFile: ByteArray): String {
        val cipher: Cipher = Cipher.getInstance(Vars.cryptoMethodCipher)
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword())
        val cipherText: ByteArray = cipher.doFinal(inputFile)
        return Base64.getEncoder()
            .encodeToString(cipherText)
    }
    fun decryptFile(cipherFile: String /*maybe base64*/): String /*after convert this base64 to byteArray*/ {
        val cipher = Cipher.getInstance(Vars.cryptoMethodCipher)
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword())
        return Base64.getEncoder().encodeToString(
            cipher.doFinal(Base64.getDecoder().decode(cipherFile.toByteArray()))
        )
    }

    private fun getKeyFromPassword(): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.getPassword().toCharArray(), "salt".toByteArray(), 128, 128)
        return SecretKeySpec(factory.generateSecret(spec).encoded, Vars.cryptoMethodCipher)
    }
}