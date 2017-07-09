package cg.ndokisteam.kitokotest.models;

/*
* Classe representation de la Table MessageTbl
*
* */

public class Message {

    private Long id;
    private String message;
    private Long sexe;
    private Long typeMessage;//Positif ou Negatif
    private String validation;

    //Constructeurs


    public Message() {
    }

    public Message(Long id) {
        this.setId(id);
    }

    public Message(String message, Long sexe, Long typeMessage, String validation) {
        this.setMessage(message);
        this.setSexe(sexe);
        this.setTypeMessage(typeMessage);
        this.setValidation(validation);
    }


    //LES PROPPRIETES

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSexe() {
        return sexe;
    }

    public void setSexe(Long sexe) {
        this.sexe = sexe;
    }

    public Long getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(Long typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }




}
