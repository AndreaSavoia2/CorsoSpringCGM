package it.cgmconsulting.myblog.entity.enumeration;

public enum AuthorityName {

    ADMIN,
    WRITER, // scrive ipost
    MEMBER, // scrive commenti e vota i post
    MODERATOR, // si occupa delel segnalazioni
    GUEST  // stato di un utante che si è registrato ma non ancora confermato la propria email

}
