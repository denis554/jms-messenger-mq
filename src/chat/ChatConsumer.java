package chat;

import gui.JFrameChat;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.activemq.ActiveMQConnectionFactory;
import util.ChatMessage;
import util.ChatUser;

/**
 * Classe consumidora assíncrona para um Topic. 
 *
 * @author Master
 */
public class ChatConsumer {

    private TopicConnectionFactory factory;
    private Topic topic;
    private TopicConnection connection;
    private TopicSession session;

    /**
     * @param me Pseudo usuário que esta utilizando o chat.
     * @param form JFrame onde possuí o elemento de texto para printar as msgs.
     * É necessário implementar a função writeMessage().
     */
    public ChatConsumer(JFrameChat form, ChatUser me) {

        try {
            //Factory
            factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            //Connection
            connection = factory.createTopicConnection();

            //Session
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Topic (cria um tópico se não existir)
            topic = session.createTopic("MSGS.PRINCIPAL");

            //Receptor
            TopicSubscriber subscriber = session.createSubscriber(topic);

            // Seta um MessageListener para recebimento de mensagens
            // onMessage() sendo implementado a partir de uma classe anônima
            subscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message received) {
                    try {
                        TextMessage message = (TextMessage) received;
                        String type = message.getStringProperty("Type");
                        
                        if (type.equals(ChatMessage.TYPE_WELCOME))
                            form.writeMessage(ChatMessage.formatWelcomeMessage(message));
                        else if (type.equals(ChatMessage.TYPE_GLOBAL))
                            form.writeMessage(ChatMessage.formatMessage(message));
                        else if (type.equals(ChatMessage.TYPE_PRIVATE) && 
                                (message.getStringProperty("Destinatario").equals(me.getUUID()) || message.getStringProperty("RemetenteUID").equals(me.getUUID())))
                            form.writeMessage(ChatMessage.formatPrivateMessage(message));
                        
                    } catch (Exception e) {
                        System.out.println("Erro onMessage: " + e.getMessage());
                    }
                }
            });

            //Starta conexao
            connection.start();

        } catch (JMSException e) {
            System.err.println("Erro Consumer: " + e.getMessage());
        }
    }
}
