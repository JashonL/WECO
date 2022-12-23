package com.olp.weco.utils

import org.json.JSONObject

object TimeZoneUtil {

    const val countryData="{\n" +
            "\"中国\": \"8\",\n" +
            "\"Belarus\": \"3\",\n" +
            "\"Philippines\": \"8\",\n" +
            "\"SintMaarten\": \"-4\",\n" +
            "\"Morocco\": \"0\",\n" +
            "\"Kyrgyzstan\": \"6\",\n" +
            "\"Afghanistan\": \"4\",\n" +
            "\"Nicaragua\": \"-6\",\n" +
            "\"Haiti\": \"-5\",\n" +
            "\"France\": \"1\",\n" +
            "\"MalvinasIslands(Falkland)\": \"-4\",\n" +
            "\"PuertoRico\": \"-4\",\n" +
            "\"Latvia\": \"2\",\n" +
            "\"CaymanIslands\": \"-4\",\n" +
            "\"Djibouti\": \"3\",\n" +
            "\"Bangladesh\": \"6\",\n" +
            "\"Panama\": \"-5\",\n" +
            "\"Uzbekistan\": \"5\",\n" +
            "\"Burundi\": \"2\",\n" +
            "\"Guyana\": \"-4\",\n" +
            "\"Kiribati\": \"12\",\n" +
            "\"Mexico\": \"-6\",\n" +
            "\"Laos\": \"7\",\n" +
            "\"Canada\": \"-5\",\n" +
            "\"MarshallIslands\": \"12\",\n" +
            "\"Iceland\": \"0\",\n" +
            "\"PitcairnIslands\": \"-8\",\n" +
            "\"Guadeloupe\": \"-4\",\n" +
            "\"Barbados\": \"-4\",\n" +
            "\"Lebanon\": \"2\",\n" +
            "\"EastTimor\": \"9\",\n" +
            "\"Lesotho\": \"2\",\n" +
            "\"NorfolkIsland\": \"11\",\n" +
            "\"Liechtenstein\": \"1\",\n" +
            "\"TheBritishVirginIslands\": \"-4\",\n" +
            "\"CentralAfricanRepublic\": \"-7\",\n" +
            "\"SaintKittsAndNevis\": \"-4\",\n" +
            "\"Slovenia\": \"1\",\n" +
            "\"Montserrat\": \"-4\",\n" +
            "\"Macau(China)\": \"8\",\n" +
            "\"Mongolia\": \"8\",\n" +
            "\"Guatemala\": \"-6\",\n" +
            "\"BosniaAndHerzegovina\": \"1\",\n" +
            "\"Egypt\": \"2\",\n" +
            "\"Russia\": \"3\",\n" +
            "\"SriLanka\": \"5\",\n" +
            "\"Angola\": \"1\",\n" +
            "\"MIDE for SI\": \"3\",\n" +
            "\"Taiwan(China)\": \"8\",\n" +
            "\"Slovakia\": \"1\",\n" +
            "\"Greece\": \"2\",\n" +
            "\"Maldives\": \"5\",\n" +
            "\"SvalbardAndJanMayen\": \"1\",\n" +
            "\"Thailand\": \"7\",\n" +
            "\"Ukraine\": \"2\",\n" +
            "\"NewZealand\": \"12\",\n" +
            "\"Italy\": \"1\",\n" +
            "\"Iraq\": \"3\",\n" +
            "\"Tonga\": \"13\",\n" +
            "\"Moldova\": \"2\",\n" +
            "\"PapuaNewGuinea\": \"10\",\n" +
            "\"Guinea\": \"0\",\n" +
            "\"Cyprus\": \"2\",\n" +
            "\"FederatedStatesOfMicronesia\": \"11\",\n" +
            "\"HongKong(China)\": \"8\",\n" +
            "\"Denmark\": \"1\",\n" +
            "\"Kuwait\": \"3\",\n" +
            "\"SaintPierreAndMiquelon\": \"-4\",\n" +
            "\"WallisAndFutuna\": \"12\",\n" +
            "\"Ghana\": \"0\",\n" +
            "\"Pakistan\": \"5\",\n" +
            "\"SouthKorea\": \"9\",\n" +
            "\"BurkinaFaso\": \"0\",\n" +
            "\"CookIslands\": \"10\",\n" +
            "\"Gabon\": \"1\",\n" +
            "\"Montenegro\": \"1\",\n" +
            "\"CongoDemocraticRepublic\": \"2\",\n" +
            "\"Armenia\": \"4\",\n" +
            "\"EquatorialGuinea\": \"1\",\n" +
            "\"Croatia\": \"1\",\n" +
            "\"Cameroon\": \"1\",\n" +
            "\"Sweden\": \"1\",\n" +
            "\"Zimbabwe\": \"2\",\n" +
            "\"Serbia\": \"1\",\n" +
            "\"Nigeria\": \"1\",\n" +
            "\"Estonia\": \"2\",\n" +
            "\"Martinique\": \"-4\",\n" +
            "\"Bolivia\": \"-4\",\n" +
            "\"Liberia\": \"0\",\n" +
            "\"Turkmenistan\": \"5\",\n" +
            "\"Kenya\": \"3\",\n" +
            "\"TurksAndCaicosIslands\": \"-5\",\n" +
            "\"FrenchPolynesia\": \"-10\",\n" +
            "\"Peru\": \"-5\",\n" +
            "\"Malawi\": \"2\",\n" +
            "\"Dominica\": \"-4\",\n" +
            "\"Tunisia\": \"1\",\n" +
            "\"China\": \"8\",\n" +
            "\"Gambia\": \"0\",\n" +
            "\"Uganda\": \"3\",\n" +
            "\"Togo\": \"0\",\n" +
            "\"Seychelles\": \"4\",\n" +
            "\"Sudan\": \"2\",\n" +
            "\"Malta\": \"1\",\n" +
            "\"Bahamas\": \"-5\",\n" +
            "\"Cambodia\": \"7\",\n" +
            "\"Zambia\": \"2\",\n" +
            "\"SaudiArabia\": \"3\",\n" +
            "\"Belgium\": \"1\",\n" +
            "\"Anguilla\": \"-4\",\n" +
            "\"SolomonIslands\": \"11\",\n" +
            "\"DominicanRepublic\": \"-4\",\n" +
            "\"RepublicOfTheCongo\": \"1\",\n" +
            "\"Portugal\": \"0\",\n" +
            "\"Tanzania\": \"3\",\n" +
            "\"Mali\": \"0\",\n" +
            "\"Ecuador\": \"-5\",\n" +
            "\"Indonesia\": \"7\",\n" +
            "\"SaintLucia\": \"-4\",\n" +
            "\"SaintVincentAndtheGrenadines\": \"-4\",\n" +
            "\"Luxembourg\": \"1\",\n" +
            "\"Chile\": \"-3\",\n" +
            "\"SouthAfrica\": \"2\",\n" +
            "\"SanMarino\": \"1\",\n" +
            "\"Bahrain\": \"3\",\n" +
            "\"Fiji\": \"12\",\n" +
            "\"Niue\": \"-11\",\n" +
            "\"Palestine\": \"2\",\n" +
            "\"Samoa\": \"-11\",\n" +
            "\"Oman\": \"4\",\n" +
            "\"Turkey\": \"2\",\n" +
            "\"NewCaledonia\": \"11\",\n" +
            "\"Niger\": \"1\",\n" +
            "\"CzechRepublic\": \"2\",\n" +
            "\"Guam\": \"10\",\n" +
            "\"Chad\": \"1\",\n" +
            "\"Mozambique\": \"2\",\n" +
            "\"Benin\": \"1\",\n" +
            "\"Romania\": \"2\",\n" +
            "\"TrinidadAndTobago\": \"-4\",\n" +
            "\"FrenchGuiana\": \"-3\",\n" +
            "\"Algeria\": \"1\",\n" +
            "\"Madagascar\": \"3\",\n" +
            "\"Comoros\": \"3\",\n" +
            "\"Belize\": \"-6\",\n" +
            "\"Paraguay\": \"-4\",\n" +
            "\"Syria\": \"2\",\n" +
            "\"Ireland\": \"0\",\n" +
            "\"Columbia\": \"-5\",\n" +
            "\"Switzerland\": \"1\",\n" +
            "\"SouthSudan\": \"3\",\n" +
            "\"Yemen\": \"3\",\n" +
            "\"Vanuatu\": \"11\",\n" +
            "\"Malaysia\": \"8\",\n" +
            "\"Aruba\": \"-4\",\n" +
            "\"Albania\": \"1\",\n" +
            "\"SierraLeone\": \"0\",\n" +
            "\"SaintHelena\": \"0\",\n" +
            "\"Austria\": \"1\",\n" +
            "\"AmericanSamoa\": \"-11\",\n" +
            "\"Monaco\": \"1\",\n" +
            "\"Mauritania\": \"0\",\n" +
            "\"UnitedStates\": \"-8\",\n" +
            "\"Bermuda\": \"-4\",\n" +
            "\"Hungary\": \"1\",\n" +
            "\"Mauritius\": \"4\",\n" +
            "\"Argentina\": \"-3\",\n" +
            "\"Poland\": \"1\",\n" +
            "\"Tokelau\": \"-10\",\n" +
            "\"Georgia\": \"4\",\n" +
            "\"Bulgaria\": \"2\",\n" +
            "\"Germany\": \"1\",\n" +
            "\"Norway\": \"1\",\n" +
            "\"Japan\": \"9\",\n" +
            "\"UnitedArabEmirates\": \"4\",\n" +
            "\"Cuba\": \"-5\",\n" +
            "\"Nauru\": \"12\",\n" +
            "\"CostaRica\": \"-6\",\n" +
            "\"Tajikistan\": \"5\",\n" +
            "\"India\": \"5\",\n" +
            "\"Greenland\": \"-3\",\n" +
            "\"Macedonia\": \"1\",\n" +
            "\"Jordan\": \"2\",\n" +
            "\"Senegal\": \"0\",\n" +
            "\"Eritrea\": \"3\",\n" +
            "\"Namibia\": \"1\",\n" +
            "\"Myanmar\": \"6\",\n" +
            "\"Uruguay\": \"-3\",\n" +
            "\"Libya\": \"2\",\n" +
            "\"Andorra\": \"1\",\n" +
            "\"Rwanda\": \"2\",\n" +
            "\"FaroeIslands\": \"0\",\n" +
            "\"Swaziland\": \"2\",\n" +
            "\"NorthKorea\": \"9\",\n" +
            "\"Brazil\": \"-3\",\n" +
            "\"Venezuela\": \"-4\",\n" +
            "\"Qatar\": \"3\",\n" +
            "\"Tuvalu\": \"12\",\n" +
            "\"Salvador\": \"-6\",\n" +
            "\"Spain\": \"1\",\n" +
            "\"Palau\": \"9\",\n" +
            "\"Israel\": \"2\",\n" +
            "\"Bhutan\": \"6\",\n" +
            "\"BruneiDarussalam\": \"8\",\n" +
            "\"Nepal\": \"5\",\n" +
            "\"Azerbaijan\": \"4\",\n" +
            "\"Vietnam\": \"7\",\n" +
            "\"Ethiopia\": \"3\",\n" +
            "\"Honduras\": \"-6\",\n" +
            "\"Australia\": \"10\",\n" +
            "\"NorthernMarianaIslands\": \"10\",\n" +
            "\"Netherlands\": \"1\",\n" +
            "\"Gibraltar\": \"1\",\n" +
            "\"Somalia\": \"3\",\n" +
            "\"CapeVerde\": \"-1\",\n" +
            "\"AntiguaAndBarbuda\": \"-3\",\n" +
            "\"UnitedStatesVirginIslands\": \"-4\",\n" +
            "\"IsleOfMan\": \"0\",\n" +
            "\"Finland\": \"2\",\n" +
            "\"Lithuania\": \"1\",\n" +
            "\"Suriname\": \"-3\",\n" +
            "\"Jamaica\": \"-5\",\n" +
            "\"UnitedKingdom\": \"0\",\n" +
            "\"Grenada\": \"-4\",\n" +
            "\"Singapore\": \"8\",\n" +
            "\"Botswana\": \"2\",\n" +
            "\"Kazakhstan\": \"6\",\n" +
            "\"GuineaBissau\": \"0\",\n" +
            "\"IvoryCoast\": \"0\"\n" +
            "}"


    fun getTimeZoneByCountry(country:String):String{
        val jsonObject = JSONObject(countryData)
        return jsonObject.optString(country, "8")
    }

}