package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * This is a contract class where it's declared as a final because it can't be extended or implement
 * anything, this class only provides constants.
 */
public final class PetContract {

    private PetContract(){};

    public static abstract class PetEntry implements BaseColumns {

        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BREED = "breed";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_WEIGHT = "weight";

        /**
         * Values for gender
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

    }
}
