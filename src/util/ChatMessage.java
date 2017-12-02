package util;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Classe auxiliar para manipulação e formatação de mensagens
 *
 * @author Master
 */
public class ChatMessage {
    
    public static final String TYPE_WELCOME = "Welcome";
    public static final String TYPE_GLOBAL = "Global";
    public static final String TYPE_PRIVATE = "Private";
    
    /**
     * Cria uma mensagem de boas vindas encapsulando as devidas propriedades
     *
     * @param session Session criada a partir da conexão com o broker
     * @param me O pseudo usuário remetente
     *
     * @return TextMessage mensagem a ser publicada pelo publisher
     */
    public static TextMessage makeWelcomeMessage(Session session, ChatUser me) {

        TextMessage message = null;

        try {
            message = session.createTextMessage();
            message.setStringProperty("Type", TYPE_WELCOME);
            message.setStringProperty("RemetenteName", me.getName());
            message.setStringProperty("RemetenteUID", me.getUUID());
        } catch (JMSException e) {
            System.err.println("Erro ao criar mensagem: " + e.getMessage());
        }

        return message;
    }

    /**
     * Cria uma mensagem global encapsulando as devidas propriedades
     *
     * @param session Session criada a partir da conexão com o broker
     * @param me O pseudo usuário remetente
     * @param text Texto do corpo da mensagem
     *
     * @return TextMessage mensagem a ser publicada pelo publisher
     */
    public static TextMessage makeMessage(Session session, ChatUser me, String text) {

        TextMessage message = null;

        try {
            message = session.createTextMessage();

            message.setText(text);
            message.setStringProperty("Type", TYPE_GLOBAL);
            message.setStringProperty("RemetenteName", me.getName());
            message.setStringProperty("RemetenteUID", me.getUUID());

        } catch (JMSException e) {
            System.err.println("Erro ao criar mensagem: " + e.getMessage());
        }

        return message;
    }
    
    

    /**
     * Cria uma mensagem privada encapsulando as devidas propriedades
     *
     * @param session Session criada a partir da conexão com o broker
     * @param me O pseudo usuário remetente
     * @param text Texto do corpo da mensagem
     * @param destinatario O pseudo usuário destinatário
     *
     * @return TextMessage mensagem a ser publicada pelo publisher
     */
    public static TextMessage makeMessage(Session session, ChatUser me, String text, String destinatario) {

        TextMessage message = null;

        try {
            message = session.createTextMessage();
            message.setText(text);
            message.setStringProperty("Type", TYPE_PRIVATE);
            message.setStringProperty("RemetenteName", me.getName());
            message.setStringProperty("RemetenteUID", me.getUUID());
            message.setStringProperty("Destinatario", destinatario);
        } catch (JMSException e) {
            System.err.println("Erro ao criar mensagem: " + e.getMessage());
        }

        return message;
    }

    /**
     * Formata uma mensagem global recebida para ser printada
     *
     * @param message mensagem
     * @return String formatada pronta para ser exibida
     */
    public static String formatMessage(TextMessage message) {
        try {
            return message.getStringProperty("RemetenteName")
                    + " [" + message.getStringProperty("RemetenteUID") + "]: "
                    + message.getText() + "\n";
        } catch (JMSException e) {
            System.err.println("Erro ao ler mensagem: " + e.getMessage());
        }

        return null;
    }

    /**
     * Formata uma mensagem privada recebida para ser printada
     *
     * @param message mensagem
     * @return String formatada pronta para ser exibida
     */
    public static String formatPrivateMessage(TextMessage message) {
        try {
            return message.getStringProperty("RemetenteName")
                    + " [" + message.getStringProperty("RemetenteUID") + "] privadamente: "
                    + message.getText() + "\n";
        } catch (JMSException e) {
            System.err.println("Erro ao ler mensagem: " + e.getMessage());
        }

        return null;
    }
    
    /**
     * Formata uma mensagem de boas vindas
     *
     * @param message mensagem
     * @return String formatada pronta para ser exibida
     */
    public static String formatWelcomeMessage(TextMessage message) {
        
        try {
            return message.getStringProperty("RemetenteName")
                    + " [" + message.getStringProperty("RemetenteUID") + "]"
                    + " acabou de entrar na sala!\n";
        } catch (JMSException e) {
            System.err.println("Erro ao ler mensagem: " + e.getMessage());
        }
        
        return null;
    }
}
