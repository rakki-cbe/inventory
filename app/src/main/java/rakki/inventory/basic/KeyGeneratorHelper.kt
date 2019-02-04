package rakki.inventory.basic

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.util.Base64
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


@RequiresApi(Build.VERSION_CODES.M)
fun generateKeyEncryp(alias: String): SecretKey {
    /*** We request key generator to generate key with the following alias ****/
    val keyGenerator = KeyGenerator
        .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

    val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()

    keyGenerator.init(keyGenParameterSpec)
    return keyGenerator.generateKey()

}

@TargetApi(Build.VERSION_CODES.M)
fun encryptPass(passwor: String): EncrytedData {
    val key = generateKeyEncryp("Authendication")
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    /*** This we will use when we going to decrypt te data**/
    val iv = cipher.iv
    val ivString = Base64.encodeToString(iv, Base64.DEFAULT)

    val encryption = cipher.doFinal(passwor.toByteArray(Charsets.UTF_8))
    //decrypt(Base64.encodeToString(encryption,Base64.DEFAULT), ivString)
    return EncrytedData(ivString, Base64.encodeToString(encryption, Base64.DEFAULT))

}

@RequiresApi(Build.VERSION_CODES.M)
fun generateKeyDecryp(alias: String): SecretKey {
    /***We are requesting our keystore for the key to respective alias **/
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    val secretKeyEntry: KeyStore.SecretKeyEntry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry

    return secretKeyEntry.secretKey
}


@TargetApi(Build.VERSION_CODES.M)
fun decrypt(encryptedData: String, iv: String): String {
    val key = generateKeyDecryp("Authendication")
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val spec = GCMParameterSpec(128, Base64.decode(iv, Base64.DEFAULT))
    cipher.init(Cipher.DECRYPT_MODE, key, spec)
    val decodedData = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT))
    Log.d("Decryption", "------>" + String(decodedData, Charsets.UTF_8))
    return String(decodedData, Charsets.UTF_8)
}

class EncrytedData(val iv: String, val encryptedData: String)
