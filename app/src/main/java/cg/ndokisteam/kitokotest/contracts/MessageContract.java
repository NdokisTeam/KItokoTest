package cg.ndokisteam.kitokotest.contracts;

import android.provider.BaseColumns;

/**
 *Classe qui sera utilisé pour la creation de Sqlite
 */

public class MessageContract {

    public final static class MessageEntries implements BaseColumns {

        public final static String MESSAGE = "message";
        public final static String SEXE = "genre";
        public final static String TYPE_MSG = "type";
        public final static String VALIDATION = "validation";

        public final static String TABLE_NAME = "messagesTbl";

    }
}

