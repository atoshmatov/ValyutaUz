package uz.toshmatov.currency.core.extensions

import androidx.annotation.DrawableRes
import uz.toshmatov.currency.core.utils.drawable

enum class BankCode(
    @DrawableRes val logo: Int
) {
    UNIVERSAL_BANK(drawable.universalbank),
    ALOQABANK(drawable.aloqabank),
    HAMKORBANK(drawable.hamkorbank),
    XALQ_BANKI(drawable.xalqbank),
    MK_BANK(drawable.mkbank),
    ASIA_ALLIANCE(drawable.asiaalliancebank),
    BRB(drawable.brb),
    MADADINVEST(drawable.madainvest),
    IPOTEKA_BANK(drawable.ipotekabank),
    INFINBANK(drawable.infinbank),
    DAVRBANK(drawable.davrbank),
    NBU(drawable.nbu),
    AGROBANK(drawable.agrobank),
    TURONBANK(drawable.turonbank),
    TRASTBANK(drawable.transtbank),
    KAPITALBANK(drawable.kapitalbank),
    ANORBANK(drawable.anorbank),
    ASAKABANK(drawable.asakabank),
    TENGEBANK(drawable.tengebank),
    ZIRAAT_BANK(drawable.ziraatbank),
    KDB_BANK(drawable.kdbbank),
    OCTOBANK(drawable.octobank),
    OFB(drawable.ofb),
    SADERATBANK(drawable.saderatbank),
    GARANTBANK(drawable.garantbank),
    HAYOT_BANK(drawable.hayatbank),
    SQB(drawable.sqb),
    IPAK_YULI_BANK(drawable.ipakyulibank),
    POYTAXT_BANK(drawable.poytaxtbank)
}

fun getBankLogo(bank: String): Int {
    return when (bank.uppercase().replace(" ", "_")) {
        "UNIVERSALBANK" -> BankCode.UNIVERSAL_BANK.logo
        "ALOQABANK" -> BankCode.ALOQABANK.logo
        "HAMKORBANK" -> BankCode.HAMKORBANK.logo
        "XALQ_BANKI" -> BankCode.XALQ_BANKI.logo
        "MKBANK" -> BankCode.MK_BANK.logo
        "ASIA_ALLIANCE" -> BankCode.ASIA_ALLIANCE.logo
        "BRB" -> BankCode.BRB.logo
        "MADADINVEST" -> BankCode.MADADINVEST.logo
        "IPOTEKA_BANK" -> BankCode.IPOTEKA_BANK.logo
        "INFINBANK" -> BankCode.INFINBANK.logo
        "DAVRBANK" -> BankCode.DAVRBANK.logo
        "NBU" -> BankCode.NBU.logo
        "AGROBANK" -> BankCode.AGROBANK.logo
        "TURONBANK" -> BankCode.TURONBANK.logo
        "TRASTBANK" -> BankCode.TRASTBANK.logo
        "KAPITALBANK" -> BankCode.KAPITALBANK.logo
        "ANORBANK" -> BankCode.ANORBANK.logo
        "ASAKABANK" -> BankCode.ASAKABANK.logo
        "TENGEBANK" -> BankCode.TENGEBANK.logo
        "ZIRAAT_BANK" -> BankCode.ZIRAAT_BANK.logo
        "KDB_BANK" -> BankCode.KDB_BANK.logo
        "OCTOBANK" -> BankCode.OCTOBANK.logo
        "OFB" -> BankCode.OFB.logo
        "SADERATBANK" -> BankCode.SADERATBANK.logo
        "GARANTBANK" -> BankCode.GARANTBANK.logo
        "HAYOT_BANK" -> BankCode.HAYOT_BANK.logo
        "SQB" -> BankCode.SQB.logo
        "IPAK_YULI_BANK" -> BankCode.IPAK_YULI_BANK.logo
        "POYTAXT_BANK" -> BankCode.POYTAXT_BANK.logo
        else -> {
            drawable.ic_empty_flag
        }
    }
}

fun main() {
    println(getBankLogo("Universal Bank"))
    println(getBankLogo("Aloqa Bank"))
    println(getBankLogo("Hamkor Bank"))
    println(getBankLogo("Xalq Banki"))
    println(getBankLogo("MK Bank"))
    println(getBankLogo("Asia Alliance"))
    println(getBankLogo("BRB"))
    println(getBankLogo("MadadInvest"))
    println(getBankLogo("Ipoteka Bank"))
    println(getBankLogo("InfinBank"))
    println(getBankLogo("Davrbank"))
    println(getBankLogo("NBU"))
    println(getBankLogo("Agrobank"))
    println(getBankLogo("Turonbank"))
    println(getBankLogo("Trastbank"))
    println(getBankLogo("Kapitalbank"))
    println(getBankLogo("Anorbank"))
    println(getBankLogo("Asakabank"))
    println(getBankLogo("Tengebank"))
    println(getBankLogo("Ziraat Bank"))
    println(getBankLogo("KDB Bank"))
    println(getBankLogo("Octobank"))
    println(getBankLogo("OFB"))
    println(getBankLogo("Saderatbank"))
    println(getBankLogo("Garantbank"))
    println(getBankLogo("Hayot Bank"))
    println(getBankLogo("SQB"))
    println(getBankLogo("Ipak Yuli Bank"))
    println(getBankLogo("Poytaxt Bank"))
    println(getBankLogo("Unknown Bank"))
}