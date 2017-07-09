package cg.ndokisteam.kitokotest.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import cg.ndokisteam.kitokotest.contracts.MessageContract;
import cg.ndokisteam.kitokotest.models.Message;

/**
 *INTERACTION AVEC LA BASE DE DONNNEES
 */

public class MessageDao extends MyHelper{


    public MessageDao(Context context) {
        super(context);
    }

    //Recuperation de tous les donnees

    public ArrayList<Message> getAllMessages(){

        SQLiteDatabase db=getWritableDatabase();

       final String REKET="SELECT "+
                MessageContract.MessageEntries.MESSAGE+","+
                MessageContract.MessageEntries.SEXE+","+
                MessageContract.MessageEntries.TYPE_MSG+","+
                MessageContract.MessageEntries.VALIDATION+" FROM "+
                MessageContract.MessageEntries.TABLE_NAME;

        ArrayList<Message> listMessages=new ArrayList<>();
        Cursor cursor = db.rawQuery(REKET,null);
        if(cursor.moveToFirst())
            if(cursor.moveToFirst())
                do{
                    listMessages.add(new Message(cursor.getString(0),cursor.getLong(1),cursor.getLong(2),cursor.getString(3)));
                }while (cursor.moveToNext());

        return listMessages;
    }

    //recuperation des messages en fonction du sexe de la personne

    public ArrayList<Message> getMessagesGender(Long sexe){

        SQLiteDatabase db=getWritableDatabase();

        final String REKET="SELECT "+
                MessageContract.MessageEntries.MESSAGE+","+
                MessageContract.MessageEntries.SEXE+","+
                MessageContract.MessageEntries.TYPE_MSG+","+
                MessageContract.MessageEntries.VALIDATION+" FROM "+
                MessageContract.MessageEntries.TABLE_NAME+" WHERE "+
                MessageContract.MessageEntries.SEXE+" ="+sexe;;

        ArrayList<Message> listMsgGender=new ArrayList<>();
        Cursor cursor = db.rawQuery(REKET,null);
        if(cursor.moveToFirst())
            if(cursor.moveToFirst())
                do{
                    listMsgGender.add(new Message(cursor.getString(0),cursor.getLong(1),cursor.getLong(2),cursor.getString(3)));
                }while (cursor.moveToNext());

        return listMsgGender;
    }


}
