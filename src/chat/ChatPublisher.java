package chat;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import org.apache.activemq.ActiveMQConnectionFactory;
import util.ChatMessage;
import util.ChatUser;

/**
 * Classe Produtora responsável pela publicação no Topic.
 *
 * @author Master
 */
public class ChatPublisher {

    private TopicConnectionFactory factory;
    private Topic topic;
    private TopicConnection connection;
    private TopicSession session;
    private TopicPublisher publisher;
    private ChatUser user;

    /**
     *
     * @param user Pseudo usuário para identificação de mensagem.
     */
    public ChatPublisher(ChatUser user) {

        this.user = user;

        try {
            //Factory
            factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            //Connection
            connection = factory.createTopicConnection();

            //Session
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Topic (cria um tópico se não existir)
            topic = session.createTopic("MSGS.PRINCIPAL");

            //Publisher
            publisher = session.createPublisher(topic);

        } catch (JMSException e) {
            System.err.println("Exception Publisher - " + e.getMessage());
        }
    }

    /**
     * Envia mensagem privada
     *
     * @param text O texto da mensagem
     * @param destinatarioUUID O uuid do pseudo usuário que irá receber a mensagem.
     */
    public void send(String text, String destinatarioUUID) {
        try {
            TextMessage message = ChatMessage.makeMessage(session, user, text, destinatarioUUID);
            publisher.publish(message);
        } catch (JMSException e) {
            System.err.println("Exception send - " + e.getMessage());
        }
    }

    /**
     * Envia mensagem global
     *
     * @param text O texto da mensagem
     */
    public void send(String text) {
        try {
            TextMessage message = ChatMessage.makeMessage(session, user, text);
            publisher.publish(message);

        } catch (JMSException e) {
            System.err.println("Exception send - " + e.getMessage());
        }
    }
    
    public void welcome() {
        try {
            TextMessage message = ChatMessage.makeWelcomeMessage(session, user);
            publisher.publish(message);

        } catch (JMSException e) {
            System.err.println("Exception send - " + e.getMessage());
        }
    }

}
