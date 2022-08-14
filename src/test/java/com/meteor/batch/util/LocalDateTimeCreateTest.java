package com.meteor.batch.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalDateTimeCreateTest {

    @Test
    void now() {
        final LocalDateTime systemDefaultLocalDateTime = LocalDateTime.now();
//        System.out.println("systemDefaultLocalDateTime : " + systemDefaultLocalDateTime);
        //2022-08-14T21:00:18.553471

        final LocalDateTime utcClockLocalDateTime = LocalDateTime.now(Clock.systemUTC());
//        System.out.println("utcClockLocalDateTime : " + utcClockLocalDateTime);
        //2022-08-14T12:00:18.553647

        ZoneId.getAvailableZoneIds()
              .forEach(zone -> {
//                  System.out.println("zone : " + zone);
//
//                  zone : Asia/Aden
//                  zone : America/Cuiaba
//                  zone : Etc/GMT+9
//                  zone : Etc/GMT+8
//                  zone : Africa/Nairobi
//                  zone : America/Marigot
//                  zone : Asia/Aqtau
//                  zone : Pacific/Kwajalein
//                  zone : America/El_Salvador
//                  zone : Asia/Pontianak
//                  zone : Africa/Cairo
//                  zone : Pacific/Pago_Pago
//                  zone : Africa/Mbabane
//                  zone : Asia/Kuching
//                  zone : Pacific/Honolulu
//                  zone : Pacific/Rarotonga
//                  zone : America/Guatemala
//                  zone : Australia/Hobart
//                  zone : Europe/London
//                  zone : America/Belize
//                  zone : America/Panama
//                  zone : Asia/Chungking
//                  zone : America/Managua
//                  zone : America/Indiana/Petersburg
//                  zone : Asia/Yerevan
//                  zone : Europe/Brussels
//                  zone : GMT
//                  zone : Europe/Warsaw
//                  zone : America/Chicago
//                  zone : Asia/Kashgar
//                  zone : Chile/Continental
//                  zone : Pacific/Yap
//                  zone : CET
//                  zone : Etc/GMT-1
//                  zone : Etc/GMT-0
//                  zone : Europe/Jersey
//                  zone : America/Tegucigalpa
//                  zone : Etc/GMT-5
//                  zone : Europe/Istanbul
//                  zone : America/Eirunepe
//                  zone : Etc/GMT-4
//                  zone : America/Miquelon
//                  zone : Etc/GMT-3
//                  zone : Europe/Luxembourg
//                  zone : Etc/GMT-2
//                  zone : Etc/GMT-9
//                  zone : America/Argentina/Catamarca
//                  zone : Etc/GMT-8
//                  zone : Etc/GMT-7
//                  zone : Etc/GMT-6
//                  zone : Europe/Zaporozhye
//                  zone : Canada/Yukon
//                  zone : Canada/Atlantic
//                  zone : Atlantic/St_Helena
//                  zone : Australia/Tasmania
//                  zone : Libya
//                  zone : Europe/Guernsey
//                  zone : America/Grand_Turk
//                  zone : Asia/Samarkand
//                  zone : America/Argentina/Cordoba
//                  zone : Asia/Phnom_Penh
//                  zone : Africa/Kigali
//                  zone : Asia/Almaty
//                  zone : US/Alaska
//                  zone : Asia/Dubai
//                  zone : Europe/Isle_of_Man
//                  zone : America/Araguaina
//                  zone : Cuba
//                  zone : Asia/Novosibirsk
//                  zone : America/Argentina/Salta
//                  zone : Etc/GMT+3
//                  zone : Africa/Tunis
//                  zone : Etc/GMT+2
//                  zone : Etc/GMT+1
//                  zone : Pacific/Fakaofo
//                  zone : Africa/Tripoli
//                  zone : Etc/GMT+0
//                  zone : Israel
//                  zone : Africa/Banjul
//                  zone : Etc/GMT+7
//                  zone : Indian/Comoro
//                  zone : Etc/GMT+6
//                  zone : Etc/GMT+5
//                  zone : Etc/GMT+4
//                  zone : Pacific/Port_Moresby
//                  zone : US/Arizona
//                  zone : Antarctica/Syowa
//                  zone : Indian/Reunion
//                  zone : Pacific/Palau
//                  zone : Europe/Kaliningrad
//                  zone : America/Montevideo
//                  zone : Africa/Windhoek
//                  zone : Asia/Karachi
//                  zone : Africa/Mogadishu
//                  zone : Australia/Perth
//                  zone : Brazil/East
//                  zone : Etc/GMT
//                  zone : Asia/Chita
//                  zone : Pacific/Easter
//                  zone : Antarctica/Davis
//                  zone : Antarctica/McMurdo
//                  zone : Asia/Macao
//                  zone : America/Manaus
//                  zone : Africa/Freetown
//                  zone : Europe/Bucharest
//                  zone : Asia/Tomsk
//                  zone : America/Argentina/Mendoza
//                  zone : Asia/Macau
//                  zone : Europe/Malta
//                  zone : Mexico/BajaSur
//                  zone : Pacific/Tahiti
//                  zone : Africa/Asmera
//                  zone : Europe/Busingen
//                  zone : America/Argentina/Rio_Gallegos
//                  zone : Africa/Malabo
//                  zone : Europe/Skopje
//                  zone : America/Catamarca
//                  zone : America/Godthab
//                  zone : Europe/Sarajevo
//                  zone : Australia/ACT
//                  zone : GB-Eire
//                  zone : Africa/Lagos
//                  zone : America/Cordoba
//                  zone : Europe/Rome
//                  zone : Asia/Dacca
//                  zone : Indian/Mauritius
//                  zone : Pacific/Samoa
//                  zone : America/Regina
//                  zone : America/Fort_Wayne
//                  zone : America/Dawson_Creek
//                  zone : Africa/Algiers
//                  zone : Europe/Mariehamn
//                  zone : America/St_Johns
//                  zone : America/St_Thomas
//                  zone : Europe/Zurich
//                  zone : America/Anguilla
//                  zone : Asia/Dili
//                  zone : America/Denver
//                  zone : Africa/Bamako
//                  zone : Europe/Saratov
//                  zone : GB
//                  zone : Mexico/General
//                  zone : Pacific/Wallis
//                  zone : Europe/Gibraltar
//                  zone : Africa/Conakry
//                  zone : Africa/Lubumbashi
//                  zone : Asia/Istanbul
//                  zone : America/Havana
//                  zone : NZ-CHAT
//                  zone : Asia/Choibalsan
//                  zone : America/Porto_Acre
//                  zone : Asia/Omsk
//                  zone : Europe/Vaduz
//                  zone : US/Michigan
//                  zone : Asia/Dhaka
//                  zone : America/Barbados
//                  zone : Europe/Tiraspol
//                  zone : Atlantic/Cape_Verde
//                  zone : Asia/Yekaterinburg
//                  zone : America/Louisville
//                  zone : Pacific/Johnston
//                  zone : Pacific/Chatham
//                  zone : Europe/Ljubljana
//                  zone : America/Sao_Paulo
//                  zone : Asia/Jayapura
//                  zone : America/Curacao
//                  zone : Asia/Dushanbe
//                  zone : America/Guyana
//                  zone : America/Guayaquil
//                  zone : America/Martinique
//                  zone : Portugal
//                  zone : Europe/Berlin
//                  zone : Europe/Moscow
//                  zone : Europe/Chisinau
//                  zone : America/Puerto_Rico
//                  zone : America/Rankin_Inlet
//                  zone : Pacific/Ponape
//                  zone : Europe/Stockholm
//                  zone : Europe/Budapest
//                  zone : America/Argentina/Jujuy
//                  zone : Australia/Eucla
//                  zone : Asia/Shanghai
//                  zone : Universal
//                  zone : Europe/Zagreb
//                  zone : America/Port_of_Spain
//                  zone : Europe/Helsinki
//                  zone : Asia/Beirut
//                  zone : Asia/Tel_Aviv
//                  zone : Pacific/Bougainville
//                  zone : US/Central
//                  zone : Africa/Sao_Tome
//                  zone : Indian/Chagos
//                  zone : America/Cayenne
//                  zone : Asia/Yakutsk
//                  zone : Pacific/Galapagos
//                  zone : Australia/North
//                  zone : Europe/Paris
//                  zone : Africa/Ndjamena
//                  zone : Pacific/Fiji
//                  zone : America/Rainy_River
//                  zone : Indian/Maldives
//                  zone : Australia/Yancowinna
//                  zone : SystemV/AST4
//                  zone : Asia/Oral
//                  zone : America/Yellowknife
//                  zone : Pacific/Enderbury
//                  zone : America/Juneau
//                  zone : Australia/Victoria
//                  zone : America/Indiana/Vevay
//                  zone : Asia/Tashkent
//                  zone : Asia/Jakarta
//                  zone : Africa/Ceuta
//                  zone : Asia/Barnaul
//                  zone : America/Recife
//                  zone : America/Buenos_Aires
//                  zone : America/Noronha
//                  zone : America/Swift_Current
//                  zone : Australia/Adelaide
//                  zone : America/Metlakatla
//                  zone : Africa/Djibouti
//                  zone : America/Paramaribo
//                  zone : Asia/Qostanay
//                  zone : Europe/Simferopol
//                  zone : Europe/Sofia
//                  zone : Africa/Nouakchott
//                  zone : Europe/Prague
//                  zone : America/Indiana/Vincennes
//                  zone : Antarctica/Mawson
//                  zone : America/Kralendijk
//                  zone : Antarctica/Troll
//                  zone : Europe/Samara
//                  zone : Indian/Christmas
//                  zone : America/Antigua
//                  zone : Pacific/Gambier
//                  zone : America/Indianapolis
//                  zone : America/Inuvik
//                  zone : America/Iqaluit
//                  zone : Pacific/Funafuti
//                  zone : UTC
//                  zone : Antarctica/Macquarie
//                  zone : Canada/Pacific
//                  zone : America/Moncton
//                  zone : Africa/Gaborone
//                  zone : Pacific/Chuuk
//                  zone : Asia/Pyongyang
//                  zone : America/St_Vincent
//                  zone : Asia/Gaza
//                  zone : Etc/Universal
//                  zone : PST8PDT
//                  zone : Atlantic/Faeroe
//                  zone : Asia/Qyzylorda
//                  zone : Canada/Newfoundland
//                  zone : America/Kentucky/Louisville
//                  zone : America/Yakutat
//                  zone : Asia/Ho_Chi_Minh
//                  zone : Antarctica/Casey
//                  zone : Europe/Copenhagen
//                  zone : Africa/Asmara
//                  zone : Atlantic/Azores
//                  zone : Europe/Vienna
//                  zone : ROK
//                  zone : Pacific/Pitcairn
//                  zone : America/Mazatlan
//                  zone : Australia/Queensland
//                  zone : Pacific/Nauru
//                  zone : Europe/Tirane
//                  zone : Asia/Kolkata
//                  zone : SystemV/MST7
//                  zone : Australia/Canberra
//                  zone : MET
//                  zone : Australia/Broken_Hill
//                  zone : Europe/Riga
//                  zone : America/Dominica
//                  zone : Africa/Abidjan
//                  zone : America/Mendoza
//                  zone : America/Santarem
//                  zone : Kwajalein
//                  zone : America/Asuncion
//                  zone : Asia/Ulan_Bator
//                  zone : NZ
//                  zone : America/Boise
//                  zone : Australia/Currie
//                  zone : EST5EDT
//                  zone : Pacific/Guam
//                  zone : Pacific/Wake
//                  zone : Atlantic/Bermuda
//                  zone : America/Costa_Rica
//                  zone : America/Dawson
//                  zone : Asia/Chongqing
//                  zone : Eire
//                  zone : Europe/Amsterdam
//                  zone : America/Indiana/Knox
//                  zone : America/North_Dakota/Beulah
//                  zone : Africa/Accra
//                  zone : Atlantic/Faroe
//                  zone : Mexico/BajaNorte
//                  zone : America/Maceio
//                  zone : Etc/UCT
//                  zone : Pacific/Apia
//                  zone : GMT0
//                  zone : America/Atka
//                  zone : Pacific/Niue
//                  zone : Australia/Lord_Howe
//                  zone : Europe/Dublin
//                  zone : Pacific/Truk
//                  zone : MST7MDT
//                  zone : America/Monterrey
//                  zone : America/Nassau
//                  zone : America/Jamaica
//                  zone : Asia/Bishkek
//                  zone : America/Atikokan
//                  zone : Atlantic/Stanley
//                  zone : Australia/NSW
//                  zone : US/Hawaii
//                  zone : SystemV/CST6
//                  zone : Indian/Mahe
//                  zone : Asia/Aqtobe
//                  zone : America/Sitka
//                  zone : Asia/Vladivostok
//                  zone : Africa/Libreville
//                  zone : Africa/Maputo
//                  zone : Zulu
//                  zone : America/Kentucky/Monticello
//                  zone : Africa/El_Aaiun
//                  zone : Africa/Ouagadougou
//                  zone : America/Coral_Harbour
//                  zone : Pacific/Marquesas
//                  zone : Brazil/West
//                  zone : America/Aruba
//                  zone : America/North_Dakota/Center
//                  zone : America/Cayman
//                  zone : Asia/Ulaanbaatar
//                  zone : Asia/Baghdad
//                  zone : Europe/San_Marino
//                  zone : America/Indiana/Tell_City
//                  zone : America/Tijuana
//                  zone : Pacific/Saipan
//                  zone : SystemV/YST9
//                  zone : Africa/Douala
//                  zone : America/Chihuahua
//                  zone : America/Ojinaga
//                  zone : Asia/Hovd
//                  zone : America/Anchorage
//                  zone : Chile/EasterIsland
//                  zone : America/Halifax
//                  zone : Antarctica/Rothera
//                  zone : America/Indiana/Indianapolis
//                  zone : US/Mountain
//                  zone : Asia/Damascus
//                  zone : America/Argentina/San_Luis
//                  zone : America/Santiago
//                  zone : Asia/Baku
//                  zone : America/Argentina/Ushuaia
//                  zone : Atlantic/Reykjavik
//                  zone : Africa/Brazzaville
//                  zone : Africa/Porto-Novo
//                  zone : America/La_Paz
//                  zone : Antarctica/DumontDUrville
//                  zone : Asia/Taipei
//                  zone : Antarctica/South_Pole
//                  zone : Asia/Manila
//                  zone : Asia/Bangkok
//                  zone : Africa/Dar_es_Salaam
//                  zone : Poland
//                  zone : Atlantic/Madeira
//                  zone : Antarctica/Palmer
//                  zone : America/Thunder_Bay
//                  zone : Africa/Addis_Ababa
//                  zone : Asia/Yangon
//                  zone : Europe/Uzhgorod
//                  zone : Brazil/DeNoronha
//                  zone : Asia/Ashkhabad
//                  zone : Etc/Zulu
//                  zone : America/Indiana/Marengo
//                  zone : America/Creston
//                  zone : America/Punta_Arenas
//                  zone : America/Mexico_City
//                  zone : Antarctica/Vostok
//                  zone : Asia/Jerusalem
//                  zone : Europe/Andorra
//                  zone : US/Samoa
//                  zone : PRC
//                  zone : Asia/Vientiane
//                  zone : Pacific/Kiritimati
//                  zone : America/Matamoros
//                  zone : America/Blanc-Sablon
//                  zone : Asia/Riyadh
//                  zone : Iceland
//                  zone : Pacific/Pohnpei
//                  zone : Asia/Ujung_Pandang
//                  zone : Atlantic/South_Georgia
//                  zone : Europe/Lisbon
//                  zone : Asia/Harbin
//                  zone : Europe/Oslo
//                  zone : Asia/Novokuznetsk
//                  zone : CST6CDT
//                  zone : Atlantic/Canary
//                  zone : America/Knox_IN
//                  zone : Asia/Kuwait
//                  zone : SystemV/HST10
//                  zone : Pacific/Efate
//                  zone : Africa/Lome
//                  zone : America/Bogota
//                  zone : America/Menominee
//                  zone : America/Adak
//                  zone : Pacific/Norfolk
//                  zone : Europe/Kirov
//                  zone : America/Resolute
//                  zone : Pacific/Kanton
//                  zone : Pacific/Tarawa
//                  zone : Africa/Kampala
//                  zone : Asia/Krasnoyarsk
//                  zone : Greenwich
//                  zone : SystemV/EST5
//                  zone : America/Edmonton
//                  zone : Europe/Podgorica
//                  zone : Australia/South
//                  zone : Canada/Central
//                  zone : Africa/Bujumbura
//                  zone : America/Santo_Domingo
//                  zone : US/Eastern
//                  zone : Europe/Minsk
//                  zone : Pacific/Auckland
//                  zone : Africa/Casablanca
//                  zone : America/Glace_Bay
//                  zone : Canada/Eastern
//                  zone : Asia/Qatar
//                  zone : Europe/Kiev
//                  zone : Singapore
//                  zone : Asia/Magadan
//                  zone : SystemV/PST8
//                  zone : America/Port-au-Prince
//                  zone : Europe/Belfast
//                  zone : America/St_Barthelemy
//                  zone : Asia/Ashgabat
//                  zone : Africa/Luanda
//                  zone : America/Nipigon
//                  zone : Atlantic/Jan_Mayen
//                  zone : Brazil/Acre
//                  zone : Asia/Muscat
//                  zone : Asia/Bahrain
//                  zone : Europe/Vilnius
//                  zone : America/Fortaleza
//                  zone : Etc/GMT0
//                  zone : US/East-Indiana
//                  zone : America/Hermosillo
//                  zone : America/Cancun
//                  zone : Africa/Maseru
//                  zone : Pacific/Kosrae
//                  zone : Africa/Kinshasa
//                  zone : Asia/Kathmandu
//                  zone : Asia/Seoul
//                  zone : Australia/Sydney
//                  zone : America/Lima
//                  zone : Australia/LHI
//                  zone : America/St_Lucia
//                  zone : Europe/Madrid
//                  zone : America/Bahia_Banderas
//                  zone : America/Montserrat
//                  zone : Asia/Brunei
//                  zone : America/Santa_Isabel
//                  zone : Canada/Mountain
//                  zone : America/Cambridge_Bay
//                  zone : Asia/Colombo
//                  zone : Australia/West
//                  zone : Indian/Antananarivo
//                  zone : Australia/Brisbane
//                  zone : Indian/Mayotte
//                  zone : US/Indiana-Starke
//                  zone : Asia/Urumqi
//                  zone : US/Aleutian
//                  zone : Europe/Volgograd
//                  zone : America/Lower_Princes
//                  zone : America/Vancouver
//                  zone : Africa/Blantyre
//                  zone : America/Rio_Branco
//                  zone : America/Danmarkshavn
//                  zone : America/Detroit
//                  zone : America/Thule
//                  zone : Africa/Lusaka
//                  zone : Asia/Hong_Kong
//                  zone : Iran
//                  zone : America/Argentina/La_Rioja
//                  zone : Africa/Dakar
//                  zone : SystemV/CST6CDT
//                  zone : America/Tortola
//                  zone : America/Porto_Velho
//                  zone : Asia/Sakhalin
//                  zone : Etc/GMT+10
//                  zone : America/Scoresbysund
//                  zone : Asia/Kamchatka
//                  zone : Asia/Thimbu
//                  zone : Africa/Harare
//                  zone : Etc/GMT+12
//                  zone : Etc/GMT+11
//                  zone : Navajo
//                  zone : America/Nome
//                  zone : Europe/Tallinn
//                  zone : Turkey
//                  zone : Africa/Khartoum
//                  zone : Africa/Johannesburg
//                  zone : Africa/Bangui
//                  zone : Europe/Belgrade
//                  zone : Jamaica
//                  zone : Africa/Bissau
//                  zone : Asia/Tehran
//                  zone : WET
//                  zone : Europe/Astrakhan
//                  zone : Africa/Juba
//                  zone : America/Campo_Grande
//                  zone : America/Belem
//                  zone : Etc/Greenwich
//                  zone : Asia/Saigon
//                  zone : America/Ensenada
//                  zone : Pacific/Midway
//                  zone : America/Jujuy
//                  zone : Africa/Timbuktu
//                  zone : America/Bahia
//                  zone : America/Goose_Bay
//                  zone : America/Virgin
//                  zone : America/Pangnirtung
//                  zone : Asia/Katmandu
//                  zone : America/Phoenix
//                  zone : Africa/Niamey
//                  zone : America/Whitehorse
//                  zone : Pacific/Noumea
//                  zone : Asia/Tbilisi
//                  zone : America/Montreal
//                  zone : Asia/Makassar
//                  zone : America/Argentina/San_Juan
//                  zone : Hongkong
//                  zone : UCT
//                  zone : Asia/Nicosia
//                  zone : America/Indiana/Winamac
//                  zone : SystemV/MST7MDT
//                  zone : America/Argentina/ComodRivadavia
//                  zone : America/Boa_Vista
//                  zone : America/Grenada
//                  zone : Asia/Atyrau
//                  zone : Australia/Darwin
//                  zone : Asia/Khandyga
//                  zone : Asia/Kuala_Lumpur
//                  zone : Asia/Famagusta
//                  zone : Asia/Thimphu
//                  zone : Asia/Rangoon
//                  zone : Europe/Bratislava
//                  zone : Asia/Calcutta
//                  zone : America/Argentina/Tucuman
//                  zone : Asia/Kabul
//                  zone : Indian/Cocos
//                  zone : Japan
//                  zone : Pacific/Tongatapu
//                  zone : America/New_York
//                  zone : Etc/GMT-12
//                  zone : Etc/GMT-11
//                  zone : America/Nuuk
//                  zone : Etc/GMT-10
//                  zone : SystemV/YST9YDT
//                  zone : Europe/Ulyanovsk
//                  zone : Etc/GMT-14
//                  zone : Etc/GMT-13
//                  zone : W-SU
//                  zone : America/Merida
//                  zone : EET
//                  zone : America/Rosario
//                  zone : Canada/Saskatchewan
//                  zone : America/St_Kitts
//                  zone : Arctic/Longyearbyen
//                  zone : America/Fort_Nelson
//                  zone : America/Caracas
//                  zone : America/Guadeloupe
//                  zone : Asia/Hebron
//                  zone : Indian/Kerguelen
//                  zone : SystemV/PST8PDT
//                  zone : Africa/Monrovia
//                  zone : Asia/Ust-Nera
//                  zone : Egypt
//                  zone : Asia/Srednekolymsk
//                  zone : America/North_Dakota/New_Salem
//                  zone : Asia/Anadyr
//                  zone : Australia/Melbourne
//                  zone : Asia/Irkutsk
//                  zone : America/Shiprock
//                  zone : America/Winnipeg
//                  zone : Europe/Vatican
//                  zone : Asia/Amman
//                  zone : Etc/UTC
//                  zone : SystemV/AST4ADT
//                  zone : Asia/Tokyo
//                  zone : America/Toronto
//                  zone : Asia/Singapore
//                  zone : Australia/Lindeman
//                  zone : America/Los_Angeles
//                  zone : SystemV/EST5EDT
//                  zone : Pacific/Majuro
//                  zone : America/Argentina/Buenos_Aires
//                  zone : Europe/Nicosia
//                  zone : Pacific/Guadalcanal
//                  zone : Europe/Athens
//                  zone : US/Pacific
//                  zone : Europe/Monaco
              });
    }

    @Test
    void of() {
        int year = 2022;
        Month month = Month.of(1);
        int dayOfMonth = 10;
        int hour = 11;
        int minute = 12;
        int second = 13;
        int nanoOfSecond = 14;

        final LocalDateTime local = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second,
                                                     nanoOfSecond);

        Assertions.assertEquals(year, local.getYear());
        Assertions.assertEquals(month, local.getMonth());
        Assertions.assertEquals(dayOfMonth, local.getDayOfMonth());
        Assertions.assertEquals(hour, local.getHour());
        Assertions.assertEquals(minute, local.getMinute());
        Assertions.assertEquals(second, local.getSecond());
        Assertions.assertEquals(nanoOfSecond, local.getNano());
    }

    @Test
    void from() {
        int year = 2022;
        Month month = Month.of(1);
        int dayOfMonth = 10;
        int hour = 11;
        int minute = 12;
        int second = 13;
        int nanoOfSecond = 14;

        final LocalDateTime local = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second,
                                                     nanoOfSecond);
        final LocalDateTime from = LocalDateTime.from(local);

        Assertions.assertEquals(year, from.getYear());
        Assertions.assertEquals(month, from.getMonth());
        Assertions.assertEquals(dayOfMonth, from.getDayOfMonth());
        Assertions.assertEquals(hour, from.getHour());
        Assertions.assertEquals(minute, from.getMinute());
        Assertions.assertEquals(second, from.getSecond());
        Assertions.assertEquals(nanoOfSecond, from.getNano());

    }

    @Test
    void ofEpochSecond() {
        final ZoneOffset of = ZoneOffset.UTC;
        final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(LocalDateTime.now().toEpochSecond(of),
                                                                        0, of);
        System.out.println(localDateTime);
    }

    @Test
    void ofInstant() {
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(
                LocalDateTime.now().toInstant(ZoneOffset.ofHours(9)),
//                LocalDateTime.now().toInstant(ZoneOffset.systemDefault()),
//                ZoneId.systemDefault());
//        final LocalDateTime localDateTime = LocalDateTime.ofInstant(
//                LocalDateTime.now().toInstant(ZoneOffset.of(ZoneId.of("Asia/Tokyo").getId())),
//                ZoneId.of(String.valueOf(ZoneId.of("Asia/Tokyo"))));
                ZoneId.of(String.valueOf(ZoneId.of("Asia/Tokyo"))));
        System.out.println(localDateTime);
    }

    @Test
    void parse() {
        final LocalDateTime parse = LocalDateTime.parse("2022-08-14T21:42:32.685169");
        Assertions.assertNotNull(parse);

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //hh : 1-12
        //HH 0-23
        final LocalDateTime parse1 = LocalDateTime.parse("2022-08-14 21:42:32", dateTimeFormatter);
        Assertions.assertNotNull(parse1);
    }

}
