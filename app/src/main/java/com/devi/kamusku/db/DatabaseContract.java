package com.devi.kamusku.db;

import android.provider.BaseColumns;

public class DatabaseContract {
   public static String TABLE_INDO = "tb_indo";
   public static String TABLE_ENG = "tb_eng";

    public static final class KamusColumns implements BaseColumns{
        public static String KATA = "kata";
        public static String ARTI = "arti";
    }
}
