package util;

import java.util.UUID;

/**
 * Classe auxiliar para simulação de persistencia de usuáio
 *
 * @author Master
 */
public class ChatUser {

    private final String uuid;
    private final String name;

    /**
     * @param name String com o nome de usuário
     */
    public ChatUser(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     * @return String da UUID
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * @return String com o nome do usuário
     */
    public String getName() {
        return this.name;
    }

}
