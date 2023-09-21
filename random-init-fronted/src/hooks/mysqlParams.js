export default function () {
    let charSetParams = [
        {
            "value": "armscii8",
        },
        {
            "value": "ascii",
        },
        {
            "value": "big5",
        },
        {
            "value": "binary",
        },
        {
            "value": "cp1250",
        },
        {
            "value": "cp1251",
        },
        {
            "value": "cp1256",
        },
        {
            "value": "cp1257",
        },
        {
            "value": "cp850",
        },
        {
            "value": "cp852",
        },
        {
            "value": "cp866",
        },
        {
            "value": "cp932",
        },
        {
            "value": "dec8",
        },
        {
            "value": "eucjpms",
        },
        {
            "value": "euckr",
        },
        {
            "value": "gb18030",
        },
        {
            "value": "gb2312",
        },
        {
            "value": "gbk",
        },
        {
            "value": "geostd8",
        },
        {
            "value": "greek",
        },
        {
            "value": "hebrew",
        },
        {
            "value": "hp8",
        },
        {
            "value": "keybcs2",
        },
        {
            "value": "koi8r",
        },
        {
            "value": "koi8u",
        },
        {
            "value": "latin1",
        },
        {
            "value": "latin2",
        },
        {
            "value": "latin5",
        },
        {
            "value": "latin7",
        },
        {
            "value": "macce",
        },
        {
            "value": "macroman",
        },
        {
            "value": "sjis",
        },
        {
            "value": "swe7",
        },
        {
            "value": "tis620",
        },
        {
            "value": "ucs2",
        },
        {
            "value": "ujis",
        },
        {
            "value": "utf16",
        },
        {
            "value": "utf16le",
        },
        {
            "value": "utf32",
        },
        {
            "value": "utf8mb3",
        },
        {
            "value": "utf8mb4",
        }
    ]

    const indexParams = [
        {
            label: "KEY",
            value: "KEY"
        },
        {
            label: "PRIMARY KEY",
            value: "PRIMARY KEY"
        },
        {
            label: "UNIQUE KEY",
            value: "UNIQUE KEY"
        },
        {
            label: "FULLTEXT KEY",
            value: "FULLTEXT KEY"
        }
    ]

    const engineParams = [
        {
            "label": "MEMORY",
            "value": "MEMORY"
        },
        {
            "label": "MRG_MYISAM",
            "value": "MRG_MYISAM"
        },
        {
            "label": "CSV",
            "value": "CSV"
        },
        {
            "label": "FEDERATED",
            "value": "FEDERATED"
        },
        {
            "label": "PERFORMANCE_SCHEMA",
            "value": "PERFORMANCE_SCHEMA"
        },
        {
            "label": "MyISAM",
            "value": "MyISAM"
        },
        {
            "label": "InnoDB",
            "value": "InnoDB"
        },
        {
            "label": "ndbinfo",
            "value": "ndbinfo"
        },
        {
            "label": "BLACKHOLE",
            "value": "BLACKHOLE"
        },
        {
            "label": "ARCHIVE",
            "value": "ARCHIVE"
        },
        {
            "label": "ndbcluster",
            "value": "ndbcluster"
        }
    ]

    let collationParams = [
        {
            "label": "armscii8_bin",
            "value": "armscii8_bin"
        },
        {
            "label": "armscii8_general_ci",
            "value": "armscii8_general_ci"
        },
        {
            "label": "ascii_bin",
            "value": "ascii_bin"
        },
        {
            "label": "ascii_general_ci",
            "value": "ascii_general_ci"
        },
        {
            "label": "big5_bin",
            "value": "big5_bin"
        },
        {
            "label": "big5_chinese_ci",
            "value": "big5_chinese_ci"
        },
        {
            "label": "binary",
            "value": "binary"
        },
        {
            "label": "cp1250_bin",
            "value": "cp1250_bin"
        },
        {
            "label": "cp1250_croatian_ci",
            "value": "cp1250_croatian_ci"
        },
        {
            "label": "cp1250_czech_cs",
            "value": "cp1250_czech_cs"
        },
        {
            "label": "cp1250_general_ci",
            "value": "cp1250_general_ci"
        },
        {
            "label": "cp1250_polish_ci",
            "value": "cp1250_polish_ci"
        },
        {
            "label": "cp1251_bin",
            "value": "cp1251_bin"
        },
        {
            "label": "cp1251_bulgarian_ci",
            "value": "cp1251_bulgarian_ci"
        },
        {
            "label": "cp1251_general_ci",
            "value": "cp1251_general_ci"
        },
        {
            "label": "cp1251_general_cs",
            "value": "cp1251_general_cs"
        },
        {
            "label": "cp1251_ukrainian_ci",
            "value": "cp1251_ukrainian_ci"
        },
        {
            "label": "cp1256_bin",
            "value": "cp1256_bin"
        },
        {
            "label": "cp1256_general_ci",
            "value": "cp1256_general_ci"
        },
        {
            "label": "cp1257_bin",
            "value": "cp1257_bin"
        },
        {
            "label": "cp1257_general_ci",
            "value": "cp1257_general_ci"
        },
        {
            "label": "cp1257_lithuanian_ci",
            "value": "cp1257_lithuanian_ci"
        },
        {
            "label": "cp850_bin",
            "value": "cp850_bin"
        },
        {
            "label": "cp850_general_ci",
            "value": "cp850_general_ci"
        },
        {
            "label": "cp852_bin",
            "value": "cp852_bin"
        },
        {
            "label": "cp852_general_ci",
            "value": "cp852_general_ci"
        },
        {
            "label": "cp866_bin",
            "value": "cp866_bin"
        },
        {
            "label": "cp866_general_ci",
            "value": "cp866_general_ci"
        },
        {
            "label": "cp932_bin",
            "value": "cp932_bin"
        },
        {
            "label": "cp932_japanese_ci",
            "value": "cp932_japanese_ci"
        },
        {
            "label": "dec8_bin",
            "value": "dec8_bin"
        },
        {
            "label": "dec8_swedish_ci",
            "value": "dec8_swedish_ci"
        },
        {
            "label": "eucjpms_bin",
            "value": "eucjpms_bin"
        },
        {
            "label": "eucjpms_japanese_ci",
            "value": "eucjpms_japanese_ci"
        },
        {
            "label": "euckr_bin",
            "value": "euckr_bin"
        },
        {
            "label": "euckr_korean_ci",
            "value": "euckr_korean_ci"
        },
        {
            "label": "gb18030_bin",
            "value": "gb18030_bin"
        },
        {
            "label": "gb18030_chinese_ci",
            "value": "gb18030_chinese_ci"
        },
        {
            "label": "gb18030_unicode_520_ci",
            "value": "gb18030_unicode_520_ci"
        },
        {
            "label": "gb2312_bin",
            "value": "gb2312_bin"
        },
        {
            "label": "gb2312_chinese_ci",
            "value": "gb2312_chinese_ci"
        },
        {
            "label": "gbk_bin",
            "value": "gbk_bin"
        },
        {
            "label": "gbk_chinese_ci",
            "value": "gbk_chinese_ci"
        },
        {
            "label": "geostd8_bin",
            "value": "geostd8_bin"
        },
        {
            "label": "geostd8_general_ci",
            "value": "geostd8_general_ci"
        },
        {
            "label": "greek_bin",
            "value": "greek_bin"
        },
        {
            "label": "greek_general_ci",
            "value": "greek_general_ci"
        },
        {
            "label": "hebrew_bin",
            "value": "hebrew_bin"
        },
        {
            "label": "hebrew_general_ci",
            "value": "hebrew_general_ci"
        },
        {
            "label": "hp8_bin",
            "value": "hp8_bin"
        },
        {
            "label": "hp8_english_ci",
            "value": "hp8_english_ci"
        },
        {
            "label": "keybcs2_bin",
            "value": "keybcs2_bin"
        },
        {
            "label": "keybcs2_general_ci",
            "value": "keybcs2_general_ci"
        },
        {
            "label": "koi8r_bin",
            "value": "koi8r_bin"
        },
        {
            "label": "koi8r_general_ci",
            "value": "koi8r_general_ci"
        },
        {
            "label": "koi8u_bin",
            "value": "koi8u_bin"
        },
        {
            "label": "koi8u_general_ci",
            "value": "koi8u_general_ci"
        },
        {
            "label": "latin1_bin",
            "value": "latin1_bin"
        },
        {
            "label": "latin1_danish_ci",
            "value": "latin1_danish_ci"
        },
        {
            "label": "latin1_general_ci",
            "value": "latin1_general_ci"
        },
        {
            "label": "latin1_general_cs",
            "value": "latin1_general_cs"
        },
        {
            "label": "latin1_german1_ci",
            "value": "latin1_german1_ci"
        },
        {
            "label": "latin1_german2_ci",
            "value": "latin1_german2_ci"
        },
        {
            "label": "latin1_spanish_ci",
            "value": "latin1_spanish_ci"
        },
        {
            "label": "latin1_swedish_ci",
            "value": "latin1_swedish_ci"
        },
        {
            "label": "latin2_bin",
            "value": "latin2_bin"
        },
        {
            "label": "latin2_croatian_ci",
            "value": "latin2_croatian_ci"
        },
        {
            "label": "latin2_czech_cs",
            "value": "latin2_czech_cs"
        },
        {
            "label": "latin2_general_ci",
            "value": "latin2_general_ci"
        },
        {
            "label": "latin2_hungarian_ci",
            "value": "latin2_hungarian_ci"
        },
        {
            "label": "latin5_bin",
            "value": "latin5_bin"
        },
        {
            "label": "latin5_turkish_ci",
            "value": "latin5_turkish_ci"
        },
        {
            "label": "latin7_bin",
            "value": "latin7_bin"
        },
        {
            "label": "latin7_estonian_cs",
            "value": "latin7_estonian_cs"
        },
        {
            "label": "latin7_general_ci",
            "value": "latin7_general_ci"
        },
        {
            "label": "latin7_general_cs",
            "value": "latin7_general_cs"
        },
        {
            "label": "macce_bin",
            "value": "macce_bin"
        },
        {
            "label": "macce_general_ci",
            "value": "macce_general_ci"
        },
        {
            "label": "macroman_bin",
            "value": "macroman_bin"
        },
        {
            "label": "macroman_general_ci",
            "value": "macroman_general_ci"
        },
        {
            "label": "sjis_bin",
            "value": "sjis_bin"
        },
        {
            "label": "sjis_japanese_ci",
            "value": "sjis_japanese_ci"
        },
        {
            "label": "swe7_bin",
            "value": "swe7_bin"
        },
        {
            "label": "swe7_swedish_ci",
            "value": "swe7_swedish_ci"
        },
        {
            "label": "tis620_bin",
            "value": "tis620_bin"
        },
        {
            "label": "tis620_thai_ci",
            "value": "tis620_thai_ci"
        },
        {
            "label": "ucs2_bin",
            "value": "ucs2_bin"
        },
        {
            "label": "ucs2_croatian_ci",
            "value": "ucs2_croatian_ci"
        },
        {
            "label": "ucs2_czech_ci",
            "value": "ucs2_czech_ci"
        },
        {
            "label": "ucs2_danish_ci",
            "value": "ucs2_danish_ci"
        },
        {
            "label": "ucs2_esperanto_ci",
            "value": "ucs2_esperanto_ci"
        },
        {
            "label": "ucs2_estonian_ci",
            "value": "ucs2_estonian_ci"
        },
        {
            "label": "ucs2_general_ci",
            "value": "ucs2_general_ci"
        },
        {
            "label": "ucs2_general_mysql500_ci",
            "value": "ucs2_general_mysql500_ci"
        },
        {
            "label": "ucs2_german2_ci",
            "value": "ucs2_german2_ci"
        },
        {
            "label": "ucs2_hungarian_ci",
            "value": "ucs2_hungarian_ci"
        },
        {
            "label": "ucs2_icelandic_ci",
            "value": "ucs2_icelandic_ci"
        },
        {
            "label": "ucs2_latvian_ci",
            "value": "ucs2_latvian_ci"
        },
        {
            "label": "ucs2_lithuanian_ci",
            "value": "ucs2_lithuanian_ci"
        },
        {
            "label": "ucs2_persian_ci",
            "value": "ucs2_persian_ci"
        },
        {
            "label": "ucs2_polish_ci",
            "value": "ucs2_polish_ci"
        },
        {
            "label": "ucs2_romanian_ci",
            "value": "ucs2_romanian_ci"
        },
        {
            "label": "ucs2_roman_ci",
            "value": "ucs2_roman_ci"
        },
        {
            "label": "ucs2_sinhala_ci",
            "value": "ucs2_sinhala_ci"
        },
        {
            "label": "ucs2_slovak_ci",
            "value": "ucs2_slovak_ci"
        },
        {
            "label": "ucs2_slovenian_ci",
            "value": "ucs2_slovenian_ci"
        },
        {
            "label": "ucs2_spanish2_ci",
            "value": "ucs2_spanish2_ci"
        },
        {
            "label": "ucs2_spanish_ci",
            "value": "ucs2_spanish_ci"
        },
        {
            "label": "ucs2_swedish_ci",
            "value": "ucs2_swedish_ci"
        },
        {
            "label": "ucs2_turkish_ci",
            "value": "ucs2_turkish_ci"
        },
        {
            "label": "ucs2_unicode_520_ci",
            "value": "ucs2_unicode_520_ci"
        },
        {
            "label": "ucs2_unicode_ci",
            "value": "ucs2_unicode_ci"
        },
        {
            "label": "ucs2_vietnamese_ci",
            "value": "ucs2_vietnamese_ci"
        },
        {
            "label": "ujis_bin",
            "value": "ujis_bin"
        },
        {
            "label": "ujis_japanese_ci",
            "value": "ujis_japanese_ci"
        },
        {
            "label": "utf16le_bin",
            "value": "utf16le_bin"
        },
        {
            "label": "utf16le_general_ci",
            "value": "utf16le_general_ci"
        },
        {
            "label": "utf16_bin",
            "value": "utf16_bin"
        },
        {
            "label": "utf16_croatian_ci",
            "value": "utf16_croatian_ci"
        },
        {
            "label": "utf16_czech_ci",
            "value": "utf16_czech_ci"
        },
        {
            "label": "utf16_danish_ci",
            "value": "utf16_danish_ci"
        },
        {
            "label": "utf16_esperanto_ci",
            "value": "utf16_esperanto_ci"
        },
        {
            "label": "utf16_estonian_ci",
            "value": "utf16_estonian_ci"
        },
        {
            "label": "utf16_general_ci",
            "value": "utf16_general_ci"
        },
        {
            "label": "utf16_german2_ci",
            "value": "utf16_german2_ci"
        },
        {
            "label": "utf16_hungarian_ci",
            "value": "utf16_hungarian_ci"
        },
        {
            "label": "utf16_icelandic_ci",
            "value": "utf16_icelandic_ci"
        },
        {
            "label": "utf16_latvian_ci",
            "value": "utf16_latvian_ci"
        },
        {
            "label": "utf16_lithuanian_ci",
            "value": "utf16_lithuanian_ci"
        },
        {
            "label": "utf16_persian_ci",
            "value": "utf16_persian_ci"
        },
        {
            "label": "utf16_polish_ci",
            "value": "utf16_polish_ci"
        },
        {
            "label": "utf16_romanian_ci",
            "value": "utf16_romanian_ci"
        },
        {
            "label": "utf16_roman_ci",
            "value": "utf16_roman_ci"
        },
        {
            "label": "utf16_sinhala_ci",
            "value": "utf16_sinhala_ci"
        },
        {
            "label": "utf16_slovak_ci",
            "value": "utf16_slovak_ci"
        },
        {
            "label": "utf16_slovenian_ci",
            "value": "utf16_slovenian_ci"
        },
        {
            "label": "utf16_spanish2_ci",
            "value": "utf16_spanish2_ci"
        },
        {
            "label": "utf16_spanish_ci",
            "value": "utf16_spanish_ci"
        },
        {
            "label": "utf16_swedish_ci",
            "value": "utf16_swedish_ci"
        },
        {
            "label": "utf16_turkish_ci",
            "value": "utf16_turkish_ci"
        },
        {
            "label": "utf16_unicode_520_ci",
            "value": "utf16_unicode_520_ci"
        },
        {
            "label": "utf16_unicode_ci",
            "value": "utf16_unicode_ci"
        },
        {
            "label": "utf16_vietnamese_ci",
            "value": "utf16_vietnamese_ci"
        },
        {
            "label": "utf32_bin",
            "value": "utf32_bin"
        },
        {
            "label": "utf32_croatian_ci",
            "value": "utf32_croatian_ci"
        },
        {
            "label": "utf32_czech_ci",
            "value": "utf32_czech_ci"
        },
        {
            "label": "utf32_danish_ci",
            "value": "utf32_danish_ci"
        },
        {
            "label": "utf32_esperanto_ci",
            "value": "utf32_esperanto_ci"
        },
        {
            "label": "utf32_estonian_ci",
            "value": "utf32_estonian_ci"
        },
        {
            "label": "utf32_general_ci",
            "value": "utf32_general_ci"
        },
        {
            "label": "utf32_german2_ci",
            "value": "utf32_german2_ci"
        },
        {
            "label": "utf32_hungarian_ci",
            "value": "utf32_hungarian_ci"
        },
        {
            "label": "utf32_icelandic_ci",
            "value": "utf32_icelandic_ci"
        },
        {
            "label": "utf32_latvian_ci",
            "value": "utf32_latvian_ci"
        },
        {
            "label": "utf32_lithuanian_ci",
            "value": "utf32_lithuanian_ci"
        },
        {
            "label": "utf32_persian_ci",
            "value": "utf32_persian_ci"
        },
        {
            "label": "utf32_polish_ci",
            "value": "utf32_polish_ci"
        },
        {
            "label": "utf32_romanian_ci",
            "value": "utf32_romanian_ci"
        },
        {
            "label": "utf32_roman_ci",
            "value": "utf32_roman_ci"
        },
        {
            "label": "utf32_sinhala_ci",
            "value": "utf32_sinhala_ci"
        },
        {
            "label": "utf32_slovak_ci",
            "value": "utf32_slovak_ci"
        },
        {
            "label": "utf32_slovenian_ci",
            "value": "utf32_slovenian_ci"
        },
        {
            "label": "utf32_spanish2_ci",
            "value": "utf32_spanish2_ci"
        },
        {
            "label": "utf32_spanish_ci",
            "value": "utf32_spanish_ci"
        },
        {
            "label": "utf32_swedish_ci",
            "value": "utf32_swedish_ci"
        },
        {
            "label": "utf32_turkish_ci",
            "value": "utf32_turkish_ci"
        },
        {
            "label": "utf32_unicode_520_ci",
            "value": "utf32_unicode_520_ci"
        },
        {
            "label": "utf32_unicode_ci",
            "value": "utf32_unicode_ci"
        },
        {
            "label": "utf32_vietnamese_ci",
            "value": "utf32_vietnamese_ci"
        },
        {
            "label": "utf8mb3_bin",
            "value": "utf8mb3_bin"
        },
        {
            "label": "utf8mb3_croatian_ci",
            "value": "utf8mb3_croatian_ci"
        },
        {
            "label": "utf8mb3_czech_ci",
            "value": "utf8mb3_czech_ci"
        },
        {
            "label": "utf8mb3_danish_ci",
            "value": "utf8mb3_danish_ci"
        },
        {
            "label": "utf8mb3_esperanto_ci",
            "value": "utf8mb3_esperanto_ci"
        },
        {
            "label": "utf8mb3_estonian_ci",
            "value": "utf8mb3_estonian_ci"
        },
        {
            "label": "utf8mb3_general_ci",
            "value": "utf8mb3_general_ci"
        },
        {
            "label": "utf8mb3_general_mysql500_ci",
            "value": "utf8mb3_general_mysql500_ci"
        },
        {
            "label": "utf8mb3_german2_ci",
            "value": "utf8mb3_german2_ci"
        },
        {
            "label": "utf8mb3_hungarian_ci",
            "value": "utf8mb3_hungarian_ci"
        },
        {
            "label": "utf8mb3_icelandic_ci",
            "value": "utf8mb3_icelandic_ci"
        },
        {
            "label": "utf8mb3_latvian_ci",
            "value": "utf8mb3_latvian_ci"
        },
        {
            "label": "utf8mb3_lithuanian_ci",
            "value": "utf8mb3_lithuanian_ci"
        },
        {
            "label": "utf8mb3_persian_ci",
            "value": "utf8mb3_persian_ci"
        },
        {
            "label": "utf8mb3_polish_ci",
            "value": "utf8mb3_polish_ci"
        },
        {
            "label": "utf8mb3_romanian_ci",
            "value": "utf8mb3_romanian_ci"
        },
        {
            "label": "utf8mb3_roman_ci",
            "value": "utf8mb3_roman_ci"
        },
        {
            "label": "utf8mb3_sinhala_ci",
            "value": "utf8mb3_sinhala_ci"
        },
        {
            "label": "utf8mb3_slovak_ci",
            "value": "utf8mb3_slovak_ci"
        },
        {
            "label": "utf8mb3_slovenian_ci",
            "value": "utf8mb3_slovenian_ci"
        },
        {
            "label": "utf8mb3_spanish2_ci",
            "value": "utf8mb3_spanish2_ci"
        },
        {
            "label": "utf8mb3_spanish_ci",
            "value": "utf8mb3_spanish_ci"
        },
        {
            "label": "utf8mb3_swedish_ci",
            "value": "utf8mb3_swedish_ci"
        },
        {
            "label": "utf8mb3_tolower_ci",
            "value": "utf8mb3_tolower_ci"
        },
        {
            "label": "utf8mb3_turkish_ci",
            "value": "utf8mb3_turkish_ci"
        },
        {
            "label": "utf8mb3_unicode_520_ci",
            "value": "utf8mb3_unicode_520_ci"
        },
        {
            "label": "utf8mb3_unicode_ci",
            "value": "utf8mb3_unicode_ci"
        },
        {
            "label": "utf8mb3_vietnamese_ci",
            "value": "utf8mb3_vietnamese_ci"
        },
        {
            "label": "utf8mb4_0900_ai_ci",
            "value": "utf8mb4_0900_ai_ci"
        },
        {
            "label": "utf8mb4_0900_as_ci",
            "value": "utf8mb4_0900_as_ci"
        },
        {
            "label": "utf8mb4_0900_as_cs",
            "value": "utf8mb4_0900_as_cs"
        },
        {
            "label": "utf8mb4_0900_bin",
            "value": "utf8mb4_0900_bin"
        },
        {
            "label": "utf8mb4_bg_0900_ai_ci",
            "value": "utf8mb4_bg_0900_ai_ci"
        },
        {
            "label": "utf8mb4_bg_0900_as_cs",
            "value": "utf8mb4_bg_0900_as_cs"
        },
        {
            "label": "utf8mb4_bin",
            "value": "utf8mb4_bin"
        },
        {
            "label": "utf8mb4_bs_0900_ai_ci",
            "value": "utf8mb4_bs_0900_ai_ci"
        },
        {
            "label": "utf8mb4_bs_0900_as_cs",
            "value": "utf8mb4_bs_0900_as_cs"
        },
        {
            "label": "utf8mb4_croatian_ci",
            "value": "utf8mb4_croatian_ci"
        },
        {
            "label": "utf8mb4_cs_0900_ai_ci",
            "value": "utf8mb4_cs_0900_ai_ci"
        },
        {
            "label": "utf8mb4_cs_0900_as_cs",
            "value": "utf8mb4_cs_0900_as_cs"
        },
        {
            "label": "utf8mb4_czech_ci",
            "value": "utf8mb4_czech_ci"
        },
        {
            "label": "utf8mb4_danish_ci",
            "value": "utf8mb4_danish_ci"
        },
        {
            "label": "utf8mb4_da_0900_ai_ci",
            "value": "utf8mb4_da_0900_ai_ci"
        },
        {
            "label": "utf8mb4_da_0900_as_cs",
            "value": "utf8mb4_da_0900_as_cs"
        },
        {
            "label": "utf8mb4_de_pb_0900_ai_ci",
            "value": "utf8mb4_de_pb_0900_ai_ci"
        },
        {
            "label": "utf8mb4_de_pb_0900_as_cs",
            "value": "utf8mb4_de_pb_0900_as_cs"
        },
        {
            "label": "utf8mb4_eo_0900_ai_ci",
            "value": "utf8mb4_eo_0900_ai_ci"
        },
        {
            "label": "utf8mb4_eo_0900_as_cs",
            "value": "utf8mb4_eo_0900_as_cs"
        },
        {
            "label": "utf8mb4_esperanto_ci",
            "value": "utf8mb4_esperanto_ci"
        },
        {
            "label": "utf8mb4_estonian_ci",
            "value": "utf8mb4_estonian_ci"
        },
        {
            "label": "utf8mb4_es_0900_ai_ci",
            "value": "utf8mb4_es_0900_ai_ci"
        },
        {
            "label": "utf8mb4_es_0900_as_cs",
            "value": "utf8mb4_es_0900_as_cs"
        },
        {
            "label": "utf8mb4_es_trad_0900_ai_ci",
            "value": "utf8mb4_es_trad_0900_ai_ci"
        },
        {
            "label": "utf8mb4_es_trad_0900_as_cs",
            "value": "utf8mb4_es_trad_0900_as_cs"
        },
        {
            "label": "utf8mb4_et_0900_ai_ci",
            "value": "utf8mb4_et_0900_ai_ci"
        },
        {
            "label": "utf8mb4_et_0900_as_cs",
            "value": "utf8mb4_et_0900_as_cs"
        },
        {
            "label": "utf8mb4_general_ci",
            "value": "utf8mb4_general_ci"
        },
        {
            "label": "utf8mb4_german2_ci",
            "value": "utf8mb4_german2_ci"
        },
        {
            "label": "utf8mb4_gl_0900_ai_ci",
            "value": "utf8mb4_gl_0900_ai_ci"
        },
        {
            "label": "utf8mb4_gl_0900_as_cs",
            "value": "utf8mb4_gl_0900_as_cs"
        },
        {
            "label": "utf8mb4_hr_0900_ai_ci",
            "value": "utf8mb4_hr_0900_ai_ci"
        },
        {
            "label": "utf8mb4_hr_0900_as_cs",
            "value": "utf8mb4_hr_0900_as_cs"
        },
        {
            "label": "utf8mb4_hungarian_ci",
            "value": "utf8mb4_hungarian_ci"
        },
        {
            "label": "utf8mb4_hu_0900_ai_ci",
            "value": "utf8mb4_hu_0900_ai_ci"
        },
        {
            "label": "utf8mb4_hu_0900_as_cs",
            "value": "utf8mb4_hu_0900_as_cs"
        },
        {
            "label": "utf8mb4_icelandic_ci",
            "value": "utf8mb4_icelandic_ci"
        },
        {
            "label": "utf8mb4_is_0900_ai_ci",
            "value": "utf8mb4_is_0900_ai_ci"
        },
        {
            "label": "utf8mb4_is_0900_as_cs",
            "value": "utf8mb4_is_0900_as_cs"
        },
        {
            "label": "utf8mb4_ja_0900_as_cs",
            "value": "utf8mb4_ja_0900_as_cs"
        },
        {
            "label": "utf8mb4_ja_0900_as_cs_ks",
            "value": "utf8mb4_ja_0900_as_cs_ks"
        },
        {
            "label": "utf8mb4_latvian_ci",
            "value": "utf8mb4_latvian_ci"
        },
        {
            "label": "utf8mb4_la_0900_ai_ci",
            "value": "utf8mb4_la_0900_ai_ci"
        },
        {
            "label": "utf8mb4_la_0900_as_cs",
            "value": "utf8mb4_la_0900_as_cs"
        },
        {
            "label": "utf8mb4_lithuanian_ci",
            "value": "utf8mb4_lithuanian_ci"
        },
        {
            "label": "utf8mb4_lt_0900_ai_ci",
            "value": "utf8mb4_lt_0900_ai_ci"
        },
        {
            "label": "utf8mb4_lt_0900_as_cs",
            "value": "utf8mb4_lt_0900_as_cs"
        },
        {
            "label": "utf8mb4_lv_0900_ai_ci",
            "value": "utf8mb4_lv_0900_ai_ci"
        },
        {
            "label": "utf8mb4_lv_0900_as_cs",
            "value": "utf8mb4_lv_0900_as_cs"
        },
        {
            "label": "utf8mb4_mn_cyrl_0900_ai_ci",
            "value": "utf8mb4_mn_cyrl_0900_ai_ci"
        },
        {
            "label": "utf8mb4_mn_cyrl_0900_as_cs",
            "value": "utf8mb4_mn_cyrl_0900_as_cs"
        },
        {
            "label": "utf8mb4_nb_0900_ai_ci",
            "value": "utf8mb4_nb_0900_ai_ci"
        },
        {
            "label": "utf8mb4_nb_0900_as_cs",
            "value": "utf8mb4_nb_0900_as_cs"
        },
        {
            "label": "utf8mb4_nn_0900_ai_ci",
            "value": "utf8mb4_nn_0900_ai_ci"
        },
        {
            "label": "utf8mb4_nn_0900_as_cs",
            "value": "utf8mb4_nn_0900_as_cs"
        },
        {
            "label": "utf8mb4_persian_ci",
            "value": "utf8mb4_persian_ci"
        },
        {
            "label": "utf8mb4_pl_0900_ai_ci",
            "value": "utf8mb4_pl_0900_ai_ci"
        },
        {
            "label": "utf8mb4_pl_0900_as_cs",
            "value": "utf8mb4_pl_0900_as_cs"
        },
        {
            "label": "utf8mb4_polish_ci",
            "value": "utf8mb4_polish_ci"
        },
        {
            "label": "utf8mb4_romanian_ci",
            "value": "utf8mb4_romanian_ci"
        },
        {
            "label": "utf8mb4_roman_ci",
            "value": "utf8mb4_roman_ci"
        },
        {
            "label": "utf8mb4_ro_0900_ai_ci",
            "value": "utf8mb4_ro_0900_ai_ci"
        },
        {
            "label": "utf8mb4_ro_0900_as_cs",
            "value": "utf8mb4_ro_0900_as_cs"
        },
        {
            "label": "utf8mb4_ru_0900_ai_ci",
            "value": "utf8mb4_ru_0900_ai_ci"
        },
        {
            "label": "utf8mb4_ru_0900_as_cs",
            "value": "utf8mb4_ru_0900_as_cs"
        },
        {
            "label": "utf8mb4_sinhala_ci",
            "value": "utf8mb4_sinhala_ci"
        },
        {
            "label": "utf8mb4_sk_0900_ai_ci",
            "value": "utf8mb4_sk_0900_ai_ci"
        },
        {
            "label": "utf8mb4_sk_0900_as_cs",
            "value": "utf8mb4_sk_0900_as_cs"
        },
        {
            "label": "utf8mb4_slovak_ci",
            "value": "utf8mb4_slovak_ci"
        },
        {
            "label": "utf8mb4_slovenian_ci",
            "value": "utf8mb4_slovenian_ci"
        },
        {
            "label": "utf8mb4_sl_0900_ai_ci",
            "value": "utf8mb4_sl_0900_ai_ci"
        },
        {
            "label": "utf8mb4_sl_0900_as_cs",
            "value": "utf8mb4_sl_0900_as_cs"
        },
        {
            "label": "utf8mb4_spanish2_ci",
            "value": "utf8mb4_spanish2_ci"
        },
        {
            "label": "utf8mb4_spanish_ci",
            "value": "utf8mb4_spanish_ci"
        },
        {
            "label": "utf8mb4_sr_latn_0900_ai_ci",
            "value": "utf8mb4_sr_latn_0900_ai_ci"
        },
        {
            "label": "utf8mb4_sr_latn_0900_as_cs",
            "value": "utf8mb4_sr_latn_0900_as_cs"
        },
        {
            "label": "utf8mb4_sv_0900_ai_ci",
            "value": "utf8mb4_sv_0900_ai_ci"
        },
        {
            "label": "utf8mb4_sv_0900_as_cs",
            "value": "utf8mb4_sv_0900_as_cs"
        },
        {
            "label": "utf8mb4_swedish_ci",
            "value": "utf8mb4_swedish_ci"
        },
        {
            "label": "utf8mb4_tr_0900_ai_ci",
            "value": "utf8mb4_tr_0900_ai_ci"
        },
        {
            "label": "utf8mb4_tr_0900_as_cs",
            "value": "utf8mb4_tr_0900_as_cs"
        },
        {
            "label": "utf8mb4_turkish_ci",
            "value": "utf8mb4_turkish_ci"
        },
        {
            "label": "utf8mb4_unicode_520_ci",
            "value": "utf8mb4_unicode_520_ci"
        },
        {
            "label": "utf8mb4_unicode_ci",
            "value": "utf8mb4_unicode_ci"
        },
        {
            "label": "utf8mb4_vietnamese_ci",
            "value": "utf8mb4_vietnamese_ci"
        },
        {
            "label": "utf8mb4_vi_0900_ai_ci",
            "value": "utf8mb4_vi_0900_ai_ci"
        },
        {
            "label": "utf8mb4_vi_0900_as_cs",
            "value": "utf8mb4_vi_0900_as_cs"
        },
        {
            "label": "utf8mb4_zh_0900_as_cs",
            "value": "utf8mb4_zh_0900_as_cs"
        }
    ]

    const typeOptions  = computed(() => {
        const typeList = [
            "INT",
            "VARCHAR",
            "DECIMAL",
            "BOOLEAN",
            "DATETIME",
            "BIGINT",
            "TEXT",
            "JSON",
            "DOUBLE",
            "TINYINT",
            "SMALLINT",
            "MEDIUMINT",
            "FLOAT",
            "TIMESTAMP",
            "DATE",
            "TIME",
            "YEAR",
            "TIMESTAMP",
            "CHAR",
            "BLOB"
        ]
        const params = []
        for (const typeName of typeList) {
            params.push({"label": typeName, "value": typeName})
        }
        return params
    })

    const extraParams = [
        {
            label: "",
            value: ""
        },
        {
            label: "AUTO_INCREMENT",
            value: "AUTO_INCREMENT"
        },
        {
            label: "ON UPDATE CURRENT_TIMESTAMP",
            value: "ON UPDATE CURRENT_TIMESTAMP"
        }
    ]

    return {
        charSetParams,
        indexParams,
        engineParams,
        collationParams,
        typeOptions,
        extraParams
    }
}